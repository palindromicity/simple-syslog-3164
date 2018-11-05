package com.github.palindromicity.syslog.dsl;

/**
 * Keys for use in maps of Syslog Data.
 */
public enum SyslogFieldKeys {
  MESSAGE("syslog.message"),
  HEADER_HOSTNAME("syslog.header.hostName"),
  HEADER_PRI("syslog.header.pri"),
  HEADER_PRI_SEVERITY("syslog.header.severity"),
  HEADER_PRI_FACILITY("syslog.header.facility"),
  HEADER_TIMESTAMP("syslog.header.timestamp");

  private String field;

  SyslogFieldKeys(String field) {
    this.field = field;
  }

  /**
   * Returns the value.
   *
   * @return the Field value.
   */
  public String getField() {
    return field;
  }
}
