[![Build Status](https://travis-ci.org/palindromicity/simple-syslog-3164.svg?branch=master)](https://travis-ci.org/palindromicity/simple-syslog-3164)

### Simple Syslog 3164

---

A java library for parsing valid Syslog [IETF RFC 3164](https://tools.ietf.org/html/rfc3164) logs.
The library provides it's own parser implementation, but also exposes the Antlr generated base classes
and interfaces should you want your own implementation.

## Basic Usage

A simple, default usage to parser a Syslog RFC 3164 log line is to build a SyslogParser
with the defaults, and pass it the line.

```java
 SyslogParser parser = new SyslogParserBuilder().build();
 Map<String,Object> syslogMap = parser.parseLine(syslogLine);

```

To parse a number of Syslog lines together, say from a file you would create
a `Reader` and all `parseLines`

```java
  List<Map<String,Object>> syslogMapList = null;
  SyslogParser parser = new SyslogParserBuilder().build();
  try (Reader reader = new BufferedReader(new FileReader(new File(fileName)))) {
      syslogMapList = parser.parseLines(reader);
  }

```

Both `parseLine` and `parseLines` also provide a functional interface if you prefer that style.
Just pass a `Consumer` to the function.

```java
 SyslogParser parser = new SyslogParserBuilder().build();
 syslogMap = parser.parseLine(syslogLine, (syslogMap) -> {
   // do something with map
 });

```

```java
  SyslogParser parser = new SyslogParserBuilder().build();
  try (Reader reader = new BufferedReader(new FileReader(new File(fileName)))) {
      parser.parseLines(reader, (map) -> {
        // do something with each map
      });
  }

```

```java
 SyslogParser parser = new SyslogParserBuilder().build();
  try (Reader reader = new BufferedReader(new FileReader(new File(fileName)))) {
      parser.parseLines(reader, (map) -> {
        // do something with each map
      }, (line, throwable) -> {
        // do something for a failed line
      });
  }
```

### Options

The `SyslogParserBuilder` supports options for changing the `AllowableVariations` and the `KeyProvider`.

##### AllowableDeviations

Allowable deviations from the RFC 3164 specification. This allows for fields required by the specification, but perhaps
omitted by convention to be missing, and a line that is by specification technically incorrect to still parse.

This is specificed by an {@code EnumSet}

```java
/**
   * Properly formed RFC 5424 Syslog.
   */
  NONE,
  /**
   *  Syslog that does not have PRIORITY.
   */
  PRIORITY
```

##### KeyProvider

A `KeyProvider` is used to provide the map keys for the Syslog data.
The default `KeyProvider` : `DefaultKeyProvider` provides keys using the `SyslogKeys`:

```java
 MESSAGE("syslog.message"),
   HEADER_HOSTNAME("syslog.header.hostName"),
   HEADER_PRI("syslog.header.pri"),
   HEADER_PRI_SEVERITY("syslog.header.severity"),
   HEADER_PRI_FACILITY("syslog.header.facility"),
   HEADER_TIMESTAMP("syslog.header.timestamp"),
```

A custom `KeyProvider` can be supplied to the `SyslogParserBuilder` if there is a different key strategy required.

### Creating your own Parsers

Simple Syslog 3164 uses [Antlr 4](http://www.antlr.org) to generate the `Listener` that the parser is based on.
The generated `Rfc3164Listener` and `Rfc3164Visitor` interfaces, or `Rfc3164BaseListener` and `Rfc3164BaseVisitor` classes,
may be used to implement new parsers as well in the event that you prefer different handling.

Implementors would then build their own parsers or builders etc. In other words the use of this library would
minimally be the Antlr classes alone.

For example you would build a 'parser' that used your implementations, most likely implemented like this:

```java
    Rfc3164Lexer lexer = new Rfc3164Lexer(new ANTLRInputStream(syslogLine));
    Rfc3164Parser parser = new Rfc3164Parser(new CommonTokenStream(lexer));
    Rfc3164Listener listener = new MyCustomListener(keyProvider);
    parser.addParseListener(listener);
    Rfc3164Parser.Syslog_msgContext ctx = parser.syslog_msg();
    return listener.getMyCustomResult();
```

---

```xml
<dependency>
  <groupId>com.github.palindromicity</groupId>
  <artifactId>simple-syslog-3164</artifactId>
  <version>VERSION</version>
  <type>pom</type>
</dependency>
```
