/*
 * Copyright 2018 Karlsruhe Institute of Technology.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.kit.datamanager.util;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.InetAddressValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jejkal
 */
public final class NetworkUtils{

  private final static Logger LOGGER = LoggerFactory.getLogger(NetworkUtils.class);

  private NetworkUtils(){
  }

  /**
   * Check if the provided argument represents a hostname. After disqualifying
   * the argument as IP, this implementation just performs a basic hostname
   * check via InetAddress.getByName Thus, results should be handled with care.
   *
   * @param address The potential hostname.
   *
   * @return TRUE if the argument may represent a hostname, FALSE otherwise.
   */
  public static boolean isHostname(String address){
    if(address.isEmpty() || isIp(address)){
      return false;
    }
    try{
      InetAddress res = InetAddress.getByName(address);
      return res instanceof Inet4Address || res instanceof Inet6Address;
    } catch(UnknownHostException ex){
      LOGGER.warn("Failed to check " + address + ". Returning 'false'.", ex);
      return false;
    }
  }

  /**
   * Check if two IPs and/or hostnames are matching. Matching two IPs/hostnames
   * works in some cases quite well. However, as there is no sophisticated
   * lookup implemented, the comparison may also fail in many cases, e.g. due to
   * DynDNS or load balancing. Thus, while using this implementation, one should
   * take care, that all valid IPs (possibly of the same type, e.g. IPv4) are
   * tested against the IP/hostname to check.
   *
   * @param ipOrHostname1 First argument, which can be an IP or hostname.
   * @param ipOrHostname2 Second argument, which can be an IP or hostname.
   *
   * @return TRUE if both arguments are representing the same host, FALSE
   * otherwise or if the check fails.
   */
  public static boolean matches(String ipOrHostname1, String ipOrHostname2){
    try{
      LOGGER.debug("Checking if {} and {} are matching.", ipOrHostname1, ipOrHostname2);

      if(StringUtils.equals(ipOrHostname1, ipOrHostname2)){
        LOGGER.debug("Both arguments are equal.");
        return true;
      }

      InetAddress[] all1 = InetAddress.getAllByName(ipOrHostname1);
      InetAddress[] all2 = InetAddress.getAllByName(ipOrHostname2);

      for(InetAddress a1 : all1){
        for(InetAddress a2 : all2){
          LOGGER.debug("Comparing InetAddress1 {} and InetAddress2 {}", a1, a2);
          if(a1.isLoopbackAddress() && a2.isLoopbackAddress()){
            LOGGER.debug("Both addresses are loopback addresses. Returning 'true'.");
            return true;
          } else if(a1.equals(a2)){
            LOGGER.debug("Both addresses are equal. Returning 'true'.");
            return true;
          }
        }
      }
      LOGGER.debug("Addresses {} and {} are not matching. Returning 'false'.", ipOrHostname1, ipOrHostname2);
    } catch(UnknownHostException ex){
      LOGGER.error("Failed to resolve at least one IP or hostname. Aborting and returning 'false'.", ex);
    }

    return false;
  }

//  public static String reverseDns(String hostIp) throws IOException 
//{
//    Resolver res = new ExtendedResolver();
//
//    Name name = ReverseMap.fromAddress(hostIp);
//    int type = Type.PTR;
//    int dclass = DClass.IN;
//    Record rec = Record.newRecord(name, type, dclass);
//    Message query = Message.newQuery(rec);
//    Message response = res.send(query);
//    Record[] answers = response.getSectionArray(Section.ANSWER);
//    if (answers.length == 0)
//       return hostIp;
//    else
//       return answers[0].rdataToString();
//  }
  /**
   * Check if the provided value represents an IP address. This method won't
   * check if the provided IP is accessible or not. It's only a format
   * validation.
   *
   * @param ip The IP to check (can be IPv4 or IPv6)
   *
   * @return TRUE if the IP is valid, FALSE otherwise.
   */
  public static boolean isIp(String ip){
    return isIpV4(ip) || isIpV6(ip);
  }

  /**
   * Check if the provided value represents an IPv4 address. This method won't
   * check if the provided IP is accessible or not. It's only a format
   * validation.
   *
   * @param ip The IPv4 to check.
   *
   * @return TRUE if the IP is valid, FALSE otherwise.
   */
  public static boolean isIpV4(String ip){
    return InetAddressValidator.getInstance().isValidInet4Address(ip);
  }

  /**
   * Check if the provided value represents an IPv6 address. This method won't
   * check if the provided IP is accessible or not. It's only a format
   * validation.
   *
   * @param ip The IPv6 to check.
   *
   * @return TRUE if the IP is valid, FALSE otherwise.
   */
  public static boolean isIpV6(String ip){
    return InetAddressValidator.getInstance().isValidInet6Address(ip);
  }
}
