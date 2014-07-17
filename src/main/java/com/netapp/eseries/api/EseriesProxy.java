package com.netapp.eseries.api;

import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class EseriesProxy {
  private static Rest rest = new Rest();
  private static RestHelper restHelper = new RestHelper();

  private String apiProxyHost;
  private String apiProxyPort;
  private String user;
  private String password;

  public EseriesProxy(String apiProxyHost, String apiProxyPort, String user, String password) {
    this.apiProxyHost = apiProxyHost;
    this.apiProxyPort = apiProxyPort;
    this.user = user;
    this.password = password;
  }

  public void executePost(String urlPath, String input) {
    HttpsURLConnection conn = rest.getLoginSession(apiProxyHost, apiProxyPort);
    Map<String, String> cookieMap = restHelper.getCookieMap(conn, user, password);

    String url = "https://" + apiProxyHost + ":" + apiProxyPort + urlPath;
    conn = rest.getApiSession(url, "POST", cookieMap);

    restHelper.addApiInput(conn, input);

    String response = restHelper.getResponse(conn);
    restHelper.printJson(response);

    conn.disconnect();
  }

  public String executeGet(String urlPath) {
    HttpsURLConnection conn = rest.getLoginSession(apiProxyHost, apiProxyPort);
    Map<String, String> cookieMap = restHelper.getCookieMap(conn, user, password);

    String url = "https://" + apiProxyHost + ":" + apiProxyPort + urlPath;
    conn = rest.getApiSession(url, "GET", cookieMap);
    String response = restHelper.getResponse(conn);
    restHelper.printJson(response);
    conn.disconnect();

    return response;
  }
}
