package kr.kein.getwww.sns;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import kr.kein.getwww.*;
import kr.kein.getwww.sns.*;
import kr.kein.getwww.util.*;

import org.json.simple.JSONValue;

import facebook4j.*;
import facebook4j.auth.*;
import facebook4j.conf.*;
import facebook4j.internal.http.*;
import facebook4j.internal.org.json.*;

public class facebookImpl2 {
	public static final String siteCode = "fb";
	public static final String dataType = "poli";
	
	public static final String appId = "484180101721290";
	public static final String appSecret = "54fae87d7600e150d4b6fd978fa8c3b7";
	public static final String oldToken = "CAACEdEose0cBAOJehw20T8HPgnOtzeSRZADAWV2D37qpvtvrA8tBZBym3G2q5aa5TsZAcXPjAxNpdCvGQHviUOof0Ka9jpZBiTNqUZBZCR45vNThJbQV0xkbhpC4shZAOsGgksBHbVnBnDD4MddDb29qreGPnZAR0GKWBQDduEBgtu6RphZC6oToTZARqECx0N3xwKcue5mFWjkmJaX3S6ZCjUQSUHDPY2E5xAZD";
	
	public static final String permissions = "email,publish_stream";
	
	public static void getFacebookPosts(String searchID, int pageno, OutputStreamWriter outFwriter, OutputStreamWriter outFwriterReply) throws Exception{
		// Executing "me" and "me/friends?limit=50" endpoints
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		  .setOAuthAppId(appId)
		  .setOAuthAppSecret(appSecret)
		  .setOAuthAccessToken(oldToken)
		  .setGZIPEnabled(true)
		  .setDebugEnabled(true)
		  //.setHttpConnectionTimeout(600)
		  //.setHttpReadTimeout(590)
		  .setHttpRetryCount(3)
		  .setHttpRetryIntervalSeconds(10)
		  .setJSONStoreEnabled(true)
		  .setOAuthPermissions(permissions);
		FacebookFactory ff = new FacebookFactory(cb.build());
		Facebook facebook = ff.getInstance();
		
		runFacebook(searchID, facebook, true, "", outFwriter, outFwriterReply);
	}
	
	public static void runFacebook(String searchID, Facebook facebook, boolean isStart, String nextPage, OutputStreamWriter outFwriter, OutputStreamWriter outFwriterReply) throws FacebookException, JSONException, IOException {
		/** 시작할 때는 검색 페이지 URL 고정, 그 뒤에는 nextPage를 처리 -> 재귀호출 **/
		if (isStart) { 
			nextPage = "266715880026269/posts?fields=id,updated_time,name,caption,description,message,likes&limit=20&offset=1";
			//nextPage = searchID+"/posts?fields=id,updated_time,name,caption,description,message,likes&limit=20&offset=1";
		}
				
		/** batch process 실행 - 페이스북 검색 **/
		List<BatchResponse>results = getFacebookResults(facebook, nextPage, outFwriter, outFwriterReply);
		//System.out.println("Origin Results :: "+results.get(0).toString());		
		List<String> resultStr = getFacebookContentList(facebook, results, outFwriter, outFwriterReply);		
		resultStr = getFacebookContentList(facebook, results, outFwriter, outFwriterReply);		
		//System.out.println("Results :: "+resultStr.toString());
		
		/** 다음 페이지 존재할 경우 재귀호출 **/
		nextPage = "";
		try { 
			JSONObject joResult = results.get(0).asJSONObject();
			JSONObject paging = (JSONObject) joResult.get("paging");
			nextPage = paging.get("next").toString();
			
			System.out.println("pageing ::: "+nextPage);
			
			if (!"".equals(nextPage)) { 
				nextPage = nextPage.replace("https://graph.facebook.com/v2.0/","");
				nextPage = nextPage.replace("https://graph.facebook.com/v2.2/","");
				runFacebook(searchID, facebook, false, nextPage, outFwriter, outFwriterReply);
			}
		} catch (Exception ge) { }
		/* -- **/
	}
	
