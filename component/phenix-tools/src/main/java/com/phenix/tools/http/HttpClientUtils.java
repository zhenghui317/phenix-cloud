package com.phenix.tools.http;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Http Client 工具类
 *
 * @author wuluodan
 * @date 2018/4/28
 */
public class HttpClientUtils {
    private static String defaultCharSet = "UTF-8";
    private static CloseableHttpClient httpClient = null;
    private static CloseableHttpResponse response = null;
    private static String httpsPrefix = "https";

    /**
     * https的post请求
     *
     * @param url
     * @param param
     * @return
     */
    public static String doGet(String url, Map<String, String> param) {
        return doGet(url, param, defaultCharSet, null);
    }

    /**
     * http的post请求
     *
     * @param url
     * @param param
     * @return
     */
    public static String doGet(String url, Map<String, String> param, String charset, Header[] headers) {
        if (StringUtils.isBlank(url)) {
            return "参数url为空";
        }
        if (!url.startsWith(httpsPrefix)) {
            return doHttpGet(url, param);
        }
        return doHttpsGet(url, param);
    }

    /**
     * https的post请求
     *
     * @param url
     * @param jsonStr
     * @return
     */
    public static String doPost(String url, String jsonStr) {
        return doPost(url, jsonStr, defaultCharSet, null);
    }

    /**
     * http的post请求
     *
     * @param url
     * @param jsonStr
     * @return
     */
    public static String doPost(String url, String jsonStr, String charset, Header[] headers) {
        if (StringUtils.isBlank(url)) {
            return "参数url为空";
        }
        if (!url.startsWith(httpsPrefix)) {
            return doHttpPost(url, jsonStr, charset, headers);
        }
        return doHttpsPost(url, jsonStr, charset, headers);
    }

    /**
     * https的post请求
     *
     * @param url
     * @param param
     * @return
     */
    public static String doPost(String url, Map<String, String> param) {
        return doPost(url, param, defaultCharSet, null);
    }

    /**
     * http的post请求
     *
     * @param url
     * @param param
     * @return
     */
    public static String doPost(String url, Map<String, String> param, String charset, Header[] headers) {
        if (StringUtils.isBlank(url)) {
            return "参数url为空";
        }
        if (!url.startsWith(httpsPrefix)) {
            return doHttpPost(url, param, charset, headers);
        }
        return doHttpsPost(url, param, charset, headers);
    }

    /**
     * http的get请求
     *
     * @param url
     * @param param
     * @return
     */
    public static String doHttpGet(String url, Map<String, String> param) {
        return doHttpGet(url, param, defaultCharSet, null);
    }

