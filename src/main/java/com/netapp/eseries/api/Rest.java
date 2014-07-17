package com.netapp.eseries.api;

import java.net.URL;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class Rest {
  RestHelper restHelper = new RestHelper();

  public HttpsURLConnection getLoginSession(String hostName, String port) {
    String loginUri = "https://" + hostName + ":" + port + "/devmgr/utils/login";
    HttpsURLConnection conn = null;
    try {
      restHelper.ignoreSslCertificates();

      URL url = new URL(loginUri);
      conn = (HttpsURLConnection) url.openConnection();
      conn.setRequestMethod("GET");
      conn.setDoInput(true);
      conn.setDoOutput(true);
      conn.setRequestProperty("Content-Type", "application/json");
    } catch (Exception e) {
      e.printStackTrace();
    }
    return conn;
  }

  public HttpsURLConnection getApiSession(String urlString, String method,
      Map<String, String> cookieMap) {

    HttpsURLConnection conn = null;
    try {
      restHelper.ignoreSslCertificates();

      URL url = new URL(urlString);
      conn = (HttpsURLConnection) url.openConnection();
      conn.setRequestMethod(method);
      conn.setDoInput(true);
      conn.setDoOutput(true);
      conn.setRequestProperty("Content-Type", "application/json");
      conn.setRequestProperty("Accept", "application/json");

      String session = "JSESSIONID=" + cookieMap.get("JSESSIONID");
      conn.setRequestProperty("Cookie", session);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return conn;
  }
}
