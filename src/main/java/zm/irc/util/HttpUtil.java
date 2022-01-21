package zm.irc.util;




import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;

import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import zm.irc.RunIrcClientBackEnd;

import java.util.Map;

public class HttpUtil {



    private static final Logger log = Logger.getLogger(RunIrcClientBackEnd.class);


    public static String getJson(String url, Map<String, String> headers) {
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);

        if(headers!= null){
            for (String key : headers.keySet()) {
                httpGet.setHeader(key, headers.get(key));
            }
        }

        try {
            log.debug(url + "[GET]>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

            HttpResponse response = httpClient.execute(httpGet);
            String httpEntityContent = EntityUtils.toString(response.getEntity());
            log.debug(url + "[GET]<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
            log.debug(httpEntityContent);
            return httpEntityContent;
        } catch (Exception e) {
            log.error("error",e);
            return null;
        } finally {
            httpGet.abort();
        }
    }

    public static String postJson(String url, String data, Map<String, String> headers) {

        HttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-Type", "application/json;charset=utf-8");
        if(headers!= null){
            for (String key : headers.keySet()) {
                httpPost.setHeader(key, headers.get(key));
            }
        }
        try {
            log.debug(url + "[POST]>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            log.debug(data);
            httpPost.setEntity(new StringEntity(data, "UTF-8"));
            HttpResponse response = httpClient.execute(httpPost);
            String httpEntityContent = EntityUtils.toString(response.getEntity());
            log.debug(url + "[POST]<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
            log.debug(httpEntityContent);
            return httpEntityContent;
        } catch (Exception e) {
            log.error("请求失败 url:" + url + ", data:" + data,e);
            return null;
        } finally {
            httpPost.abort();
        }
    }
}