    /**
     * http的Get请求
     *
     * @param url
     * @param param
     * @return
     */
    public static String doHttpGet(String url, Map<String, String> param, String charset, Header[] headers) {
        CloseableHttpClient httpclient = null;
        CloseableHttpResponse response = null;

        try {
            httpclient = HttpClients.createDefault();
            if (param != null && !param.isEmpty()) {
                //参数集合
                List<NameValuePair> getParams = new ArrayList<NameValuePair>();
                for (Map.Entry<String, String> entry : param.entrySet()) {
                    getParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
                url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(getParams), charset);
            }
            //发送gey请求
            HttpGet httpGet = new HttpGet(url);
            if (headers != null) {
                httpGet.setHeaders(headers);
            }
            response = httpclient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return EntityUtils.toString(response.getEntity(), defaultCharSet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (httpclient != null) {
                try {
                    httpclient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * https的get请求
     *
     * @param url
     * @param param
     * @return
     */
    public static String doHttpsGet(String url, Map<String, String> param) {
        return doHttpsGet(url, param, defaultCharSet, null);
    }

    /**
     * https的Get请求
     *
     * @param url
     * @param param
     * @return
     */
    public static String doHttpsGet(String url, Map<String, String> param, String charset, Header[] headers) {
        try {
            httpClient = SSLClientUtils.createSSLClientDefault();

            if (param != null && !param.isEmpty()) {
                //参数集合
                List<NameValuePair> getParams = new ArrayList<NameValuePair>();
                for (Map.Entry<String, String> entry : param.entrySet()) {
                    getParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
                url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(getParams, charset), charset);
            }
            //发送gey请求
            HttpGet httpGet = new HttpGet(url);
            if (headers != null) {
                httpGet.setHeaders(headers);
            }
            response = httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return EntityUtils.toString(response.getEntity());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * http的post请求
     *
     * @param url
     * @param jsonStr
     * @return
     */
    public static String doHttpPost(String url, String jsonStr) {
        return doHttpPost(url, jsonStr, defaultCharSet, null);
    }

    /**
     * http的post请求（用于请求json格式的参数）
     *
     * @param url
     * @param jsonStr
     * @return
     */
    public static String doHttpPost(String url, String jsonStr, String charset, Header[] headers) {
        try {
            httpClient = HttpClients.createDefault();

            // 创建httpPost
            HttpPost httpPost = new HttpPost(url);
            if (headers == null) {
                httpPost.setHeader("Accept", "application/json");
            } else {
                httpPost.setHeaders(headers);
            }

            StringEntity entity = new StringEntity(jsonStr, charset);
//            entity.setContentType("application/json");
//            entity.setContentEncoding(new BasicHeader("Content-Type", "application/json"));
            httpPost.setEntity(entity);
            //发送post请求
            response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity responseEntity = response.getEntity();
                String jsonString = EntityUtils.toString(responseEntity, defaultCharSet);
                return jsonString;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * http的post请求(用于key-value格式的参数)
     *
     * @param url
     * @param param
     * @return
     */
    public static String doHttpPost(String url, Map<String, String> param) {
        return doHttpPost(url, param, defaultCharSet, null);
    }

    /**
     * http的post请求(用于key-value格式的参数)
     *
     * @param url
     * @param param
     * @return
     */
    public static String doHttpPost(String url, Map<String, String> param, String charset, Header[] headers) {
        try {
            //请求发起客户端
            httpClient = HttpClients.createDefault();
            //参数集合
            List<NameValuePair> postParams = new ArrayList<NameValuePair>();
            //遍历参数并添加到集合
            for (Map.Entry<String, String> entry : param.entrySet()) {
                postParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }

            //通过post方式访问
            HttpPost post = new HttpPost(url);
            HttpEntity paramEntity = new UrlEncodedFormEntity(postParams, charset);
            post.setEntity(paramEntity);
            if (headers != null) {
                post.setHeaders(headers);
            }
            response = httpClient.execute(post);
            StatusLine status = response.getStatusLine();
            int state = status.getStatusCode();
            if (state == HttpStatus.SC_OK) {
                HttpEntity valueEntity = response.getEntity();
                String content = EntityUtils.toString(valueEntity, defaultCharSet);
                //jsonObject = JSONObject.fromObject(content);
                return content;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * https的post请求
     *
     * @param url
     * @param jsonStr
     * @return
     */
    public static String doHttpsPost(String url, String jsonStr) {
        return doHttpsPost(url, jsonStr, defaultCharSet, null);
    }

    /**
     * https的post请求
     *
     * @param url
     * @param jsonStr
     * @return
     */
    public static String doHttpsPost(String url, String jsonStr, String charset, Header[] headers) {
        try {
            httpClient = SSLClientUtils.createSSLClientDefault();
            HttpPost httpPost = new HttpPost(url);
            if (headers == null) {
                httpPost.setHeader("Content-Type", "application/json");
            } else {
                httpPost.setHeaders(headers);
            }
            StringEntity se = new StringEntity(jsonStr);
//            se.setContentType("application/json");
//            se.setContentEncoding(new BasicHeader("Content-Type", "application/json"));
            httpPost.setEntity(se);

            response = httpClient.execute(httpPost);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    return EntityUtils.toString(resEntity, charset);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * https的post请求(用于key-value格式的参数)
     *
     * @param url
     * @param param
     * @return
     */
    public static String doHttpsPost(String url, Map<String, String> param) {
        return doHttpsPost(url, param, defaultCharSet, null);
    }

    /**
     * https的post请求(用于key-value格式的参数)
     *
     * @param url
     * @param param
     * @return
     */
    public static String doHttpsPost(String url, Map<String, String> param, String charset, Header[] headers) {
        try {
            httpClient = SSLClientUtils.createSSLClientDefault();
            //参数集合
            List<NameValuePair> postParams = new ArrayList<NameValuePair>();
            //遍历参数并添加到集合
            for (Map.Entry<String, String> entry : param.entrySet()) {
                postParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }

            //通过post方式访问
            HttpPost post = new HttpPost(url);
            HttpEntity paramEntity = new UrlEncodedFormEntity(postParams, charset);
            post.setEntity(paramEntity);
            if (headers != null) {
                post.setHeaders(headers);
            }
            response = httpClient.execute(post);
            StatusLine status = response.getStatusLine();
            int state = status.getStatusCode();
            if (state == HttpStatus.SC_OK) {
                HttpEntity valueEntity = response.getEntity();
                String content = EntityUtils.toString(valueEntity, defaultCharSet);
                //jsonObject = JSONObject.fromObject(content);
                return content;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }


}
