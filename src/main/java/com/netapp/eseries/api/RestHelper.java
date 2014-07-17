package com.netapp.eseries.api;

import java.io.IOException;
import java.io.OutputStream;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class RestHelper {

  public void ignoreSslCertificates() {
    try {
      // Create a trust and accept all certificates without any validation
      TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
          return null;
        }

        public void checkClientTrusted(X509Certificate[] certs, String authType) {}

        public void checkServerTrusted(X509Certificate[] certs, String authType) {}
      }};

      final SSLContext sc = SSLContext.getInstance("SSL");
      sc.init(null, trustAllCerts, new java.security.SecureRandom());
      HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

      HostnameVerifier allHostsValid = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
          return true;
        }
      };

      HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public Map<String, String> getCookieMap(String[] cookieArray) {

    Map<String, String> cookieMap = new HashMap<String, String>();
    for (String cookieElement : cookieArray) {
      String match1 = null;
      String match2 = null;
      CharSequence inputStr = cookieElement;
      String patternStr = "(\\S+)\\=(\\S+)";
      Pattern pattern = Pattern.compile(patternStr);
      Matcher matcher = pattern.matcher(inputStr);
      boolean matchFound = matcher.find();
      if (matchFound) {
        match1 = matcher.group(1);
        match2 = matcher.group(2);
      }

      if (match1 != null && match2 != null) {
        cookieMap.put(match1, match2);
      }
    }
    return cookieMap;
  }

  public void printJson(String input) {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    JsonParser jsonParser = new JsonParser();
    JsonElement jsonElement = jsonParser.parse(input);
    String json = gson.toJson(jsonElement);
    System.out.println(json);
  }

  public void addApiInput(HttpsURLConnection conn, String input) {
    try {
      OutputStream os = conn.getOutputStream();
      os.write(input.getBytes());
      os.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public String getResponse(HttpsURLConnection conn) {
    Scanner scanner = null;
    String result = null;

    int responseCode = 0;
    try {
      responseCode = conn.getResponseCode();
    } catch (IOException e) {
      e.printStackTrace();
    }

    if (responseCode != 200) {
      try {
        scanner = new Scanner(conn.getErrorStream());
        scanner.useDelimiter("\\Z");
        result = scanner.next();
      } catch (Exception e) {
        System.out.println("Warning no response returned from API");
        result = "{}";
      }
    } else {
      try {
        scanner = new Scanner(conn.getInputStream());
        scanner.useDelimiter("\\Z");
        result = scanner.next();
      } catch (Exception e) {
        System.out.println("Warning no response returned from API");
        result = "{}";
      }
    }

    if (scanner != null) {
      scanner.close();
    }

    return result;
  }

  public Map<String, String> getCookieMap(HttpsURLConnection conn, String user, String password) {
    String input = "{\"userId\":\"" + user + "\", \"password\":\"" + password + "\" }";
    this.addApiInput(conn, input);

    String cookie = conn.getHeaderField("Set-Cookie");
    String[] cookieArray = cookie.split(";");
    Map<String, String> cookieMap = this.getCookieMap(cookieArray);

    conn.disconnect();

    return cookieMap;
  }
}
