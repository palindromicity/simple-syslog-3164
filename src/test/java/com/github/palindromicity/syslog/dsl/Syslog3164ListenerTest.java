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

package com.github.palindromicity.syslog.dsl;

import java.util.EnumSet;
import java.util.Map;

import com.github.palindromicity.syslog.AllowableDeviations;
import com.github.palindromicity.syslog.DefaultKeyProvider;
import com.github.palindromicity.syslog.dsl.generated.Rfc3164Lexer;
import com.github.palindromicity.syslog.dsl.generated.Rfc3164Parser;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.Assert;
import org.junit.Test;

public class Syslog3164ListenerTest {

  private static final String expectedMessageOne = "CISE_RADIUS_Accounting 0018032501 1 0 2018-09-14 10:54:09.095"
      + " +10:00 0221114759 3002 NOTICE Radius-Accounting: RADIUS Accounting watchdog update, ConfigVersionId=73, "
      + "Device IP Address=00.00.000.0, RequestLatency=2, NetworkDeviceName=foo, "
      + "User-Name=ACCOUNT-01\\\\\\\\D622322, NAS-IP-Address=00.00.000.0, NAS-Port=50742, "
      + "Framed-IP-Address=00.00.000.000, Class=CACS:0A3D720400016DBFE530A22E:lzpqrst/323409315/14578982, "
      + "Called-Station-ID=00-CA-E5-B1-21-AA, Calling-Station-ID=54-E1-AD-A1-27-72, Acct-Status-Type=Interim-Update, "
      + "Acct-Delay-Time=10, Acct-Input-Octets=379294, Acct-Output-Octets=1053336, Acct-Session-Id=00025EB8, "
      + "Acct-Input-Packets=1657, Acct-Output-Packets=2018, Event-Timestamp=1536886439, NAS-Port-Type=Ethernet, "
      + "NAS-Port-Id=GigabitEthernet7/0/42, cisco-av-pair=dc-profile-name=Microsoft-Workstation, "
      + "cisco-av-pair=dc-device-name=MSFT 5.0, cisco-av-pair=dc-device-class-tag=Workstation:Microsoft-Workstation, "
      + "cisco-av-pair=dc-certainty-metric=10, "
      + "cisco-av-pair=dc-opaque=\\000\\000\\000\\002\\000\\000\\000\\001\\000\\000\\000\\000, "
      + "cisco-av-pair=dc-protocol-map=9, "
      + "cisco-av-pair=dhcp-option=pad="
      + "1b:2e:01:08:ff:2e:01:08:ff:0a:90:84:51:0a:2c:08:0a:d0:52:31:0a:d0:5a:1b:2e:01:08:ff:2e:01:08:ff:79:f9:2b:"
      + "ff:43:17:73:6d:73:62:6f:6f:74:5c:78:38:36:5c:77:64:73:6e:62:70:2e:63:6f:6d:00:ff:6f:6d:00:ff:00:00:00:00:00:"
      + "00:00:00:00:00:00:00:00:00:00:00:00:00:00:22:23:54:00:00, cisco-av-pair=dhcp-option=00:ff:00:00, "
      + "cisco-av-pair=dhcp-option=dhcp-parameter-request-list="
      + "1\\\\, 15\\\\, 3\\\\, 6\\\\, 44\\\\, 46\\\\, 47\\\\, 31\\\\, 33\\\\, 121\\\\, 249\\\\, 43\\\\, 252,"
      + " cisco-av-pair=dhcp-option=dhcp-class-identifier=MSFT 5.0, cisco-av-pair=dhcp-option=host-name=W00000PC0R1JC3,"
      + " cisco-av-pair=dhcp-option=dhcp-client-identifier=01:54:e1:ad:a1:27:72,"
      + " cisco-av-pair=dhcp-option=dhcp-message-type=8, cisco-av-pair=audit-session-id=0A3D720400016DBFE530A22E,"
      + " cisco-av-pair=method=dot1x, AcsSessionID=lzpqrst/323409315/14579377, SelectedAccessService=PEAP_MAB,"
      + " Step=11004, Step=11017, Step=15049, Step=15008, Step=22094, Step=11005, NetworkDeviceGroups=Stage#Deployment"
      + " Type#Secure Mode D2, NetworkDeviceGroups=Location#All Locations#Placename#500 Exhibition St"
      + " CompanyPlace#Level 18, NetworkDeviceGroups=Device Type#All Device Types#Access Switch#Catalyst 3850,"
      + " NetworkDeviceGroups=Location Type#Location Type#Office, CPMSessionID=0A3D720400016DBFE530A22E,"
      + " Stage=Stage#Deployment Type#Secure Mode D2, Location=Location#All Locations#Placename#500 Exhibition St"
      + " CompanyPlace#Level 18, Device Type=Device Type#All Device Types#Access Switch#Catalyst 3850, Network Device"
      + " Profile=Cisco, Location Type=Location Type#Location Type#Office";

  private static final String expectedHostNameOne = "lzpqrst-admin.in.mycompany.com.lg";
  private static final String expectedPriOne = "181";
  private static final String expectedTimestampOne = "2018-09-14T00:54:09+00:00";
  private static final String expectedFacilityOne = "22";
  private static final String expectedSeverityOne = "5";

