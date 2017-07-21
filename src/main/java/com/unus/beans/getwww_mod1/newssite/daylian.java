package com.unus.beans.getwww_mod1.newssite;

import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.unus.beans.getwww_mod1.getwww;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

public class daylian {
	public static int searchNum = 0;
	public static int tryCount = 0;
	public static String prefix = "daylian";
	public static String reqHost = "http://www.dailian.co.kr";
	public static int reqPort = 80;
	public static String reqPath = "/newslist/?kind=menu_code&code=1";
	public static String newsCate = "poli";
	
	public static void getNewsDaylian(int pageno, String dtDate, OutputStreamWriter outFwriter, OutputStreamWriter outFwriterReply) throws Exception{
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
		
		//reqparam.put("reqDt","date");
		//reqparam.put("reqDtDate", dtDate);
				
		String rsbody = getwww.getHtmlBody(reqHost, reqPort, reqPath, reqparam, "UTF-8");
		//rsbody = convertEuckrToUtf8(rsbody);
		try {
		daylian.parseHtmlDaylian(rsbody, outFwriter, outFwriterReply);
		} catch (Exception e) { e.printStackTrace(); }
		searchNum += getSum;
		//System.out.println(getDate+"'th News "+ pageno + " PAGE getting count : "+getSum);
	}
	
