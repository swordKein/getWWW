package test.com.unus.beans.getwww_mod1.util;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import com.unus.beans.getwww_mod1.util.DateUtils;
import com.unus.beans.getwww_mod1.util.JsonUtil;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class JsonUtilTest {

    @Test
    public void testGetRemoveStringBySet() throws Exception {
    	String thisTime = DateUtils.getLocalDateTime();
    	System.out.println(" result txt :: " + thisTime);
    }
    
    @Test
    public void testConvertSetMapToString() throws Exception {

    	HashSet<String> newSet = new HashSet<String>();
    	newSet.add("OOC");
    	HashMap<Integer, Object> newMap = new HashMap<Integer, Object>();
    	newMap.put(9999999, newSet);
    	
    	String result = JsonUtil.convertSetMapToString(newMap, "," , "#");
    	System.out.println(" result txt :: " + result);
    }
    
    @Test
    public void testReplace() throws Exception {
    	String str = "\"test\"";
    	str = str.trim().replace("\"", "").replace("\\", "");
    	System.out.println("result : "+str);
    }
    
    @Test
    public void testGsonSchema() throws Exception {
    	String data = "{\"COLLECTORID\":10, \"ROUTEID\":9, \"REG_DATE\":20150824, \"SUCCESS_COUNT\":2216, \"FAIL_COUNT\":0}";
    	Gson gson = new Gson();
    	JsonObject data1 = new JsonParser().parse(data).getAsJsonObject();
    	//JsonNode rootNode = JsonUtil.getJsonSchema(data);

    	JsonParser jsonParser = new JsonParser();
    	JsonElement element = jsonParser.parse(data);
    	JsonObject rootNode = element.getAsJsonObject();
    	//JsonNode rootNodeTree = gson.toJsonTree(data);
    
    	for (Entry<String, JsonElement> node : rootNode.entrySet()) {
    	  //  JsonNode node=(JsonNode)i.next();
    	    System.out.println("key : "+node.getKey());
    	}
    	
    	//for (JsonNode node : rootNodeTree.) {
    	//	System.out.println("key : "+rootNodeTree);
    	//}
    	
    	//System.out.println("result : "+JsonUtil.getJsonSchema(data));
    }
    
    @Test
    public void testGsonConvert() throws Exception {
    	String data = "{IDX=65, COUNS_ID=0.0, CONTENT=만약, RECV_DATE=2015-10-17 15:16:40.0}";
    	Gson gson = new Gson();
    	JsonObject data1 = new JsonParser().parse(data).getAsJsonObject();
    	//JsonNode rootNode = JsonUtil.getJsonSchema(data);

    	JsonParser jsonParser = new JsonParser();
    	JsonElement element = jsonParser.parse(data);
    	JsonObject rootNode = element.getAsJsonObject();
    	//JsonNode rootNodeTree = gson.toJsonTree(data);
    
    	for (Entry<String, JsonElement> node : rootNode.entrySet()) {
    	  //  JsonNode node=(JsonNode)i.next();
    	    System.out.println("key : "+node.getKey());
    	}
    	
    	//for (JsonNode node : rootNodeTree.) {
    	//	System.out.println("key : "+rootNodeTree);
    	//}
    	
    	//System.out.println("result : "+JsonUtil.getJsonSchema(data));
    }
}