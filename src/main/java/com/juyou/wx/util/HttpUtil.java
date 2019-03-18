package com.juyou.wx.util;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpUtil {

    public static String doPost(String actionUrl, Map<String, String> params) {
        return doPost(actionUrl, params, "UTF-8");
    }

    public static String doPost_GBK(String actionUrl, Map<String, String> params) {
        return doPost(actionUrl, params, "GBK");
    }


    private static String doPost(String actionUrl, Map<String, String> params, String charset) {
        String result = "";

        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }


        CloseableHttpResponse response = null;
        try {
            HttpPost httpPost = new HttpPost(actionUrl);
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, charset));
            httpPost.setHeader("Content-Type", "application/json");

            CloseableHttpClient httpclient = HttpClients.createDefault();
            response = httpclient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            InputStream io = entity.getContent();
            result = getContent(io);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;

    }


    public static String doGet(String actionUrl, Map<String, String> params) {
        return doGet(actionUrl, params, "UTF-8");
    }

    public static String doGet_GBK(String actionUrl, Map<String, String> params) {
        return doGet(actionUrl, params, "GBK");
    }


    private static String doGet(String actionUrl, Map<String, String> params, String charset) {
        String result = "";
        CloseableHttpResponse response = null;
        try {

            String paramsStr = "";
            if (null != params) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    paramsStr += "&" + URLEncoder.encode(entry.getKey(), charset) + "=" + URLEncoder.encode(entry.getValue(), charset);
                }
            }

            String url = (-1 == actionUrl.indexOf("?") ? (actionUrl + "?1=1") : actionUrl) + paramsStr;
            response = HttpClients.createDefault().execute(new HttpGet(url));
            HttpEntity entity = response.getEntity();
            InputStream io = entity.getContent();
            result = getContent(io);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;

    }


    public static void printContent(InputStream io) throws IOException {
        BufferedReader read = new BufferedReader(new InputStreamReader(io, "UTF-8"));
        String line = null;
        while (null != (line = read.readLine())) {
            System.out.println(line);
        }
    }

    public static String getContent(InputStream io) throws IOException {
        StringBuffer sb = new StringBuffer();
        BufferedReader read = new BufferedReader(new InputStreamReader(io, "UTF-8"));
        String line = null;
        while (null != (line = read.readLine())) {
            sb.append(line);
        }
        return sb.toString();
    }


    public static String doPostJson(String url, String json) {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultString = "";
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            // 创建请求内容
            StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);
            // 执行http请求
            response = httpClient.execute(httpPost);
            resultString = EntityUtils.toString(response.getEntity(), "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resultString;
    }


    /**
     * 获取完成的请求url
     * @param
     * @return
     */
    public static String getRequestURL(HttpServletRequest req)  {
        StringBuffer url = req.getRequestURL();
        String param=req.getQueryString();
        StringBuilder sb = new StringBuilder(500);
        sb.append(url.toString());
        if(null!=param){
            sb.append("?");
            sb.append(param);
        }
        return  sb.toString();
    }

}