	public static List<BatchResponse> getFacebookResults(Facebook facebook, String nextPage, OutputStreamWriter outFwriter, OutputStreamWriter outFwriterReply) throws FacebookException, JSONException {
		BatchRequests<BatchRequest> batch = new BatchRequests<BatchRequest>();
		batch.add(new BatchRequest(RequestMethod.GET, nextPage));
		//batch.add(new BatchRequest(RequestMethod.GET, "search/?q=유정복&type=post"));
		
		List<BatchResponse> results = facebook.executeBatch(batch);				
		return results;
	}
	
	public static List<String> getFacebookContentList(Facebook facebook, List<BatchResponse> results, OutputStreamWriter outFwriter, OutputStreamWriter outFwriterReply) throws FacebookException, JSONException, IOException {
		List<String> resultStr = new ArrayList<String>();
		
		JSONObject joResult = results.get(0).asJSONObject();		
		JSONArray item = (JSONArray) joResult.get("data");
		String tag = "\t";
		for(int i = 0 ; i < item.length(); i++) {
			JSONObject obj1 = (JSONObject)item.get(i);	
			String uuid = UUID.randomUUID().toString();  uuid = uuid.replace("-", "");
			/** facebook data parsing **/
			String c_id = obj1.get("id").toString().replace("\n"," ");
			String c_date = obj1.get("updated_time").toString();		
			String cc_date = DateUtils.getChangeDate(c_date);
			String c_message = "";
				try {	c_message = obj1.get("message").toString().replace("\n", " ").replace("\r"," ");		} catch (Exception ge) { }
			String c_desc = "";
			if ("".equals(c_message)) {
				try {	c_desc = obj1.get("description").toString().replace("\n", " ").replace("\r"," ") ;	} catch (Exception ge) { }
				if (!"".equals(c_desc)) { c_message = c_desc; }
			}
			
			/** like 좋아요 갯수 취득 **/
			int c_likeCnt = getFacebookPostLikesCount(facebook, c_id);
			
			/** 댓글 리스트 취득 **/
			getFacebookPostComments(facebook, c_id, outFwriterReply);
			/** --**/
			
			String outStr = "";
			if (!"".equals(c_message)) { 
				outStr = siteCode  +tag 
						+ uuid  +tag 
						+ dataType  +tag 
						+ cc_date  +tag
						+ c_id  +tag 
						+ c_message  +tag
						+ String.valueOf(c_likeCnt); 
			}
			//System.out.println("DATA "+i+"'s :: "+ outStr);
			
			//JSONObject c_likeAll = (JSONObject) obj1.get("likes");
			//JSONArray c_likes = (JSONArray) c_likeAll.get("data");
			//JSONArray c_likes = c_likeAll.getJSONArray("name");
			//System.out.println("LIKE DATA "+i+"'s  length="+c_likes.length()+ ":: "+ c_likes.toString());
			//outStr = "Data "+i+"'s "+outStr;
			if (!"".equals(c_message)) { 
				
			if("file".equals(getwww.runningMode)) {					
				// #TODO .write 실제 파일에 쓰기
				outFwriter.write(outStr+"\n");
			} else if("console".equals(getwww.runningMode)) {
				System.out.println(outStr+"\n");
			}
			
			
			resultStr.add(outStr);
			
			}
			//c_likeAll = null;
			//c_likes = null;
			obj1 = null;
		}
		
		return resultStr;
	}
	