  private static final String expectedHostNameTwo = "10.34.84.145";
  private static final String expectedMessageTwo = "Aug  7 00:45:43 stage-pdp01 CISE_Profiler 0000024855 1 0 "
      + "2014-08-07 00:45:43.741 -07:00 0000288542 80002 INFO  Profiler: Profiler EndPoint profiling event occurred, "
      + "ConfigVersionId=113, EndpointCertainityMetric=10, EndpointIPAddress=10.56.111.14, "
      + "EndpointMacAddress=3C:97:0E:C3:F8:F1, EndpointMatchedPolicy=Nortel-Device, EndpointNADAddress=10.56.72.127, "
      + "EndpointOUI=Wistron InfoComm(Kunshan)Co.\\,Ltd., EndpointPolicy=Nortel-Device, "
      + "EndpointProperty=StaticAssignment=false\\,PostureApplicable=Yes\\,PolicyVersion=402\\,"
      + "IdentityGroupID=0c1d9270-68a6-11e1-bc72-0050568e013c\\,Total Certainty Factor=10\\,"
      + "BYODRegistration=Unknown\\,FeedService=false\\,EndPointPolicyID=49054ed0-68a6-11e1-bc72-0050568e013c\\,"
      + "FirstCollection=1407397543718\\,MatchedPolicyID=49054ed0-68a6-11e1-bc72-0050568e013c\\,TimeToProfile=19\\,"
      + "StaticGroupAssignment=false\\,NmapSubnetScanID=0\\,DeviceRegistrationStatus=NotRegistered\\,PortalUser=, "
      + "EndpointSourceEvent=SNMPQuery Probe, EndpointIdentityGroup=Profiled, ProfilerServer=stage-pdp01.cisco.com,";
  private static final String expectedPriTwo = "181";
  private static final String expectedTimestampTwo = "Aug  6 17:26:31";
  private static final String expectedFacilityTwo = "22";
  private static final String expectedSeverityTwo = "5";

  @Test
  @SuppressWarnings("unchecked")
  public void testAllPresent() throws Exception {
    Map<String, Object> map = handleFile("src/test/resources/logs/3164/single_ise.txt");
    Assert.assertEquals(expectedMessageOne, map.get(SyslogFieldKeys.MESSAGE.getField()));
    Assert.assertEquals(expectedHostNameOne, map.get(SyslogFieldKeys.HEADER_HOSTNAME.getField()));
    Assert.assertEquals(expectedPriOne, map.get(SyslogFieldKeys.HEADER_PRI.getField()));
    Assert.assertEquals(expectedSeverityOne, map.get(SyslogFieldKeys.HEADER_PRI_SEVERITY.getField()));
    Assert.assertEquals(expectedFacilityOne, map.get(SyslogFieldKeys.HEADER_PRI_FACILITY.getField()));
    Assert.assertEquals(expectedTimestampOne, map.get(SyslogFieldKeys.HEADER_TIMESTAMP.getField()));
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testAllPresentOldDate() throws Exception {
    Map<String, Object> map = handleFile("src/test/resources/logs/3164/single_ise_old_date.txt");
    Assert.assertEquals(expectedMessageTwo, map.get(SyslogFieldKeys.MESSAGE.getField()));
    Assert.assertEquals(expectedHostNameTwo, map.get(SyslogFieldKeys.HEADER_HOSTNAME.getField()));
    Assert.assertEquals(expectedPriTwo, map.get(SyslogFieldKeys.HEADER_PRI.getField()));
    Assert.assertEquals(expectedSeverityTwo, map.get(SyslogFieldKeys.HEADER_PRI_SEVERITY.getField()));
    Assert.assertEquals(expectedFacilityTwo, map.get(SyslogFieldKeys.HEADER_PRI_FACILITY.getField()));
    Assert.assertEquals(expectedTimestampTwo, map.get(SyslogFieldKeys.HEADER_TIMESTAMP.getField()));
  }

  @Test(expected = ParseException.class)
  @SuppressWarnings("unchecked")
  public void testWithDeviation() throws Exception {
    Map<String, Object> map = handleFile("src/test/resources/logs/3164/single_ise_deviation.txt");
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testWithDeviationAllowed() throws Exception {
    Map<String, Object> map = handleFile("src/test/resources/logs/3164/single_ise_deviation.txt",
        EnumSet.of(AllowableDeviations.PRIORITY));
    Assert.assertEquals(expectedMessageOne, map.get(SyslogFieldKeys.MESSAGE.getField()));
    Assert.assertEquals(expectedHostNameOne, map.get(SyslogFieldKeys.HEADER_HOSTNAME.getField()));
    Assert.assertNull(map.get(SyslogFieldKeys.HEADER_PRI.getField()));
    Assert.assertEquals(expectedTimestampOne, map.get(SyslogFieldKeys.HEADER_TIMESTAMP.getField()));
  }

  private static Map<String, Object> handleFile(String fileName) throws Exception {
    return handleFile(fileName, EnumSet.of(AllowableDeviations.NONE));
  }

  private static Map<String, Object> handleFile(String fileName, EnumSet<AllowableDeviations> deviations)
      throws Exception {
    Rfc3164Lexer lexer = new Rfc3164Lexer(new ANTLRFileStream(fileName));
    Rfc3164Parser parser = new Rfc3164Parser(new CommonTokenStream(lexer));
    Syslog3164Listener listener = new Syslog3164Listener(new DefaultKeyProvider(), deviations);
    parser.addParseListener(listener);
    Rfc3164Parser.Syslog_msgContext ctx = parser.syslog_msg();
    return listener.getMsgMap();
  }

}