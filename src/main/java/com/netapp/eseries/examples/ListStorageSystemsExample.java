package com.netapp.eseries.examples;

import com.netapp.eseries.api.EseriesProxy;

public class ListStorageSystemsExample {
  private static String apiProxyHost = "10.65.57.19";
  private static String apiProxyPort = "8443";
  private static String apiUsername = "rw";
  private static String apiPassword = "rw";

  public static void main(String[] args) {
    EseriesProxy eseriesProxy = new EseriesProxy(apiProxyHost, apiProxyPort, apiUsername, apiPassword);

    final String STORAGE_SYSTEMS_URL = "/devmgr/v2/storage-systems";

    try {
      eseriesProxy.executeGet(STORAGE_SYSTEMS_URL);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
