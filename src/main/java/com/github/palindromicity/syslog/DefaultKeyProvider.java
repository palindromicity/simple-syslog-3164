package com.github.palindromicity.syslog;

import java.util.regex.Pattern;

import com.github.palindromicity.syslog.dsl.SyslogFieldKeys;

/**
 * Default implementation of {@link KeyProvider}.
 *
 * <p>
 *   This implementation uses the {@link SyslogFieldKeys} for values.
 * </p>
 */
public class DefaultKeyProvider implements KeyProvider {
  @Override
  public String getMessage() {
    return SyslogFieldKeys.MESSAGE.getField();
  }

  @Override
  public String getHeaderHostName() {
    return SyslogFieldKeys.HEADER_HOSTNAME.getField();
  }

  @Override
  public String getHeaderPriority() {
    return SyslogFieldKeys.HEADER_PRI.getField();
  }

  @Override
  public String getHeaderSeverity() {
    return SyslogFieldKeys.HEADER_PRI_SEVERITY.getField();
  }

  @Override
  public String getHeaderFacility() {
    return SyslogFieldKeys.HEADER_PRI_FACILITY.getField();
  }

  @Override
  public String getHeaderTimeStamp() {
    return SyslogFieldKeys.HEADER_TIMESTAMP.getField();
  }
}
