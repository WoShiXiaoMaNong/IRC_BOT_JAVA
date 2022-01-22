package zm.irc.dao;

import org.apache.log4j.Logger;
import zm.irc.util.HttpUtil;
import zm.irc.util.Md5Util;
import com.alibaba.fastjson.JSON;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

public class BaiduTransDao {
    private static final Logger log = Logger.getLogger(BaiduTransDao.class);
    private static String appId = "";
    private static String secretKey = "";
    private static String baseUrl = "http://api.fanyi.baidu.com/api/trans/vip/translate?";

    public static BaiduTransDao dao = new BaiduTransDao();
    private BaiduTransDao(){}

    public static void init(String appId,String secretKey){
        BaiduTransDao.appId = appId;
        BaiduTransDao.secretKey = secretKey;

    }

    public String enToZh(String enMsg){
        String salt = "1435660282";
        String sign = this.sign(enMsg,salt);
        String query = String.format("q=%s&from=en&to=zh&appid=%s&salt=%s&sign=%s",
                URLEncoder.encode(enMsg, StandardCharsets.UTF_8),appId,salt,sign);
        return sendRequest(query);
    }

    public String zhToEn(String zhMsg){
        String salt = "1435660288";
        String sign = this.sign(zhMsg,salt);
        String query = String.format("q=%s&from=zh&to=en&appid=%s&salt=%s&sign=%s",
                URLEncoder.encode(zhMsg, StandardCharsets.UTF_8),appId,salt,sign);
        return sendRequest(query);
    }

    public String transTo(String msg,String distLanguage){
        String salt = "1435660288";
        String sign = this.sign(msg,salt);
        String query = String.format("q=%s&from=auto&to=%s&appid=%s&salt=%s&sign=%s",
                URLEncoder.encode(msg, StandardCharsets.UTF_8),distLanguage,appId,salt,sign);
        return sendRequest(query);
    }


    private String sendRequest(String query){
        try {
            String url = baseUrl + URLEncoder.encode(query, StandardCharsets.UTF_8);
            url = baseUrl + query;
            String jsonStr = HttpUtil.getJson(url, Collections.emptyMap());

            BaiduResult result = JSON.parseObject(jsonStr,BaiduResult.class);
            return result.getTrans_result().get("dst");
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

class BaiduResult{
    String from;
    String to;
    Map<String,String> trans_result;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Map<String, String> getTrans_result() {
        return trans_result;
    }

    public void setTrans_result(Map<String, String> trans_result) {
        this.trans_result = trans_result;
    }
}