	public static void parseHtmlDaylian(String rsbody, OutputStreamWriter outFwriter, OutputStreamWriter outFwriterReply) throws Exception {
		Document doc = Jsoup.parse(rsbody,"UTF-8");
		
		/** get TITLE & LINK List **/
		Elements docTitles = doc.select(".list_news1");
		//System.out.println("Titles: => \n"+docTitles.toString());
		ArrayList<String> rTitles = new ArrayList<String>();
		ArrayList<String> rLinks = new ArrayList<String>();
		ArrayList<String> rEm = new ArrayList<String>();
		ArrayList<String> rAuthors = new ArrayList<String>();
		String eDate = "";
		String eAuthor = "";
		String[] eDatess = null;
		for (Element et: docTitles) {
			String eTitle = et.select(".list_title1").text().toString();
			String eLink = reqHost + et.select(".list_title1 a").attr("href");
			String eDates = et.select(".list_sub1 a").first().nextSibling().toString();
			try {
			if (eDates.length() > 10) {
				eDatess = eDates.split("\\(");
				eDate = eDatess[1].substring(0,10);
				eAuthor = (eDatess[0].length() > 1) ? eDatess[0].substring(1 ,eDatess[0].length()) : "unknown" ;
			} } catch(Exception e) { eAuthor = "unknown"; }
			 
			rTitles.add(eTitle);
			rLinks.add(eLink);		
			rEm.add(eDate);
			rAuthors.add(eAuthor);
			//System.out.println("TITLE : => "+ eTitle + "& DATE = "+eDate + "& LINK : "+eLink);
			//System.out.println("TITLE : => "+ eTitle + " &DATE="+eDate+" &AUTHOR="+eAuthor+ " & LINK : "+eLink);
			
		}
		
		/** get TITLE & LINK List **/
		Elements docTitles2 = doc.select(".list_news2");
		//System.out.println("Titles: => \n"+docTitles2.toString() +": size"+docTitles2.size());
		String eDate2 = "";
		String eAuthor2 = "";
		String[] eDatess2 = null;
		for (Element et2: docTitles2) {
			String eTitle2 = et2.select(".list_title2").text().toString();
			String eLink2 = reqHost + et2.select(".list_title2 a").attr("href");
			String eDates2 = et2.select(".list_sub2 a").first().nextSibling().toString();
			try {
			if (eDates2.length() > 10) {
				eDatess2 = eDates2.split("\\(");
				eDate2 = eDatess2[1].substring(0,10);
				eAuthor2 = (eDatess2[0].length() > 1) ? eDatess2[0].substring(1 ,eDatess2[0].length()) : "unknown" ;
			} else { 
				eDate = "unknown";
			}
			} catch (Exception e) { eAuthor2 = "unknown"; e.printStackTrace(); }
			rTitles.add(eTitle2);
			rLinks.add(eLink2);			
			rEm.add(eDate2);
			rAuthors.add(eAuthor2);
			//System.out.println("TITLE : => "+ eTitle + "& DATE = "+eDate + "& LINK : "+eLink);
			//System.out.println("TITLE2 : => "+ eTitle2 + " &DATE="+eDate2+" &AUTHOR="+eAuthor2+ " & LINK : "+eLink2);
			
		}
		
		/** get DATE List 
		Elements docDates = doc.select(".phArtc_list dd[class=date]");
		//System.out.println("Dates: => \n"+docDates.toString());
		ArrayList<String> rEm = new ArrayList<String>();
		ArrayList<String> rAuthors = new ArrayList<String>();
		String eDate = "";
		for (Element eD: docDates) {
			eDate = eD.select("span").text().toString();
			//System.out.println("sDATE : => "+ eDate);
			if (eDate.length() > 0) { eDate = eDate.substring(0,12).trim().replace(" ", "-"); }
			//System.out.println("DATE : => "+ eDate);
			String tAuthor = eD.select("strong").text().toString();
			//System.out.println("test-author : => "+ tAuthor);
			rAuthors.add(tAuthor);
			
			rEm.add(eDate);
		} **/
		
		/** get AUTHOR List 
		Elements docAuthors = doc.select(".phArtc_list dd[class=date] strong");
		System.out.println("Authors: => \n"+docAuthors.toString());
		ArrayList<String> rAuthors = new ArrayList<String>();
		for (Element eA: docAuthors) {
			String eAuthor = eA.text();
			//System.out.println("AUTHOR : "+eAuthor);
			
			rAuthors.add(eAuthor);
		}	 **/
		

		String tab = "\t";	
		int senum = 0;   // 뉴스의 SEQ
		int getSum = 0;  // 읽어온 뉴스 개수
		
		//System.out.println("title:"+rTitles.size()+ " & date:"+rEm.size()+" & link:"+rLinks.size()+" & author:"+rAuthors.size());
		
		for (String se: rTitles) {	
			String rDate = rEm.get(senum).replace(".","");
			//System.out.println("t:"+rTitles.get(senum)+" & d:"+rEm.get(senum)+" & L:"+rLinks.get(senum)+" &A:"+rAuthors.get(senum));

			//System.out.print("rdate = "+rDate+"\n");
			//System.out.print("rLinks = "+rLinks.toString()+"\n");
			//if (se.replace(".","").trim().equals(getDate)) {
			if(rTitles.get(senum) != null
					&& rEm.get(senum) != null
					&& rAuthors.get(senum) != null
					&& rLinks.get(senum) != null && rLinks.get(senum).length() > 1) {				
				//System.out.print("rLink = "+rLinks.get(senum).toString()+"\n");
				String encoding = "UTF-8";
				Map<String,String> contentMap = daylian.getNewsDaylianContent(rLinks.get(senum), encoding);				

				String title = getwww.removeTag(rTitles.get(senum));
				//String title = rTitles.get(senum);
				//System.out.println("title::"+title);
				
				String content = contentMap.get("content");
				//System.out.println("content1::"+content);
				try {
					content =  getwww.removeTag(content);
				} catch (Exception e) { 
					//
				}
				//System.out.println("content2::"+content);
				
				String author = (rAuthors != null && rAuthors.get(senum) != null) ? rAuthors.get(senum) : "unknown" ;

				if (author.length() < 2) { author = "unknown"; }
				String[] keyset = rLinks.get(senum).toString().split("/");
				//System.out.println("keyset0__ = "+keyset[5].toString());
				//String[] keyset0 = keyset[4].split("&");
				//System.out.println("keyset0 = "+keyset0.toString());
				//String[] keyset1 = keyset0[0].split("=");
				String keyset2 = keyset[5];
				/** 기사 리스트 OUTPUT Form **/

				String uuid = UUID.randomUUID().toString();  uuid = uuid.replace("-", "");
				/*
				String outText = prefix + keyset2 + tab + rDate
						+ tab + author
						//+ tab + "unknown"
						+ tab + getwww_mod1.removeTag(rTitles.get(senum)) + tab + rLinks.get(senum) + tab + getwww_mod1.removeTag(content);
				*/
				
				String outText = 
						prefix 
						+ tab + uuid 
						+ tab + newsCate 
						+ tab + rDate 
						+ tab + prefix 
						+ tab + author
						+ tab + title
						+ tab + rLinks.get(senum) 
						+ tab + content
						+ tab + keyset2;
						//+ tab + "0";
				if("file".equals(getwww.runningMode)) {					
					// #TODO .write 실제 파일에 쓰기
					outFwriter.write(outText+"\n");
				} else if("console".equals(getwww.runningMode)) {
					System.out.println(outText+"\n");
				}
				
				//Map<String,Object> replyMap = daylian.getNewsDaylianReply(rLinks.get(senum),0,null, outFwriterReply);
//				/System.out.println("Reply : \n"+ replyMap.get("reply"));

				getSum++;
			}
			senum++;
		}
		System.out.println("parseHtml end");
		//return getSum;
	}
	
	public static Map<String, String> getNewsDaylianContent(String url, String encoding) throws Exception {
		String rsbody = getwww.getURLBody(url,encoding);
		
		Map<String,String> content = new HashMap<String,String>();
		try {
			content = daylian.parseHtmlDaylianContent(rsbody);
		} catch (Exception e) { e.printStackTrace(); }
		//System.out.println(url+"'th => \n"+ content.get("content"));		
		return content;
	}	
	
