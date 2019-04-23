/*
 * Copyright 2018 simple-syslog-3164 authors
 * All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.palindromicity.syslog;

import java.io.BufferedReader;
import java.io.Reader;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import com.github.palindromicity.syslog.dsl.DefaultErrorListener;
import com.github.palindromicity.syslog.dsl.Syslog3164Listener;
import com.github.palindromicity.syslog.dsl.generated.Rfc3164Lexer;
import com.github.palindromicity.syslog.dsl.generated.Rfc3164Parser;
import com.github.palindromicity.syslog.util.Validate;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

/**
 * {@link SyslogParser} for valid RFC 3164 syslog.
 */
class Rfc3164SyslogParser implements SyslogParser {

  /**
   * {@link com.github.palindromicity.syslog.KeyProvider} to provide keys for the
   * {@link Syslog3164Listener}.
   */
  private KeyProvider keyProvider;
  private EnumSet<AllowableDeviations> deviations;

  /**
   * Create a new {@code Rfc3164SyslogParser}.
   *
   * @param keyProvider {@link com.github.palindromicity.syslog.KeyProvider} to provide keys for the
   * {@link Syslog3164Listener}.
   * @param deviations {@link AllowableDeviations} for parsing
   */
  Rfc3164SyslogParser(KeyProvider keyProvider, EnumSet<AllowableDeviations> deviations) {
    Validate.notNull(keyProvider, "keyProvider");
    this.keyProvider = keyProvider;
    this.deviations = deviations;
  }

  @Override
  public Map<String, Object> parseLine(String syslogLine) {
    Validate.notBlank(syslogLine, "syslogLine");
    Rfc3164Lexer lexer = new Rfc3164Lexer(CharStreams.fromString(syslogLine));
    lexer.removeErrorListeners();
    lexer.addErrorListener(new DefaultErrorListener());
    Rfc3164Parser parser = new Rfc3164Parser(new CommonTokenStream(lexer));
    Syslog3164Listener listener = new Syslog3164Listener(keyProvider, deviations);
    parser.addParseListener(listener);
    parser.removeErrorListeners();
    parser.addErrorListener(new DefaultErrorListener());
    parser.syslog_msg();
    return listener.getMsgMap();
  }

  @Override
  public void parseLine(String line, Consumer<Map<String, Object>> consumer) {
    Validate.notNull(consumer, "consumer");
    consumer.accept(parseLine(line));
  }

  @Override
  public List<Map<String, Object>> parseLines(Reader reader) {
    Validate.notNull(reader, "reader");
    return new BufferedReader(reader).lines()
        .map(this::parseLine)
        .collect(Collectors.toList());
  }

  @Override
  public void parseLines(Reader reader, Consumer<Map<String, Object>> consumer) {
    Validate.notNull(reader, "reader");
    Validate.notNull(consumer, "consumer");
    new BufferedReader(reader).lines()
        .map(this::parseLine)
        .forEach(consumer);
  }

  @Override
  public void parseLines(Reader reader, Consumer<Map<String, Object>> messageConsumer,
      BiConsumer<String, Throwable> errorConsumer) {
    Validate.notNull(reader, "reader");
    Validate.notNull(reader, "messageConsumer");
    Validate.notNull(reader, "errorConsumer");

    List<String> lines = new BufferedReader(reader).lines().collect(Collectors.toList());
    lines.forEach((line) -> {
      try {
        messageConsumer.accept(parseLine(line));
      } catch (Throwable throwable) {
        errorConsumer.accept(line, throwable);
      }
    });
  }
}
