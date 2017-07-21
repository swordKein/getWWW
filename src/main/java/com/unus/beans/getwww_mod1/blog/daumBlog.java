package com.unus.beans.getwww_mod1.blog;

import java.io.OutputStreamWriter;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.unus.beans.getwww_mod1.*;
import com.unus.beans.getwww_mod1.newssite.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class daumBlog extends Thread {
	public static int searchNum = 0;
	public static int tryCount = 0;
	public static String prefix = "daum_blog";
	//public static String newsCate = "poli";
	public static String reqHost = "http://search.daum.net";
	public static int reqPort = 80;
	//public static String reqPath = "/api/service/news/list/category/1002.jsonp";
	public static final String reqPath = "/search?w=blog&m=board&f=section&SA=daumsec&lpp=10&nil_src=blog&DA=PGD";
	
	public static int getDaumBlog(String reqTxt, int pageNo, String stDate, String edDate, OutputStreamWriter outFwriter, OutputStreamWriter outFwriterReply) throws Exception{
		//System.out.println("* Start =================================================================================================");
		
		String reqQuery = URLEncoder.encode(reqTxt, "UTF-8");
		int getSum = 0;		
		if ( pageNo == 1 ) { getSum = 0; }
		
		String reqPageno = Integer.toString(pageNo);
		Map<String,String> reqparam = new HashMap<String,String>();
		//reqparam.put("reqQuery", reqQuery);
		reqparam.put("reqPage","page");
		reqparam.put("reqPageNo", reqPageno);
		
		reqparam.put("reqQuery","q");
		reqparam.put("reqQueryString",reqQuery);
				

		String newsCateUrl = "";
		
		String reqPathIn = reqPath;
								
		String rsbody = getwww.getHtmlBody(reqHost, reqPort, reqPathIn, reqparam, "UTF-8");				
		getSum = daumBlog.parseJsonDaumBlog(rsbody, outFwriter, outFwriterReply);
		
		if (getSum == 10) {
			int resultCnt = getDaumBlog(reqTxt, ++pageNo, stDate, edDate, outFwriter, outFwriterReply);
		}
		
		//System.out.println("reqQuery :: "+ reqQuery + " :> " + pageNo + " PAGE getting count : "+getSum);
		return pageNo;		
	}
	
	public static String getAgreeDaum(String newsId) throws Exception{
		String reqHost = "http://like.Daum.net";
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
	
	public static int parseJsonDaumBlog(String rsbody, OutputStreamWriter outFwriter, OutputStreamWriter outFwriterReply) throws Exception {
		Document doc = Jsoup.parse(rsbody,"UTF-8");
		System.out.printf("%s \n",doc.toString());
		String tUrl = doc.select("script").toString().replace("\n", "");
		System.out.println(" s "+tUrl.indexOf("(")+ " : d : "+tUrl.lastIndexOf(")"));
		
		if (!"".equals(tUrl)) { 
			tUrl = tUrl.substring(tUrl.indexOf("("),tUrl.lastIndexOf(")")); 
			//tUrl
		}
		System.out.printf("\n\nurl ::%s \n", tUrl);
		
		/** get TITLE & LINK List **/
		Elements docTitles = doc.select("body");
		System.out.println("Titles: => \n"+docTitles.toString());
		ArrayList<String> rTitles = new ArrayList<String>();
		ArrayList<String> rLinks = new ArrayList<String>();
		for (Element et: docTitles) {
			String eTitle = et.text().toString();
			String eLink = et.attr("href");
		
			System.out.println("TITLE : => "+ eTitle + " & LINK : "+eLink);
			
			rTitles.add(eTitle);
			rLinks.add(eLink);
		}
		
		/** get DATE List **/
		Elements docDates = doc.select(".list_type_1 .list_data .date");
		//System.out.println("Dates: => \n"+docDates.toString());
		ArrayList<String> rEm = new ArrayList<String>();
		String eDate = "";
		for (Element eD: docDates) {
			eDate = eD.text().toString();
			if (eDate.length() > 0) { eDate = getwww.removeTag(eDate).trim().replace(".", "-").substring(0,10); }
			//System.out.println("DATE : => "+ eDate);
			
			rEm.add(eDate);
		}
		
		/** get AUTHOR List **/
		Elements docAuthors = doc.select(".list_type_1 .list_data");
		//System.out.println("Authors: => \n"+docAuthors.toString());
		ArrayList<String> rAu = new ArrayList<String>();
		String eAuthor = "";
		for (Element eA: docAuthors) {
			eAuthor = eA.text().toString();
			if(!"".equals(eAuthor)) { eAuthor = eAuthor.split(">")[0]; }
			//System.out.println("AUTHOR : => "+ eAuthor);
			
			rAu.add(eAuthor);
		}
		
		/** get BLOG-ID List **/
		Elements docIDs = doc.select(".list_type_1 input[name=blogId]");
		//System.out.println("IDs: => \n"+docIDs.toString());
		ArrayList<String> rIDs = new ArrayList<String>();
		for (Element eId: docIDs) {
			String eI = eId.attr("value").toString();		
			//System.out.println("ID : => "+ eI);			
			rIDs.add(eI);
		}
		
		/** get BLOG-POST-ID List **/
		Elements docPIDs = doc.select(".list_type_1 input[name=logNo]");
		//System.out.println("PIDs: => \n"+docPIDs.toString());
		ArrayList<String> rPIDs = new ArrayList<String>();
		
		ArrayList<String> rLink2 = new ArrayList<String>();
		int idNo = 0;
		String preLink = "http://blog.daum.com/PostView.nhn?blogId=";
		
		for (Element ePid: docPIDs) {
			String ePi = ePid.attr("value").toString();		
			//System.out.println("PID : => "+ ePi);			
			rPIDs.add(ePi);
			
			String conUrl = preLink +  rIDs.get(idNo) + "&logNo=" + ePi;
			rLink2.add(conUrl);
			idNo++;
		}
		
		String tab = "\t";	
		int senum = 0;   // 뉴스의 SEQ
		int getSum = 0;  // 읽어온 뉴스 개수
		for (String se: rEm) {	
			String rDate = se.replace(".","");
			//System.out.print("rdate = "+rDate+"\n");
			//System.out.print("rLinks = "+rLinks.toString()+"\n");
			//if (se.replace(".","").trim().equals(getDate)) {
			if(rLink2.get(senum) != null && rLink2.get(senum).length() > 1) {
				//System.out.print("rLink = "+rLinks.get(senum).toString()+"\n");
				String encoding = "euc_kr";
				Map<String,String> contentMap = getDaumBlogContent(rLink2.get(senum), encoding);
				String content = contentMap.get("content");
				String author = "";
				if (rAu.get(senum) == null || "".equals(rAu.get(senum))) { author = "unknown"; }
				else { author = rAu.get(senum); }
				String[] keyset = rLinks.get(senum).toString().split("/");
				//System.out.println("keyset0__ = "+keyset[3].toString());
				//String[] keyset0 = keyset[3].split("&");
				//System.out.println("keyset0 = "+keyset0.toString());
				//String[] keyset1 = keyset0[1].split("=");
				//String keyset2 = keyset1[1];
				//System.out.println("keyset2 = "+keyset2.toString());
				/** 기사 리스트 OUTPUT Form **/
				String nId = rPIDs.get(senum);
				String uuid = UUID.randomUUID().toString();  uuid = uuid.replace("-", "");
				//String rcomp = rComp.get(senum);
				content =  (getwww.removeTag(content));
				//String agreeCnt = getAgreeDaum(nId);
				String outText = 
						prefix 
						+ tab + uuid 
						+ tab + getwww.newsCate 
						+ tab + rDate 
					//	+ tab + rcomp 
						+ tab + "-"
						+ tab + author
						+ tab + getwww.removeTag(rTitles.get(senum)) 
						+ tab + rLinks.get(senum) 
						+ tab + getwww.removeTag(content)
						+ tab + nId
					//	+ tab + agreeCnt
						;
				if("file".equals(getwww.runningMode)) {					
					// #TODO .write 실제 파일에 쓰기
					outFwriter.write(outText+"\n");
				} else if("console".equals(getwww.runningMode)) {
					System.out.println(outText);
				}
				
				//Map<String,Object> replyMap = Seoul.getDaumBlogReply(keyset[9].replace(".html",""),1,null, outFwriterReply);
				//System.out.println("Reply : \n"+ replyMap.get("reply"));

				getSum++;				
			}
			senum++;
		}
		return getSum;
	}
	
	public static Map<String, String> getDaumBlogContent(String url, String encoding) throws Exception {
		String rsbody = getwww.getURLBody(url,encoding);
		//System.out.println(url+"'th => \n");
		Map<String,String> content = daumBlog.parseHtmlDaumContent(rsbody);
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
		//Elements docC1 = doc.select(".view");
		Elements docC1 = doc.select(".post-view");
		//System.out.println("###"+docC1.text().toString());
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
	
	public static Map<String, Object> getDaumBlogReply(String url, int pageno, Map<String, Object> replyMap, OutputStreamWriter outFwriterReply, String newsId, String charset) throws Exception {
		//System.out.println("* Start get reply =================================================================================================");
		
		//http://media.Daum.net/politics/others/newsview?cPageIndex=2&rMode=list&cSortKey=depth&allComment=T&newsId=
		//http://media.Daum.net/politics/others/newsview?cPageIndex=2&rMode=list&cSortKey=depth&allComment=T&newsId=20150126115906052
		//http://news.rhea.media.Daum.net/rhea/do/social/json/commentList?bbsId=news&articleId=49856694&allComment=T&cSortKey=depth&cPageIndex=1
				
		String encoding = "euc_kr";
		if (!"".equals(charset)) encoding = charset;
		
		//String newsId = "";
		//String url = "http://m100.Daum.com/svc/guest/list.html?article="+newsId;
		int getSum = 0;
		//if (pageno == 1) { getSum = 99; replyMap = new HashMap<String, Object>(); replyMap.put("newsId", newsId); }  // 초기값 초기MAP 설정  getSum=99 는 2페이지는 무조건 검색하기 위함
		
		String replyUrl = "";
		for (int i=1; i<3; i++) {
			replyUrl = url + "&cPageIndex="+Integer.toString(i);
			System.out.println("reply URL ::"+replyUrl);
			String rsbody = getwww.getURLBody(replyUrl,encoding);		
			replyMap = daumBlog.parseHtmlDaumReply(rsbody, replyMap);
			getSum = Integer.parseInt(replyMap.get("getSum").toString());		
			System.out.println("NewsID : "+newsId+"'th REPLY => " + "getting count : "+getSum);
			//System.out.println("rsbody :: "+rsbody);	
			searchNum += getSum;	
		}			
		
		//List<String> reply = (List<String>) replyMap.get("reply");
		List<String> reply = null;
		//if (reply != null) { System.out.println("REPLY :"+reply.toString()); }
		
		//if (getSum > 0) { 
		//	Daum.getDaumBlogReply(newsId, ++pageno, replyMap, outFwriterReply);  // #TODO 이번 페이지에 읽어온 댓글이 있을 경우 다음 댓글 페이지를 검색하도록 조정 -재귀호출 
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