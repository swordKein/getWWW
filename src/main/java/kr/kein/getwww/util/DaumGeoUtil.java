package kr.kein.getwww.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import kr.kein.getwww.getwww;
import kr.kein.getwww.vo.GeoData;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class DaumGeoUtil {
    public static GeoData getDaumGeoByAddr(String addr) {
        GeoData result = new GeoData();
        String reqHost = "https://apis.daum.net";
        String agreeCnt = "";

        int reqPort = 0;
        String reqPath = "/local/geo/addr2coord?apikey=747dfd9d3253aeff396eed7f9ed2abd6&output=json";
        addr = CommonUtils.getURLEncode(addr);
        reqPath = reqPath + "&q=" + addr;

        String rsbody = "";
        try {
            rsbody = getwww.getHtmlBody(reqHost, reqPort, reqPath, null, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonArray fArray = new JsonArray();
        JsonParser parser = new JsonParser();
        try {
            System.out.println("#daum geo::"+rsbody);
//            JsonObject fObj = (JsonObject)parser.parse(rsbody);
  //          JsonObject rObj = fObj.getAsJsonObject("lat");
    //        System.out.println("#daum geo data::"+rObj.toString());
      //      agreeCnt = rObj.get("likeCount").toString();
            //System.out.println(">>"+agreeCnt);
        } catch (Exception pe) {
            pe.printStackTrace();
        }

        return result;
    }
}
