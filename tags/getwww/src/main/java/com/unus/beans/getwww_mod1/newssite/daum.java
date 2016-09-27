package main.java.com.unus.beans.getwww_mod1.newssite;

import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import main.java.com.unus.beans.getwww_mod1.getwww;
import main.java.com.unus.beans.getwww_mod1.util.CommonUtils;
import main.java.com.unus.beans.getwww_mod1.util.DateUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class daum extends Thread {
	public static int searchNum = 0;
	public static int tryCount = 0;
	public static String prefix = "daum";
	//public static String newsCate = "poli";
	public static String reqHost = "http://media.daum.net";
	public static int reqPort = 80;
	//public static String reqPath = "/api/service/news/list/category/1002.jsonp";
	public static final String reqPath = "/api/service/news/list/category/";
	public static final String tab = "\t | ";
	
	public static String getNewsDaum(int pageno, String dtDate, OutputStreamWriter outFwriter, OutputStreamWriter outFwriterReply) throws Exception{
		//System.out.println("* Start =================================================================================================");
		

		//String query = "김무성";
		//String query = "";
		//String reqQuery = URLEncoder.encode(query, "UTF-8");
		int getSum = 0;		
		if ( pageno == 1 ) { getSum = 0; }
		
		String reqPageno = Integer.toString(pageno);
		Map<String,String> reqparam = new HashMap<String,String>();
		//reqparam.put("reqQuery", reqQuery);
		reqparam.put("reqPageKey","page");
		reqparam.put("reqPageno", reqPageno);
		
		reqparam.put("reqDt","regdate");
		reqparam.put("reqDtDate", dtDate);
		

		String newsCateUrl = "";
		switch(getwww.newsCate) {		
			case "poli" :
				newsCateUrl = "1002";
			break;
			case "soci" :
				newsCateUrl = "1001";
			break;
			case "glob" :
				newsCateUrl = "1007";
			break;
			case "econ" :
				newsCateUrl = "1006";
			break;
			default:
				newsCateUrl = "1002";
			break;
		}
		String reqPathIn = reqPath + newsCateUrl + ".jsonp";
								
		String rsbody = getwww.getHtmlBody(reqHost, reqPort, reqPathIn, reqparam, "UTF-8");
		/**
		 * 본문읽기
		 * http://media.daum.net/api/service/news/view.jsonp?newsid=20150527224024050
		 * 
		 * articleId 구하기
		 * "uccId":"53277717","
		 * 
		 * 댓글 리스트
		 * http://news.rhea.media.daum.net/rhea/do/social/json/commentList?bbsId=news&articleId=53277717&allComment=T&cSortKey=depth&cPageIndex=1&cSearchKey=&cSearchValue=&callback=rheaJsonp0
		 * 
		 */
		
		//rsbody = convertEuckrToUtf8(rsbody);
		System.out.println("Searching news-URL :: "+reqPathIn+" ["+dtDate+"]  "+ "["+reqPageno+"] \n");
		//System.out.println("Thead info :: "+d1.activeCount()+" >> "+d1.currentThread());
		
		String prcDate = parseJsonDaum(rsbody, outFwriter, outFwriterReply);
		prcDate = prcDate.replace("-", "");
		//return prcDate;
		return prcDate;
		//searchNum += getSum;
		//System.out.println(getDate+"'th News "+ pageno + " PAGE getting count : "+getSum);
	}
	
	public static String getAgreeDaum(String newsId) throws Exception{
		String reqHost = "http://like.daum.net";
		String agreeCnt = "";
		
		int reqPort = 80;
		String reqPath = "/item/news/";
		reqPath = reqPath + newsId + ".json";		
				
		String rsbody = getwww.getHtmlBody(reqHost, reqPort, reqPath, null, "UTF-8");
		JsonArray fArray = new JsonArray();
		JsonParser parser = new JsonParser();
		try {			
            JsonObject fObj = (JsonObject)parser.parse(rsbody);
            JsonObject rObj = fObj.getAsJsonObject("data");
            
            agreeCnt = rObj.get("likeCount").toString();
            //System.out.println(">>"+agreeCnt);
		} catch (Exception pe) {
			pe.printStackTrace();
	    }		
		
		return agreeCnt;
	}
	
	public static String parseJsonDaum(String rsbody, OutputStreamWriter outFwriter, OutputStreamWriter outFwriterReply) throws Exception {
		JsonArray fArray = new JsonArray();
		JsonParser parser = new JsonParser();
		try {			
            JsonObject fObj = (JsonObject)parser.parse(rsbody);
            fArray = fObj.getAsJsonArray("simpleNews");   
		} catch (Exception pe) {
			pe.printStackTrace();
	    }		

		ArrayList<String> rIDs = new ArrayList<String>();
		ArrayList<String> rTitles = new ArrayList<String>();
		ArrayList<String> rLinks = new ArrayList<String>();
		ArrayList<String> rEm = new ArrayList<String>();
		ArrayList<String> rAuthors = new ArrayList<String>();
		ArrayList<String> rComp = new ArrayList<String>();
		
		String eDate = "";
		String newsUrlTag = "";
		if (fArray != null) {
		for (int i=0; i<fArray.size(); i++) {
			rTitles.add(fArray.get(i).getAsJsonObject().get("title").toString());
			//System.out.println(">>"+fArray.get(i).getAsJsonObject().get("title"));
			String newsId = fArray.get(i).getAsJsonObject().get("newsId").toString(); 
			newsId = newsId.replace("\"", "");
			rIDs.add(newsId);
			
			switch(getwww.newsCate) {		
				case "poli" :
					newsUrlTag = "politics";
				break;
				case "soci" :
					newsUrlTag = "society";
				break;
				case "glob" :
					newsUrlTag = "foreign";
				break;
				case "econ" :
					newsUrlTag = "economic";
				break;
				default:
					newsUrlTag = "politics";
				break;
			}
			rLinks.add("http://media.daum.net/"+newsUrlTag+"/all/newsview?newsid="+newsId);
			//System.out.println(">>"+newsId);
			
			String tmpDt = fArray.get(i).getAsJsonObject().get("regDt").toString();
			eDate = tmpDt.substring(1,5) + "-" + tmpDt.substring(5,7) + "-" + tmpDt.substring(7,9);
			rEm.add(eDate);
			//System.out.println(">>"+eDate);
			
			String tmpComp = fArray.get(i).getAsJsonObject().get("cpKorName").toString();
			tmpComp = tmpComp.replace("\"","");
			rComp.add(tmpComp);
			//System.out.println(">>"+tmpComp);
			
			String eCon = fArray.get(i).getAsJsonObject().get("contents").toString();
			String eAuthor = (eCon.contains("기자")) ? eCon : "unknown" ;
			String rAuthor = "";
			try {
			if (eAuthor.length() > 1) {
				if (rAuthor.length() < 1 && eAuthor.indexOf("기자의") > 4) { rAuthor = eAuthor.substring(eAuthor.indexOf("기자의")-4,eAuthor.indexOf("기자의")) + "기자"; }
				else if (rAuthor.length() < 1 && eAuthor.indexOf("기자가") > 4) { rAuthor = eAuthor.substring(eAuthor.indexOf("기자가")-4,eAuthor.indexOf("기자가")) + "기자"; }
				else if (rAuthor.length() < 1 && eAuthor.indexOf("기자 =") > 4) { rAuthor = eAuthor.substring(eAuthor.indexOf("기자 =")-4,eAuthor.indexOf("기자 =")) + "기자"; }
				else if (rAuthor.length() < 1 && eAuthor.indexOf("기자 ]") > 4) { rAuthor = eAuthor.substring(eAuthor.indexOf("기자 ]")-4,eAuthor.indexOf("기자 ]")) + "기자"; }
				else if (rAuthor.length() < 1 && eAuthor.indexOf("기자입") > 4) { rAuthor = eAuthor.substring(eAuthor.indexOf("기자입")-4,eAuthor.indexOf("기자입")) + "기자"; }
				else if (rAuthor.length() < 1 && eAuthor.indexOf("기자】") > 4) { rAuthor = eAuthor.substring(eAuthor.indexOf("기자】")-4,eAuthor.indexOf("기자】")) + "기자"; }
				else if (rAuthor.length() < 1 && eAuthor.indexOf("기자]") > 4) { rAuthor = eAuthor.substring(eAuthor.indexOf("기자]")-4,eAuthor.indexOf("기자]")) + "기자"; }
				
				else { rAuthor = "unknown"; }
			} } catch (Exception e) { 
				e.printStackTrace();
				rAuthor = "unknown";
			}
			if(rAuthor.contains("니다")) { rAuthor = "unknown"; }
			rAuthors.add(rAuthor);
			//System.out.println(">>"+rAuthor);
			
		}
		
		int senum = 0;   // 뉴스의 SEQ
		int getSum = 0;  // 읽어온 뉴스 개수
		for (String se: rEm) {	
			String rDate = se.replace(".","");
			//System.out.print("rdate = "+rDate+"\n");
			//System.out.print("rLinks = "+rLinks.toString()+"\n");
			//if (se.replace(".","").trim().equals(getDate)) {
			if(rLinks.get(senum) != null && rLinks.get(senum).length() > 1) {
				//System.out.print("rLink = "+rLinks.get(senum).toString()+"\n");
				String encoding = "UTF-8";
				Map<String,String> contentMap = daum.getNewsDaumContent(rLinks.get(senum), encoding);
				String content = contentMap.get("content");
				//String author = (rAuthors != null && rAuthors.get(senum) != null) ? rAuthors.get(senum).toString() : "unknown" ;
				String author = rAuthors.get(senum) ;
				if (author.length() < 2) { author = "unknown"; }
				String[] keyset = rLinks.get(senum).toString().split("/");
				//System.out.println("keyset0__ = "+keyset[5].toString());
				//String[] keyset0 = keyset[4].split("&");
				//System.out.println("keyset0 = "+keyset0.toString());
				//String[] keyset1 = keyset0[0].split("=");
				String keyset2 = keyset[5];
				/** 기사 리스트 OUTPUT Form **/
				String nId = rIDs.get(senum);
				String uuid = UUID.randomUUID().toString();  uuid = uuid.replace("-", "");
				String rcomp = rComp.get(senum);
				content =  (getwww.removeTag(content));
				String agreeCnt = getAgreeDaum(nId);
				String outText = 
						CommonUtils.addQt(prefix) 
						+ tab + CommonUtils.addQt(uuid )
						+ tab + CommonUtils.addQt(getwww.newsCate )
						+ tab + CommonUtils.addQt(rDate )
						+ tab + CommonUtils.addQt(rcomp )
						+ tab + CommonUtils.addQt(author)
						+ tab + CommonUtils.addQt(getwww.removeTag(rTitles.get(senum)) )
						+ tab + CommonUtils.addQt(rLinks.get(senum) )
						+ tab + CommonUtils.addQt(getwww.removeTag(content))
						+ tab + CommonUtils.addQt(nId)
						+ tab + CommonUtils.addQt(agreeCnt);
				if("file".equals(getwww.runningMode)) {					
					// #TODO .write 실제 파일에 쓰기
					outFwriter.write(outText+"\n");
				} else if("console".equals(getwww.runningMode)) {
					System.out.println(outText);
				}
				
				String articleId = daum.getDaumNewsKey(nId, "UTF-8");
				
				Map<String,Object> replyMap = daum.getNewsDaumReply(1, null, outFwriterReply, nId, articleId, "UTF-8");
				//System.out.println("Reply : \n"+ replyMap.get("reply"));

				getSum++;				
			}
			senum++;
		}
		//return getSum;
		//System.out.println("return prcDate="+eDate);
		} else {
			eDate = "999";
		}
		return eDate;
	}
	
	public static Map<String, String> getNewsDaumContent(String url, String encoding) throws Exception {
		String rsbody = getwww.getURLBody(url,encoding);
		Map<String,String> content = daum.parseHtmlDaumContent(rsbody);
		//System.out.println(url+"'th => \n"+ content.get("content"));		
		return content;
	}	
	
	public static Map<String, String> parseHtmlDaumContent(String rsbody) throws ParseException {
		Document doc = Jsoup.parse(rsbody);
		Map<String, String> resultMap = new HashMap<String,String>();
		String content = "";
		//String author = "";
		
		/** get Content FirstLine **/
		try {
		Elements docC1 = doc.select(".section_content");
		content = docC1.text();
		content = (!"".equals(content)) ? getwww.removeTex(content) : "";		
		} catch (Exception e) {
			//
			e.printStackTrace();
		}
				
		//System.out.println("content: \n"+ content +"\n");
		resultMap.put("content", content);
		//resultMap.put("author", author);
		
		return resultMap;
	}
	
	public static Map<String, Object> getNewsDaumReply(int pageno, Map<String, Object> replyMap, OutputStreamWriter outFwriterReply, String newsId, String articleId, String charset) throws Exception {
		if (replyMap == null) { replyMap = new HashMap<String, Object>(); }
		//System.out.println("* Start get reply =================================================================================================");
		
		//http://media.daum.net/politics/others/newsview?cPageIndex=2&rMode=list&cSortKey=depth&allComment=T&newsId=
		//http://media.daum.net/politics/others/newsview?cPageIndex=2&rMode=list&cSortKey=depth&allComment=T&newsId=20150126115906052
		//http://news.rhea.media.daum.net/rhea/do/social/json/commentList?bbsId=news&articleId=49856694&allComment=T&cSortKey=depth&cPageIndex=1
	
		if (!"".equals(articleId)) {
			String encoding = "euc_kr";
			if (!"".equals(charset)) encoding = charset;
			
			//String url = "http://m100.Daum.com/svc/guest/list.html?article="+newsId;
			String url = "http://news.rhea.media.daum.net/rhea/do/social/json/commentList?bbsId=news&allComment=T&cSortKey=depth&cSearchKey=&cSearchValue=&callback=rheaJsonp0" 
						+ "&articleId="+articleId  ;
						//+ "&cPageIndex="+pageno   ;
			
			int getSum = 0;
			//if (pageno == 1) { getSum = 99; replyMap = new HashMap<String, Object>(); replyMap.put("newsId", newsId); }  // 초기값 초기MAP 설정  getSum=99 는 2페이지는 무조건 검색하기 위함
			
			String replyUrl = "";
			replyUrl = url + "&cPageIndex="+Integer.toString(pageno);
			//System.out.println("reply URL ::"+replyUrl);
			String rsbody = getwww.getURLBody(replyUrl,encoding);		
			
			replyMap = daum.parseHtmlDaumReply2(rsbody, replyMap, outFwriterReply, newsId);
			getSum = Integer.parseInt(replyMap.get("getSum").toString());		
			if(!"file".equals(getwww.runningMode)) {		
				System.out.println("============ NewsID : "+newsId+"'th REPLY => " + "getting count : "+getSum);
			}
			
			/*
			if (getSum > 0) {
				System.out.println("\n--------------------------------------------------- toPage :: "+ ( pageno + 1 ) +"\n");
				
				replyMap = daum.getNewsDaumReply(++pageno, replyMap, outFwriterReply, newsId, articleId, charset);
			}
			*/
			
			//System.out.println("rsbody :: "+rsbody);	
			searchNum += getSum;	
						
			
			//List<String> reply = (List<String>) replyMap.get("reply");
			List<String> reply = null;
			//if (reply != null) { System.out.println("REPLY :"+reply.toString()); }
			
			//if (getSum > 0) { 
			//	daum.getNewsDaumReply(newsId, ++pageno, replyMap, outFwriterReply);  // #TODO 이번 페이지에 읽어온 댓글이 있을 경우 다음 댓글 페이지를 검색하도록 조정 -재귀호출 
			//} else {
				// FILE WRITING
				/** 기사 댓글 리스트 OUTPUT Form**/
				if (reply != null && reply.size() > 0) { 
					for (String reTxt: reply) {
						//System.out.println("  RE: "+reTxt.toString());
						// #TODO .write 실제 파일에 쓰기
						
						try {
							if("file".equals(getwww.runningMode)) {
								outFwriterReply.write(reTxt.toString()+"\n"); 
							} else if ("console".equals(getwww.runningMode)) {
								System.out.println(reTxt.toString()+"\n");
							}
						} catch (Exception e) { e.printStackTrace(); }
						
					}				
				}
			//}
		}
		
		return replyMap;
	}
	
	public static String getDaumNewsKey(String newsId, String charset) throws ParseException {		
		String keyUrl = "http://media.daum.net/api/service/news/view.jsonp?newsId="+newsId;
		String keyBody = "";
		try {
			keyBody = getwww.getURLBody(keyUrl, charset);
		} catch (Exception e) {
			e.printStackTrace();
		}		
		
		String articleId = "";
		if (!"".equals(keyBody)) {
			/** get REPLY article Id from  NewsID **/
			try {			
				JsonElement jelement = new JsonParser().parse(keyBody);
			    JsonObject  jobject = jelement.getAsJsonObject();	
			    //System.out.println("jobject :: "+jobject.toString());
			    
			    articleId = jobject.get("uccId").toString();	
			    articleId = articleId.replace("\"", "");
			    
			    //System.out.println("### ArticleId => "+articleId.toString());
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		
		return articleId;
	}
		
	public static Map<String, Object> parseHtmlDaumReply2(String rsbody, Map<String, Object> replyMap, OutputStreamWriter outFwriterReply, String newsId) throws ParseException {
		if (replyMap == null) { replyMap = new HashMap<String, Object>(); }
		List<String> reply = (ArrayList<String>) replyMap.get("reply");
		if (reply == null) { reply = new ArrayList<String>(); }
		
		/** get reply count **/
		int replyCount = (int) (( replyMap.get("replyCount") != null ) ? (replyMap.get("replyCount")) : 0) ;
		int getSum = 0;
		
		/** get REPLY List per Page_No v**/
		try {			
			String rsbody1 = rsbody.substring(rsbody.indexOf("(")+1, rsbody.lastIndexOf(")"));
			//System.out.println("### replys :: "+rsbody1);
			
			JsonElement jelement = new JsonParser().parse(rsbody1);
		    JsonObject  jobject = jelement.getAsJsonObject();
		    JsonArray  jArr = (JsonArray) jobject.get("comments");
		    
		    for (int i = 0; i < jArr.size(); i++) {
		    	JsonElement jEl = new JsonParser().parse(jArr.get(i).toString());
		    	JsonObject  jObj = jEl.getAsJsonObject();
		    	
		    	String uuid = UUID.randomUUID().toString();  uuid = uuid.replace("-", "");
		    	String articleId = jObj.get("articleId").toString();
		    	String cmntId = jObj.get("id").toString();
		    	String cType = "R";
		    	JsonObject regDate = (JsonObject) jObj.get("regDate");
		    	String regdate = regDate.get("time").toString();
		    	regdate = DateUtils.getDateFromTimestampStr(regdate.substring(0,regdate.length()-3));
		    		    			    	
		    	String author = jObj.get("daumName").toString();	
		    	author = getwww.removeTag(author);
		    	
		    	String childCnt = jObj.get("childCount").toString();
		    	int childCount = ( !"".equals(childCnt)) ? Integer.parseInt(childCnt) : 0;
		    	String agrCnt = jObj.get("recommendCount").toString();
		    	String denCnt = jObj.get("disagreeCount").toString();
		    	String content = jObj.get("commentContent").toString();
		    	
		    	content = getwww.removeTag(content);
		    	
		    			    	
		    	//System.out.println("# REPLY ::  " + i + "'th => " + jArr.get(i));
		    	String outText = "";
		    	outText = CommonUtils.addQt(prefix) 
		    			+ tab + CommonUtils.addQt(uuid) 
		    			+ tab + CommonUtils.addQt(cType) 
		    			+ tab + CommonUtils.addQt(regdate) 
		    			+ tab + CommonUtils.addQt(author)  
		    			+ tab + CommonUtils.addQt(content)
		    			+ tab + CommonUtils.addQt(newsId) 
		    			+ tab + CommonUtils.addQt("0")
		    			+ tab + CommonUtils.addQt(cmntId) 
		    			+ tab + CommonUtils.addQt(agrCnt) 
		    			+ tab + CommonUtils.addQt(denCnt) 
		    			+ tab + CommonUtils.addQt(childCnt) ;
		    	
		    	if("file".equals(getwww.runningMode)) {					
					// #TODO .write 실제 파일에 쓰기
		    		outFwriterReply.write(outText+"\n");
				} else if("console".equals(getwww.runningMode)) {
					System.out.println("#REPLY ::  " + i + "'th => " + outText);
				}
		    	 	
		    	/*
		    	 * 대댓글 건수( childCount > 0) 일 경우 대댓글 리스트 채집
		    	 */
		    	if (childCount > 0) {
		    		daum.getDaumReplyReply(1, replyMap, outFwriterReply, newsId, articleId, cmntId, "UTF-8");
		    	}
		    	
				getSum++;
		    }
		    
		    //System.out.println("### jArr :: "+jArr.toString());
		    
		} catch (Exception e) {
			e.printStackTrace();
		} 
			
		//if (reply != null ) { System.out.println("test reply :"+reply.toString()); }
		
		replyCount = replyCount + getSum;
		replyMap.put("getSum",Integer.toString(getSum));
		replyMap.put("replyCount", replyCount);
		replyMap.put("reply", reply);
		return replyMap;
	}
	
	public static Map<String, Object> getDaumReplyReply(int pageno, Map<String, Object> replyMap, OutputStreamWriter outFwriterReply, String newsId, String articleId, String commentId, String charset) throws Exception {
		if (replyMap == null) { replyMap = new HashMap<String, Object>(); }
	
		//http://news.rhea.media.daum.net/rhea/do/social/json/commentView?bbsId=news&articleId=53316794&listSortKey=depth&commentId=286368984&cView=view&allComment=T&useFullData=F&cPageIndex=1&callback=rheaJsonp1
		
		if (!"".equals(articleId)) {
			String encoding = ( !"".equals(charset) ) ? charset : "euc_kr";
			
			String url = "http://news.rhea.media.daum.net/rhea/do/social/json/commentView?bbsId=news&&listSortKey=depth&allComment=T&cView=view&useFullData=F&callback=rheaJsonp1" 
						+ "&articleId="+articleId  
						+ "&commentId="+commentId ;
			
			int getSum = 0;
			
			String reReplyUrl = "";
			reReplyUrl = url + "&cPageIndex="+Integer.toString(pageno);
			//System.out.println("##Re::Reply URL ::"+reReplyUrl);
			String rsbody = getwww.getURLBody(reReplyUrl,encoding);		
			
			replyMap = daum.parseHtmlDaumReplyReply(rsbody, replyMap, outFwriterReply, newsId, commentId);
			getSum = Integer.parseInt(replyMap.get("getSum").toString());	
			if(!"file".equals(getwww.runningMode)) {
				System.out.println("##  CommentID : "+commentId + " 'th RE::REPLY => " + "getting count : "+getSum);
			}
			
			if (getSum > 0) {
				replyMap = daum.getDaumReplyReply(++pageno, replyMap, outFwriterReply, newsId, articleId, commentId, charset);
			}				
		}
		
		return replyMap;	
	}	

	public static Map<String, Object> parseHtmlDaumReplyReply(String rsbody, Map<String, Object> replyMap, OutputStreamWriter outFwriterReply, String newsId, String commentId) throws ParseException {
		if (replyMap == null) { replyMap = new HashMap<String, Object>(); }
		List<String> reply = (ArrayList<String>) replyMap.get("reply");
		if (reply == null) { reply = new ArrayList<String>(); }
		
		/** get reply count **/
		int replyCount = (int) (( replyMap.get("replyCount") != null ) ? (replyMap.get("replyCount")) : 0) ;
		int getSum = 0;
		
		/** get REPLY List per Page_No v**/
		try {			
			String rsbody1 = rsbody.substring(rsbody.indexOf("(")+1, rsbody.lastIndexOf(")"));
			//System.out.println("### replys :: "+rsbody1);
			
			JsonElement jelement = new JsonParser().parse(rsbody1);
		    JsonObject  jobject = jelement.getAsJsonObject();

		    JsonObject  jParentObj = (JsonObject) jobject.get("parentComment");
		    JsonArray  jArr = (JsonArray) jobject.get("comments");
		    
		    if (jArr != null) {
			    for (int i = 0; i < jArr.size(); i++) {
			    	// 원 댓글의 ID 채집		    	
			    	String parentId = jParentObj.get("id").toString();
			    	
			    	// 대댓글의 ID 채집
			    	JsonElement jEl = new JsonParser().parse(jArr.get(i).toString());
			    	JsonObject  jObj = jEl.getAsJsonObject();
			    	
			    	String cmntId = jObj.get("id").toString();
			    	String cType = "RR";
			    	JsonObject regDate = (JsonObject) jObj.get("regDate");
			    	String regdate = regDate.get("time").toString();
			    	regdate = DateUtils.getDateFromTimestampStr(regdate.substring(0,regdate.length()-3));
			    	
			    	String author = jObj.get("daumName").toString();
			    	author = getwww.removeTag(author);
			    	String agrCnt = jObj.get("recommendCount").toString();
			    	String denCnt = jObj.get("disagreeCount").toString();
			    	String content = jObj.get("commentContent").toString();
			    	
			    	content = getwww.removeTag(content);		    	
	
			    	//System.out.println("## reREPLY ::  " + i + "'th => " + jArr.get(i));		    	
			    	//System.out.println("## reREPLY ::  " + i + "'th => " + newsId + " | " + cType + " | " + cmntId + " | " + author );
			    	
			    	String outText = "";
			    	outText = CommonUtils.addQt(prefix) 
			    			+ tab + CommonUtils.addQt(CommonUtils.getUuid()) 
			    			+ tab + CommonUtils.addQt(cType) 
			    			+ tab + CommonUtils.addQt(regdate)
			    			+ tab + CommonUtils.addQt(author) 
			    			+ tab + CommonUtils.addQt(content) 
			    			+ tab + CommonUtils.addQt(newsId) 
			    			+ tab + CommonUtils.addQt(parentId)
			    			+ tab + CommonUtils.addQt(cmntId) 
			    			+ tab + CommonUtils.addQt(agrCnt) 
			    			+ tab + CommonUtils.addQt(denCnt)
			    			+ tab + CommonUtils.addQt("0");
			    	
			    	if("file".equals(getwww.runningMode)) {					
						// #TODO .write 실제 파일에 쓰기
			    		outFwriterReply.write(outText+"\n");
					} else if("console".equals(getwww.runningMode)) {
						System.out.println("############# re#REPLY ::  " + i + "'th => " + outText);
					}
			    	
					getSum++;
			    }
		    }
		    
		    //System.out.println("### jArr :: "+jArr.toString());
		    
		} catch (Exception e) {
			e.printStackTrace();
		} 
					
		replyCount = replyCount + getSum;
		replyMap.put("getSum",Integer.toString(getSum));
		replyMap.put("replyCount", replyCount);
		replyMap.put("reply", reply);
		return replyMap;
	}
	
	public static Map<String, Object> parseHtmlDaumReply(String rsbody, Map<String, Object> replyMap) throws ParseException {
		Document doc = Jsoup.parse(rsbody);
		//System.out.println("DOC::"+doc.childNodes().toString());
		
		if (replyMap == null) { replyMap = new HashMap<String, Object>(); }
		
		List<String> reply = (ArrayList<String>) replyMap.get("reply");
		if (reply == null) { reply = new ArrayList<String>(); }
		
		/** get reply count **/
		String replyCount = "0";
		int getSum = 0;
		
		
		/** get REPLY List per Page_No v**/
		try {
			/*
			Elements docReplyUserList = doc.select(".user_setbox a");
			//System.out.println("REPLYsss : "+docReplyUserList);
			ArrayList<String> replyUser = new ArrayList<String>();
			ArrayList<String> replyName = new ArrayList<String>();
			for (Element ep: docReplyUserList) {
				if (!ep.toString().contains("guest/user")) {  // 대댓글 제외
					String eps = ep.attr("href").toString();
					String epss = eps.substring(eps.indexOf("?usr_id=")+8, eps.lastIndexOf("&usr_no"));  // 괄호안의 사용자 아디디만 가져옴 usr_id
					//System.out.println("RP USER:"+epss);
					replyUser.add(epss);
					String unss = ep.text().toString(); // 사용자 이름만 가져옴
					//System.out.println("RP USER NAME:"+unss);
					replyName.add(unss);					
				}
			}
			*/
			Elements docReplyList = doc.select("#rheaComment");
			//ArrayList<String> replyComment = new ArrayList<String>();
			System.out.println("REPLYsss : "+docReplyList.toString()+"\n");
			for (Element cp: docReplyList) {
				//System.out.println("REPLYsss:"+cp.text());
				//replyComment.add(cp.text().toString());
			}
			/*
			Elements docReplyRateAllowList = doc.select(".user_etc a[style]");
			ArrayList<String> replyCommentRateAllow = new ArrayList<String>(); // 찬성개수 Array
			ArrayList<String> replyCommentRateDeny = new ArrayList<String>();  // 반대개수 Array
			//System.out.println("REPLY Rate sss : "+docReplyRateAllowList+"\n");
			for (Element rp: docReplyRateAllowList) {
				//System.out.println("REPLY Rate sss :"+rp.text());
				String rps = rp.text().toString().trim();
				String rpss = rps.substring(rps.indexOf("(")+1, rps.lastIndexOf(")"));  // 괄호안의 수치만 가져옴
				if (rps.startsWith("찬성")) { 	replyCommentRateAllow.add(rpss); }	else if (rps.startsWith("반대")) { replyCommentRateDeny.add(rpss); }		
			}
			
			for (int i=0; i<replyUser.size(); i++) {
				if (!replyComment.get(i).toString().startsWith("관리자가")) {
					//System.out.println(replyUser.get(i).toString() + "\t" + replyName.get(i) + "\t" + replyComment.get(i).toString() 
					//	+ "\t" +replyCommentRateAllow.get(i) + "\t" + replyCommentRateDeny.get(i));
					
					// 댓글 리스트 OUTPUT Formatting 
					reply.add(prefix + replyMap.get("newsId").toString() + "\t" + replyUser.get(i).toString() + "\t"   
						+ replyName.get(i) + "\t" + replyComment.get(i).toString() 
						+ "\t" +replyCommentRateAllow.get(i) + "\t" + replyCommentRateDeny.get(i));
					getSum = i;
				}
			}
			
			*/
		} catch (Exception e) {
			//
		} 
			
		//if (reply != null ) { System.out.println("test reply :"+reply.toString()); }
		
		replyMap.put("getSum",Integer.toString(getSum));
		if ("".equals(replyCount)) { replyCount = "0"; }
		replyMap.put("replyCount", replyCount);
		replyMap.put("reply", reply);
		return replyMap;
	}
}