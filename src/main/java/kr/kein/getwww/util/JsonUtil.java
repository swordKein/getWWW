package kr.kein.getwww.util;

import java.net.URL;
import java.util.*;
import java.io.IOException;
import java.lang.reflect.*;

import org.json.simple.JSONObject;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sun.codemodel.internal.JCodeModel;

public class JsonUtil {
	public static int jsonOutput(JSONObject jobj) {
		int resultCode = 0;
		
		 try {
			 
			
		 } catch (Exception e) { e.printStackTrace(); }	 	
		 return resultCode;
	}
	
	
	/* 주어진 HashSet<String> 내의 모든 String을  공백으로 분리된 1개의 String으로 리턴
    *
    */
   public static String convertSetMapToString(HashMap<Integer,Object> reqSet, String tagStr, String sharp) {
       String result = "";
       System.out.println("### reqSet :: "+reqSet);
       
       if (reqSet != null) {
       	String tmp = "";
           for (Integer thisValue : reqSet.keySet()) {
        	   System.out.println("### thisValue :: "+thisValue);
           	for (String str : (HashSet<String>) reqSet.get(thisValue)) {
           		System.out.println("### (HashSet<String>) reqSet.get(thisValue) :: "+(HashSet<String>) reqSet.get(thisValue));
           		if(!"".equals(str)) {
	            		if ("".equals(tmp)) { result = sharp + str.trim(); }
	            		else { result = result.trim() + tagStr + sharp + str.trim(); }
	            		tmp = str;
           		}
           	}
           }
       }
       result = result.trim();
       return result;
   }
   
   
   public String getGsonSchema(String data) throws Exception {
	   String result = "";
	   Gson gson = new GsonBuilder().create();
	   //result = gson.toJson(data.getMerchant().getStakeholder_list());
	   result = gson.toJson(data);
	   
	   return result;
   }
   

   public static JsonNode getJsonSchema (String jsonData) throws Exception {
	   ObjectMapper mapper=new ObjectMapper();
	   
	   JsonNode rootNode = mapper.readTree(jsonData);
	   JsonParser jp=mapper.getJsonFactory().createJsonParser(jsonData);
	   
	   rootNode=mapper.readTree(jp);
	   
	   ObjectNode onode = (ObjectNode) rootNode;
	   
	   //String wv = mapper.writeValue(rootNode);
	   
	   //JsonSchema jsc = mapper.generateJsonSchema(SimpleBean.class);
	   
	   return rootNode;
   }

}