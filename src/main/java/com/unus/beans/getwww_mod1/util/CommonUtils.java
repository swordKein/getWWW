package com.unus.beans.getwww_mod1.util;

import java.io.File;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.mozilla.universalchardet.UniversalDetector;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;


public class CommonUtils {
	public static String findFileEncoding(File file) {
  	  String encoding = "";
  	  try {
  		System.out.println(" start UniversalDetector ! ");
	    	   UniversalDetector detector = new UniversalDetector(null);
	    	   System.out.println(" UniversalDetector :: "+detector.getClass().toString() );
	    	   
	    	   byte[] buf = new byte[4096];
	    	   java.io.FileInputStream fis = new java.io.FileInputStream(file);
	
	    	   int nread;
	    	   while ((nread = fis.read(buf)) > 0 && !detector.isDone()) {
	    	   detector.handleData(buf, 0, nread);
	    	   }
	    	  
	    	   detector.dataEnd();
	    	  
	    	   encoding = detector.getDetectedCharset();
	    	   if (!"".equals(encoding)) {
	    		   System.out.println("Detected encoding = " + encoding);
	    	   } else {
	    		   System.out.println("No encoding detected.");
	    	   }
	
	    	   detector.reset();
  	  } catch (Exception e) { e.printStackTrace(); 
  	  System.out.println(" findFileEncoding error "+e.getCause()); }   
  	  
  	   return encoding;
  }

	public static String getUuid() {
		String uuid = UUID.randomUUID().toString();  uuid = uuid.replace("-", "");
		return uuid;
	}
	
	public static String addQt(String txt) {
		String qt = "\"";
		return qt + txt + qt;
	}
	
	public static String getMapListFromJsonArray(HashMap<String,Object> bodyMap) {
		String result = "";
		Map[] maps = null;
		System.out.println("### bodyMap headers :: "+bodyMap.get("header") );

		Gson gson = new Gson();
//		String header = bodyMap.get("header").toString();
//		JSONArray arr = gson.fromJson(header, JSONArray.class);
//
//		System.out.println("### arr result:: "+arr.toString() );
//		
//		Map[] map1 = gson.fromJson(arr.toString(), Map[].class);
		String header = bodyMap.get("header").toString();     
    	Map[] headerArr = gson.fromJson(header, Map[].class);

		//JSONArray datasArr = gson.fromJson(datas, JSONArray.class);
		//System.out.println("### bodyMap datasArr :: "+  datasArr.toString());
		
		//datas = "[[a,1,2],[bbbsdfdsf,3,4]]";
		//datas.replace("<", "").replace(">", "");
    	
    	
    	String datas0 = "{\"header\":[{\"title\":\"filename\"},{\"title\":\"org_txt\"},{\"title\":\"recv_date\"}],\"datas\":[[\"20150824101850-4093_-01027765585_____-91092689__-I.txt\",\"안녕하십니 ek\",\"20150824\"],[\"234\",\"abkdfj\",\"23432\"]]}";
    	JsonParser jsonParser = new JsonParser();
    	JsonElement element = jsonParser.parse(datas0);
    	JsonObject jObj = element.getAsJsonObject();
    	System.out.println("### bodyMap jObj :: "+ jObj.get("datas"));
    	
    	JsonArray datasArr = jObj.get("datas").getAsJsonArray();
//    	for (JsonElement str : datasArr) {
//    		JsonArray sr = str.getAsJsonArray();
//    		for (JsonElement s : sr) {
//    			System.out.println("### bodyMap datasArr.get(0) :: "+ s);
//    		}
//    	}

		List<HashMap<String, Object>> resMap = new ArrayList<HashMap<String, Object>>();
			
		for (int d=0; d < datasArr.size(); d++) {
			HashMap<String, Object> newMap = new HashMap<String, Object>();
			for(int h=0; h< headerArr.length; h++) {				
				String H = headerArr[h].get("title").toString();
				JsonArray dArr = datasArr.get(d).getAsJsonArray();				
				//System.out.println("### "+h+" :: "+H+ " ====>  " + dArr.get(h)); 
				
				newMap.put(H, dArr.get(h));				
			}
			resMap.add(newMap);
		}
		//System.out.println("### map1 result:: "+resMap.toString() );
		
		maps = new Map[resMap.size()];
		maps = resMap.toArray(maps);
		
		System.out.println("### maps result:: "+maps );
		
		/*
		ArrayList<String> headerArr = (ArrayList<String>) bodyMap.get("header");
		ArrayList<String> bodyArr = (ArrayList<String>) bodyMap.get("datas");
		        	
		List<HashMap<String,Object>> tempMap = new ArrayList<HashMap<String,Object>>();
		for(int i=0; i < headerArr.size(); i++) {
			System.out.println("### bodyMap headerArr :: "+i+" >> "+bodyMap.get("header") );
			for (int d=0; d < bodyArr.size(); d++) {
	    		String[] bArr = bodyArr.get(d).toString().split(",");
	    		HashMap<String,Object> newMap = new HashMap<String,Object>();
	    		
	    		for (int j=0; j < bArr.length; j++) {
	    			newMap.put(headerArr.get(i).toString(), bArr[i]);
	    		}
	
	    		tempMap.add(newMap);
			}
		}
		*/
	
		//maps = new Map[tempMap.size()];
		//maps = tempMap.toArray(maps);
		
		//result = maps.toString();
		
		return result;
	}
}