	public static Map<String, String> parseHtmlDaylianContent(String rsbody) throws ParseException {
		Document doc = Jsoup.parse(rsbody);
		Map<String, String> resultMap = new HashMap<String,String>();
		String content = "";
		String author = "";
		
		/** get Content FirstLine **/
		try {
		Elements docC1 = doc.select("#view_con");
		content = docC1.text();
		content = (!"".equals(content)) ? getwww.removeTex(content) : "";
		//System.out.println("XXX:"+content);

		/*
		if (content.length() > 15) { 
			author = content.substring(content.length()-15,content.length());
			try {
			author = 
			(author.contains("."))	? author.substring(author.lastIndexOf(".")+2, author.length()).trim() : "unknown" ;
			} catch (Exception e) {
				//e.printStackTrace();
				author = "unknown";
			} 
			//System.out.println("author:"+author);
		}
		*/
				
		} catch (Exception e) {
			//
			e.printStackTrace();
		}
		
		/** get Content SecondLine 
		try {
		Element docC2 = doc.select("#contents div[class=article_txt] p[class=title_foot]").last();
		content = (content.length() == 0 && docC2.nextSibling() != null) ? docC2.nextSibling().toString() : ""; 
		//System.out.println("YYY: "+docC2.nextSibling().toString());
		//System.out.println("XXX:"+content);		
		} catch (Exception e) {
			//
			e.printStackTrace();
		}**/
		
		/** get Content List
		try {
		Elements docC3 = doc.select("#contents div[class=article_txt] br");

		for (Element el3: docC3) {
			Node knode = (Node) el3.nextSibling();
			if (knode != null && knode.toString().length() > 2) {
				String nodeTxt = (!knode.toString().contains("href")) ? getwww_mod1.removeTag(knode.toString()) : "";
				//System.out.println(nodeTxt);
				content = content + " " + nodeTxt;
			}			
		}	
		} catch (Exception e) {
			//
			e.printStackTrace();
		} **/
		
		//System.out.println("content: \n"+ content +"\n");
		resultMap.put("content", content);
		//resultMap.put("author", author);
		
		//System.out.println("parseContent END");
		return resultMap;
	}
	
	public static Map<String, Object> getNewsDaylianReply(String url, int pageno, Map<String, Object> replyMap, OutputStreamWriter outFwriterReply) throws Exception {
		//System.out.println("* Start get reply =================================================================================================");
		String encoding = "euc_kr";
		String newsId = "";
		//String url = "http://m100.Daylian.com/svc/guest/list.html?article="+newsId;
		int getSum = 0;
		
		//if (pageno == 1) { getSum = 99; replyMap = new HashMap<String, Object>(); replyMap.put("newsId", newsId); }  // 초기값 초기MAP 설정  getSum=99 는 2페이지는 무조건 검색하기 위함
		
		url = url + "&pn="+Integer.toString(pageno);
		String rsbody = getwww.getURLBody(url,encoding);		
		replyMap = daylian.parseHtmlDaylianReply(rsbody, replyMap);
		getSum = Integer.parseInt(replyMap.get("getSum").toString());		
		searchNum += getSum;		
		//System.out.println("NewsID : "+newsId+"'th REPLY => " + "getting count : "+getSum);
		List<String> reply = (List<String>) replyMap.get("reply");
		//if (reply != null) { System.out.println("REPLY :"+reply.toString()); }
		
		//if (getSum > 0) { 
		//	daylian.getNewsDaylianReply(newsId, ++pageno, replyMap, outFwriterReply);  // #TODO 이번 페이지에 읽어온 댓글이 있을 경우 다음 댓글 페이지를 검색하도록 조정 -재귀호출 
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
	
	public static Map<String, Object> parseHtmlDaylianReply(String rsbody, Map<String, Object> replyMap) throws ParseException {
		Document doc = Jsoup.parse(rsbody);
		if (replyMap == null) { replyMap = new HashMap<String, Object>(); }
		
		List<String> reply = (ArrayList<String>) replyMap.get("reply");
		if (reply == null) { reply = new ArrayList<String>(); }
		
		/** get reply count **/
		String replyCount = "0";
		int getSum = 0;
		
		try {

			//<strong class="livereReply_good_count">2</strong></button></li>	
			//<strong class="livereReply_bad_count">2</strong>
			
			Elements docReply = doc.select("livereReplyEvaluation");
			//System.out.println("VVV count : "+docReply);			
			replyCount = docReply.text();		
			//System.out.println("REPLY Count : "+replyCount);		
		} catch (Exception e) {
			//
		}
		
		/** get REPLY List per Page_No v**/
		try {
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
			
			Elements docReplyList = doc.select(".comment_txt");
			ArrayList<String> replyComment = new ArrayList<String>();
			//System.out.println("REPLYsss : "+docReplyList2.text()+"\n");
			for (Element cp: docReplyList) {
				//System.out.println("REPLYsss:"+cp.text());
				replyComment.add(cp.text().toString());
			}
			
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
					reply.add("daylian"+ replyMap.get("newsId").toString() + "\t" + replyUser.get(i).toString() + "\t"   
						+ replyName.get(i) + "\t" + replyComment.get(i).toString() 
						+ "\t" +replyCommentRateAllow.get(i) + "\t" + replyCommentRateDeny.get(i));
					getSum = i;
				}
			}
			
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