package socket;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tianfufeng on 2017/4/28.
 */
public class HttpClientUtil {

    private static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

    private static Object LOCAL_LOCK = new Object();
    private static final String CHAR_SET = "UTF-8";
    private static CloseableHttpClient httpClient = null;
    /**
     * 最大连接数400
     */
    private static int MAX_CONNECTION_NUM = 1200;
    /**
     * 单路由最大连接数80
     */
    private static int MAX_PER_ROUTE = 160;
    /**
     * 向服务端请求超时时间设置(单位:毫秒)
     */
    private static int SERVER_REQUEST_TIME_OUT = 3000;
    /**
     * 服务端响应超时时间设置(单位:毫秒)
     */
    private static int SERVER_RESPONSE_TIME_OUT = 3000;
    /**
     * 连接池管理对象
     */
    static PoolingHttpClientConnectionManager cm = null;


    private static final int MAX_TIMEOUT = 7000;
    private static PoolingHttpClientConnectionManager connMgr;
    private static RequestConfig requestConfig;

    static {
        // 设置连接池
        connMgr = new PoolingHttpClientConnectionManager();
        // 设置连接池大小
        connMgr.setMaxTotal(100);
        connMgr.setDefaultMaxPerRoute(connMgr.getMaxTotal());

        RequestConfig.Builder configBuilder = RequestConfig.custom();
        // 设置连接超时
        configBuilder.setConnectTimeout(MAX_TIMEOUT);
        // 设置读取超时
        configBuilder.setSocketTimeout(MAX_TIMEOUT);
        // 设置从连接池获取连接实例的超时
        configBuilder.setConnectionRequestTimeout(MAX_TIMEOUT);
        // 在提交请求之前 测试连接是否可用
        configBuilder.setStaleConnectionCheckEnabled(true);
        requestConfig = configBuilder.build();
    }

    /**
     * 发送 GET 请求（HTTP），不带输入数据
     *
     * @param url
     * @return
     */
    public static String doGet(String url) {
        return doGet(url, new HashMap<String, Object>());
    }

