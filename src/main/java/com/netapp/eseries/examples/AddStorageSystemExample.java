package com.netapp.eseries.examples;

import com.netapp.eseries.api.EseriesProxy;

public class AddStorageSystemExample {

  private static String apiProxyHost = "10.65.57.19";
  private static String apiProxyPort = "8443";
  private static String apiUsername = "rw";
  private static String apiPassword = "rw";
  private static String storageSystem = "10.65.57.19";

  public static void main(String[] args) {
    EseriesProxy eseriesProxy =
        new EseriesProxy(apiProxyHost, apiProxyPort, apiUsername, apiPassword);

    final String STORAGE_SYSTEMS_URL_PATH = "/devmgr/v2/storage-systems";

    try {
      String input = "{\"controllerAddresses\":[\"" + storageSystem + "\"]}";
      eseriesProxy.executePost(STORAGE_SYSTEMS_URL_PATH, input);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
