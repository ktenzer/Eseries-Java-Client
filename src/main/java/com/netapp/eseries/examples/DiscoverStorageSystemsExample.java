package com.netapp.eseries.examples;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.netapp.eseries.api.EseriesProxy;

public class DiscoverStorageSystemsExample {
  private static String apiProxyHost = "10.65.57.19";
  private static String apiProxyPort = "8443";
  private static String apiUsername = "rw";
  private static String apiPassword = "rw";
  private static String startIp = "10.65.57.19";
  private static String endIp = "10.65.57.19";
  private static String connectionTimeout = "30";
  private static String maxPortsToUse = "20";

  public static void main(String[] args) {
    EseriesProxy eseriesProxy =
        new EseriesProxy(apiProxyHost, apiProxyPort, apiUsername, apiPassword);

    final String DISCOVER_STORAGE_SYSTEMS_URL_PATH = "/devmgr/v2/discovery";

    try {
      String input =
          "{\"startIP\":\"" + startIp + "\", \"endIP\":\"" + endIp + "\", \"connectionTimeout\": "
              + connectionTimeout + ", \"maxPortsToUse\": " + maxPortsToUse + "}";
      eseriesProxy.executePost(DISCOVER_STORAGE_SYSTEMS_URL_PATH, input);
      String response = eseriesProxy.executeGet(DISCOVER_STORAGE_SYSTEMS_URL_PATH);

      while (isDiscoveryRunning(response)) {
        System.out.println("Discovery is running, sleeping 10 seconds");
        Thread.sleep(10000);

        response = eseriesProxy.executeGet(DISCOVER_STORAGE_SYSTEMS_URL_PATH);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    System.out.println("Discovery completed successfully");
  }

  private static boolean isDiscoveryRunning(String response) {

    JsonParser parser = new JsonParser();
    JsonObject jsonObject = (JsonObject) parser.parse(response);

    return Boolean.valueOf(jsonObject.get("discoverProcessRunning").toString());
  }
}