    /**
     * 发送 GET 请求（HTTP），K-V形式
     *
     * @param url
     * @param params
     * @return
     */
    public static String doGet(String url, Map<String, Object> params) {
        logger.debug("start HttpClientUtil[doGet] url:{} /r/n params:{}", url, JSONObject.toJSONString(params));
        String apiUrl = url;
        StringBuffer param = new StringBuffer();
        int i = 0;
        for (String key : params.keySet()) {
            if (i == 0)
                param.append("?");
            else
                param.append("&");
            param.append(key).append("=").append(params.get(key));
            i++;
        }
        apiUrl += param;
        String result = null;
        CloseableHttpClient httpclient = getHttpsClient();
        HttpGet httpGet = null;
        HttpResponse response = null;
        try {
            httpGet = new HttpGet(apiUrl);
            response = httpclient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();
                result = IOUtils.toString(instream, CHAR_SET);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpGet != null) {
                httpGet.releaseConnection();
            }
        }
        logger.debug("end HttpClientUtil[doGet] url:{} /r/n params:{} /r/n result:{}", new String[]{url, JSONObject.toJSONString(params), result});
        return result;
    }

    /**
     * 发送 POST 请求（HTTP），不带输入数据
     *
     * @param apiUrl
     * @return
     */
    public static String doPost(String apiUrl) {
        return doPost(apiUrl, new HashMap<String, Object>());
    }
    /**
     * 发送 POST 请求（HTTP）
     *
     * @param apiUrl API接口URL
     * @param params 参数map
     * @return
     */
    public static String doPost(String apiUrl, Map<String, String> head, String params) {
        //logger.info("start HttpClientUtil[doPost] url:{} ,head:{}, body:{}", apiUrl, head.toString(), JSONObject.toJSONString(params));
        CloseableHttpClient httpClient = getHttpsClient();
        String httpStr = null;
        HttpPost httpPost = new HttpPost(apiUrl);
        CloseableHttpResponse response = null;
        long startTime = System.currentTimeMillis();
        try {
            //添加请求头
            for (Map.Entry<String, String> entry : head.entrySet()) {
                httpPost.addHeader(entry.getKey(), entry.getValue());
            }
            //添加请求体
            StringEntity s = new StringEntity(params);
            s.setContentType("application/json");
            httpPost.setEntity(s);
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            httpStr = EntityUtils.toString(entity, CHAR_SET);
        } catch (Exception e) {
            logger.error("调用apiUrl,{} 异常，{}", apiUrl, e);
            logger.info("调用apiUrl ,{} 异常，{}", apiUrl, e);
        } finally {
            if (httpPost != null) {
                httpPost.releaseConnection();
            }
        }
        logger.info("耗时：{}ms,end HttpClientUtil[doPost] url:{} \r\n params:{} \r\n result:{}",System.currentTimeMillis() - startTime, apiUrl, params, httpStr);
        return httpStr;
    }
    /**
     * 发送 POST 请求（HTTP），K-V形式
     *
     * @param apiUrl API接口URL
     * @param params 参数map
     * @return
     */
    public static String doPost(String apiUrl, Map<String, String> head, Map<String, Object> params) {
        //logger.info("start HttpClientUtil[doPost] url:{} ,head:{}, body:{}", apiUrl, head.toString(), JSONObject.toJSONString(params));
        CloseableHttpClient httpClient = getHttpsClient();
        String httpStr = null;
        HttpPost httpPost = new HttpPost(apiUrl);
        CloseableHttpResponse response = null;
        long startTime = System.currentTimeMillis();
        try {

            //添加请求头
            for (Map.Entry<String, String> entry : head.entrySet()) {
                httpPost.addHeader(entry.getKey(), entry.getValue());
            }

            //添加请求体
            String tempStr = StringEscapeUtils.unescapeJavaScript(JSONObject.toJSONString(params));
            logger.info("tempStr:  "+tempStr);
            StringEntity s = new StringEntity(tempStr,"UTF-8");
            s.setContentType("application/json");
            httpPost.setEntity(s);
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            httpStr = EntityUtils.toString(entity, CHAR_SET);
        } catch (Exception e) {
            logger.error("调用apiUrl,{} 异常，{}", apiUrl, e);
            logger.info("调用apiUrl ,{} 异常，{}", apiUrl, e);
        } finally {
            if (httpPost != null) {
                httpPost.releaseConnection();
            }
        }
        logger.info("耗时：{}ms,end HttpClientUtil[doPost] url:{} \r\n params:{} \r\n result:{}",System.currentTimeMillis() - startTime, apiUrl, JSONObject.toJSONString(params), httpStr);
        return httpStr;
    }

    /**
     * 发送 POST 请求（HTTP），JSON形式
     *
     * @param apiUrl
     * @param json   json对象
     * @return
     */
    public static String doPost(String apiUrl, Object json) {
        logger.debug("start HttpClientUtil[doPost] url:{} \r\n params:{}", apiUrl, JSONObject.toJSONString(json));
        CloseableHttpClient httpClient = getHttpsClient();
        String httpStr = null;
        HttpPost httpPost = new HttpPost(apiUrl);
        CloseableHttpResponse response = null;

        try {
            StringEntity stringEntity = new StringEntity(json.toString(), CHAR_SET);//解决中文乱码问题
            stringEntity.setContentEncoding("UTF-8");
            stringEntity.setContentType("application/json");
            httpPost.setEntity(stringEntity);
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            httpStr = EntityUtils.toString(entity, CHAR_SET);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (httpPost != null) {
                httpPost.releaseConnection();
            }
        }
        logger.debug("end HttpClientUtil[doGet] url:{} \r\n params:{} /r/n result:{}", new String[]{apiUrl, JSONObject.toJSONString(json), httpStr});
        return httpStr;
    }

    /**
     * 发送 SSL POST 请求（HTTPS），K-V形式
     *
     * @param apiUrl API接口URL
     * @param params 参数map
     * @return 返回参数
     */

    public static String doPostSSL(String apiUrl, Map<String, Object> params) {
        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(createSSLConnSocketFactory()).setConnectionManager(connMgr).setDefaultRequestConfig(requestConfig).build();
        HttpPost httpPost = new HttpPost(apiUrl);
        CloseableHttpResponse response = null;
        String httpStr = null;

        try {
            httpPost.setConfig(requestConfig);
            List<NameValuePair> pairList = new ArrayList<NameValuePair>(params.size());
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                NameValuePair pair = new BasicNameValuePair(entry.getKey(), entry
                        .getValue().toString());
                pairList.add(pair);
            }
            httpPost.setEntity(new UrlEncodedFormEntity(pairList, Charset.forName(CHAR_SET)));
            response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                return null;
            }
            HttpEntity entity = response.getEntity();
            if (entity == null) {
                return null;
            }
            httpStr = EntityUtils.toString(entity, CHAR_SET);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (httpPost != null) {
                httpPost.releaseConnection();
            }
        }
        return httpStr;
    }

    /**
     * 发送 SSL POST 请求（HTTPS），JSON形式
     *
     * @param apiUrl API接口URL
     * @param json   JSON对象
     * @return
     */
    public static String doPostSSL(String apiUrl, Object json) {
        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(createSSLConnSocketFactory()).setConnectionManager(connMgr).setDefaultRequestConfig(requestConfig).build();
        HttpPost httpPost = new HttpPost(apiUrl);
        CloseableHttpResponse response = null;
        String httpStr = null;

        try {
            httpPost.setConfig(requestConfig);
            StringEntity stringEntity = new StringEntity(json.toString(), CHAR_SET);//解决中文乱码问题
            stringEntity.setContentEncoding(CHAR_SET);
            stringEntity.setContentType("application/json");
            httpPost.setEntity(stringEntity);
            response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                return null;
            }
            HttpEntity entity = response.getEntity();
            if (entity == null) {
                return null;
            }
            httpStr = EntityUtils.toString(entity, CHAR_SET);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (httpPost != null) {
                httpPost.releaseConnection();
            }
        }
        return httpStr;
    }

    /**
     * 创建SSL安全连接
     *
     * @return
     */
    private static SSLConnectionSocketFactory createSSLConnSocketFactory() {
        SSLConnectionSocketFactory sslsf = null;
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }

            }).build();
            sslsf = new SSLConnectionSocketFactory(sslContext, new X509HostnameVerifier() {

                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }

                @Override
                public void verify(String host, SSLSocket ssl) throws IOException {
                }

                @Override
                public void verify(String host, X509Certificate cert) throws SSLException {
                }

                @Override
                public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {
                }
            });
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return sslsf;
    }

    public static CloseableHttpClient getHttpsClient() {
        if (httpClient != null) {
            return httpClient;
        }
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(SERVER_REQUEST_TIME_OUT).setConnectTimeout(SERVER_RESPONSE_TIME_OUT).build();
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig).setConnectionManager(getPoolManager()).build();
        return httpClient;
    }


    private static PoolingHttpClientConnectionManager getPoolManager() {
        if (null == cm) {
            synchronized (LOCAL_LOCK) {
                if (null == cm) {
                    SSLContextBuilder sslContextBuilder = new SSLContextBuilder();
                    try {
                        sslContextBuilder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
                        SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(
                                sslContextBuilder.build());
                        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                                .register("https", socketFactory)
                                .register("http", new PlainConnectionSocketFactory())
                                .build();
                        cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
                        cm.setMaxTotal(MAX_CONNECTION_NUM);
                        cm.setDefaultMaxPerRoute(MAX_PER_ROUTE);
                    } catch (Exception e) {
                        logger.error("getPoolManager error:{}", e);
                    }
                }
            }
        }
        return cm;
    }

    public static void main(String[] args) {
        String text = doGet("http://10.32.40.100:9300/");
        System.out.println(text);
    }

}