	public static int getFacebookPostLikesCount(Facebook facebook, String postId) throws FacebookException, JSONException {
		int cnt = 0;
		try {
			BatchRequests<BatchRequest> batch = new BatchRequests<BatchRequest>();
			String nextPage = postId + "/likes?limit=1000000";
			batch.add(new BatchRequest(RequestMethod.GET, nextPage));
			//batch.add(new BatchRequest(RequestMethod.GET, "search/?q=유정복&type=post"));
			
			List<BatchResponse> results = facebook.executeBatch(batch);		
			JSONObject joResult = results.get(0).asJSONObject();
			//System.out.println("KKK :: "+joResult.toString());
			//JSONObject c_likeAll = (JSONObject) joResult.get("likes");
			JSONArray c_likes = (JSONArray) joResult.get("data");
			cnt = c_likes.length();
		} catch (Exception e) { 
			e.printStackTrace();
		}
		return cnt;
	}
	
	public static String getFacebookPostComments(Facebook facebook, String postId, OutputStreamWriter outFwriterReply) throws FacebookException, JSONException {
		int cnt = 0;
		String c_commentsStr = "";
		
		try {
			BatchRequests<BatchRequest> batch = new BatchRequests<BatchRequest>();
			String nextPage = postId + "/comments?limit=1000000";
			batch.add(new BatchRequest(RequestMethod.GET, nextPage));
			//batch.add(new BatchRequest(RequestMethod.GET, "search/?q=유정복&type=post"));
			
			List<BatchResponse> results = facebook.executeBatch(batch);		
			JSONObject joResult = results.get(0).asJSONObject();
			//System.out.println("KKK :: "+joResult.toString());
			//JSONObject c_likeAll = (JSONObject) joResult.get("likes");
			JSONArray c_comments = (JSONArray) joResult.get("data");
			c_commentsStr = getFacebookPostCommentsContent(c_comments, postId);

			//System.out.println("comments LIST ::"+c_comments);
			if (!"".equals(c_commentsStr)) {
				
			if("file".equals(getwww.runningMode)) {					
				// #TODO .write 실제 파일에 쓰기
				outFwriterReply.write(c_commentsStr);
			} else if("console".equals(getwww.runningMode)) {
				System.out.println(c_commentsStr+"\n");
			}
			
			}
		} catch (Exception e) { 
			e.printStackTrace();
		}
		return c_commentsStr;
	}
	
	public static String getFacebookPostCommentsContent(JSONArray itemArray, String o_id) throws JSONException {
		String c_commentsStr = "";
		
		if (itemArray != null && itemArray.length() > 0) {
			String tag = "\t";
			for(int i = 0 ; i < itemArray.length(); i++) {
				JSONObject obj1 = (JSONObject) itemArray.get(i);	
				String uuid = UUID.randomUUID().toString();  uuid = uuid.replace("-", "");
				String dataTypeReply = "fb_reply";
				/** facebook data parsing **/
				String c_id = obj1.get("id").toString().replace("\n"," ");
				String c_date = obj1.get("created_time").toString();		
				String cc_date = DateUtils.getChangeDate(c_date);
				String c_message = "";
					try {	c_message = obj1.get("message").toString().replace("\n", " ").replace("\r"," ");		} catch (Exception ge) { }
								
				/** like 좋아요 갯수 취득 **/
				String c_likeCnt = obj1.get("like_count").toString();
				
				/** 댓글 작성자 정보 취득 **/
				JSONObject obj2 = (JSONObject) obj1.get("from");
				String c_authorId = obj2.get("id").toString();
				String c_authorName = obj2.get("name").toString();	
				
				String outStr = "";
				if (!"".equals(c_message)) { 
					outStr = siteCode  +tag 
							+ uuid  +tag 
							+ dataTypeReply  +tag 
							+ cc_date  +tag
							+ o_id  +tag
							+ c_id  +tag 
							+ c_message  +tag
							+ c_authorId  +tag
							+ c_authorName  +tag
							+ c_likeCnt +"\n"; 
					
					c_commentsStr = c_commentsStr + outStr;
				}
				
				//System.out.print(" subComments :: "+outStr);
				obj1 = null;
				obj2 = null;
			}		
		}
		return c_commentsStr;
	}
}