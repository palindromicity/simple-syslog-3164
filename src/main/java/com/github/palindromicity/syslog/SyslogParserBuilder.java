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

import java.util.EnumSet;

/**
 * Builder for SyslogParser instances.
 */
public class SyslogParserBuilder {

  /**
   * The {@link KeyProvider}.
   * Defaults to {@link DefaultKeyProvider}
   */
  private KeyProvider keyProvider = new DefaultKeyProvider();

  /**
   * The {@link AllowableDeviations}.
   * Defaults to {@link AllowableDeviations#NONE}
   */
  private EnumSet<AllowableDeviations> deviations = EnumSet.of(AllowableDeviations.NONE);

  /**
   * Add a {@link KeyProvider} to the builder.
   *
   * @param keyProvider the {@link KeyProvider}
   * @return {@code SyslogParserBuilder}
   */
  public SyslogParserBuilder withKeyProvider(final KeyProvider keyProvider) {
    this.keyProvider = keyProvider;
    return this;
  }

  /**
   * Add a {@link AllowableDeviations} to the builder.
   *
   * @param specification the deviations
   * @return {@code SyslogParserBuilder}
   */
  public SyslogParserBuilder withDeviations(final EnumSet<AllowableDeviations> specification) {
    this.deviations = specification;
    return this;
  }

  /**
   * Builds a new {@link SyslogParser} instance using options if provided.
   *
   * @return {@link SyslogParser}
   * @throws IllegalStateException if deviations is unknown
   */
  public SyslogParser build() {
    return new Rfc3164SyslogParser(keyProvider, deviations);
  }
}
