/*
 * Copyright 2018-2021 simple-syslog-3164 authors
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

import java.util.regex.Pattern;

/**
 * {@code KeyProvider} defines an interface for classes that can be used to provide
 * Map keys used for Syslog message parts.
 */
public interface KeyProvider {

  /**
   * Provides the key name for the MSG @see <a href="https://tools.ietf.org/html/rfc5424#section-6.4">Section 6.4</a>.
   * @return MSG key name
   */
  String getMessage();


  /**
   * Provides the key name for the HEADER HOSTNAME @see <a href="https://tools.ietf.org/html/rfc5424#section-6.2.4">Section 6.2.4</a>.
   * @return HOSTNAME key name
   */
  String getHeaderHostName();

  /**
   * Provides the key name for the HEADER PRI @see <a href="https://tools.ietf.org/html/rfc5424#section-6.2.1">Section 6.2.1</a>.
   * @return PRI key name
   */
  String getHeaderPriority();

  /**
   * Provides the key name for the Severity from the HEADER PRI @see <a href="https://tools.ietf.org/html/rfc5424#section-6.2.1">Section 6.2.1</a>.
   * @return PRI key name
   */
  String getHeaderSeverity();

  /**
   * Provides the key name for the Facility from the HEADER PRI @see <a href="https://tools.ietf.org/html/rfc5424#section-6.2.1">Section 6.2.1</a>.
   * @return PRI key name
   */
  String getHeaderFacility();

  /**
   * Provides the key name for the HEADER TIMESTAMP @see <a href="https://tools.ietf.org/html/rfc5424#section-6.2.3">Section 6.2.3</a>.
   * @return TIMESTAMP key name
   */
  String getHeaderTimeStamp();

}
