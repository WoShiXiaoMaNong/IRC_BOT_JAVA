package zm.irc.dao;

import org.apache.log4j.Logger;
import zm.irc.util.HttpUtil;
import zm.irc.util.Md5Util;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

public class BaiduTransDao {
    private static final Logger log = Logger.getLogger(BaiduTransDao.class);
    public static String appId = "";
    public static String secretKey = "";
    public static String baseUrl = "http://api.fanyi.baidu.com/api/trans/vip/translate?";


    public String enToZh(String enMsg){
        String salt = "1435660282";
        String sign = this.sign(enMsg,salt);
        String query = String.format("q=%s&from=en&to=zh&appid=%s&salt=%s&sign=%s",
                enMsg,appId,salt,sign);
        return sendRequest(query);
    }

    public static void main(String[] args) {
        BaiduTransDao dd  = new BaiduTransDao();
        String s = dd.enToZh("HelloWorld!");
        System.out.println(s);

    }


    public String zhToEn(String zhMsg){
        String salt = "1435660288";
        String sign = this.sign(zhMsg,salt);
        String query = String.format("q=%s&from=zh&to=en&appid=%s&salt=%s&sign=%s",
                zhMsg,appId,salt,sign);
        return sendRequest(query);
    }


    private String sendRequest(String query){
        try {
            String url = baseUrl + URLEncoder.encode(query, StandardCharsets.UTF_8);
            url = baseUrl + query;
            return HttpUtil.getJson(url, Collections.emptyMap());
        }catch (Exception e){
            log.error("Send request error!",e);
        }
        return null;
    }


    private String sign(String queryMsg,String salt){

        String toBeSignedStr = appId +  queryMsg + salt + secretKey;

        return Md5Util.toMd5(toBeSignedStr);
    }


}
