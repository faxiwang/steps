package com.steps.writer_log.util;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
public class IpUtils {
  public static String getIpAddr(HttpServletRequest request) {
    String ipAddress = request.getHeader("x-forwarded-for");
    boolean unknown = (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress));
    if (unknown) {
      ipAddress = request.getHeader("Proxy-Client-IP");
    }
    if (unknown) {
      ipAddress = request.getHeader("WL-Proxy-Client-IP");
    }
    if (unknown) {
      ipAddress = request.getHeader("HTTP_CLIENT_IP");
    }
    if (unknown) {
      ipAddress = request.getHeader("HTTP_X_FORWARDED_FOR");
    }
    if (unknown) {
      ipAddress = request.getRemoteAddr();
      if (ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")) {
        //根据网卡取本机配置的IP
        try {
          InetAddress inet = InetAddress.getLocalHost();
          ipAddress = inet.getHostAddress();
        } catch (UnknownHostException e) {
          log.error(e.getMessage(), e);
        }
      }
    }
    //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割  "***.***.***.***".length() = 15

    if (ipAddress != null && ipAddress.length() > 15 && ipAddress.contains(",")) {
      ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
    }
    return ipAddress;
  }

  /**
   * 获得MAC地址
   *
   * @param ip
   * @return
   */
  public static String getMACAddress(String ip) {
    String str = "";
    String macAddress = "";
    try (InputStreamReader ir = new InputStreamReader(Runtime.getRuntime().exec("nbtstat -A " + ip).getInputStream(), "UTF-8");
         LineNumberReader input = new LineNumberReader(ir)) {
      for (int i = 1; i < 100; i++) {
        str = input.readLine();
        if (str != null && str.indexOf("MAC Address") > 1) {
          macAddress = str.substring(str.indexOf("MAC Address") + 14, str.length());
          break;
        }
      }
    } catch (IOException e) {
      log.error(e.getMessage(), e);
    }
    return macAddress;
  }
}