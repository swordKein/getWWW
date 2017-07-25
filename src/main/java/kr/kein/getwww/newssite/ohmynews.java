package kr.kein.getwww.newssite;

import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import kr.kein.getwww.getwww;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

public class ohmynews {
	public static int searchNum = 0;
	public static int tryCount = 0;
	
	public static String prefix = "ohmynews";
	public static String newsCate = "poli";
	/** 헤더 정보
	 * 구분코드 : TAB(\t)
	 * 수집사이트코드	UUID	카테고리2(정치=poli)	작성일	신문사코드	기자명	제목	본문링크	본문	뉴스ID
	 *
	 */
	public static String reqHost = "http://www.ohmynews.com";
	public static int reqPort = 80;
	public static String reqPath = "/NWS_Web/ArticlePage/Total_Article.aspx?PAGE_CD=C0400";


	public static void getNewsOhmynews(int pageno, String dtDate, OutputStreamWriter outFwriter, OutputStreamWriter outFwriterReply) throws Exception{
		//System.out.println("* Start =================================================================================================");
		

		//String query = "김무성";
		//String query = "";
		//String reqQuery = URLEncoder.encode(query, "UTF-8");
		int getSum = 0;		
		if ( pageno == 1 ) { getSum = 0; }
		
		String reqPageno = Integer.toString(pageno);
		Map<String,String> reqparam = new HashMap<String,String>();
		//reqparam.put("reqQuery", reqQuery);
		reqparam.put("reqPageKey","pageno");
		reqparam.put("reqPageno", reqPageno);
				
		String rsbody = getwww.getHtmlBody(reqHost, reqPort, reqPath, reqparam, "UTF-8");
		//rsbody = convertEuckrToUtf8(rsbody);
		getSum = ohmynews.parseHtmlOhmynews(rsbody, outFwriter, outFwriterReply);
		searchNum += getSum;
		//System.out.println(getDate+"'th News "+ pageno + " PAGE getting count : "+getSum);
	}
	
	public static int parseHtmlOhmynews(String rsbody, OutputStreamWriter outFwriter, OutputStreamWriter outFwriterReply) throws Exception {
		Document doc = Jsoup.parse(rsbody,"UTF-8");
		
		/** get TITLE & LINK List **/
		Elements docTitles = doc.select(".news_list dt a");
		//System.out.println("Titles: => \n"+docTitles.toString());
		ArrayList<String> rTitles = new ArrayList<String>();
		ArrayList<String> rLinks = new ArrayList<String>();
		for (Element et: docTitles) {
			String eTitle = et.text().toString();
			String eLink = et.attr("href");
			if (!"".equals(eLink)) { eLink = reqHost + eLink; }
			//System.out.println("TITLE : => "+ eTitle + " & LINK : "+eLink);
			
			rTitles.add(eTitle);
			rLinks.add(eLink);
		}
		
		/** get DATE List **/
		Elements docDates = doc.select(".news_list p[class=source] span");
		//System.out.println("Dates: => \n"+docDates.toString());
		ArrayList<String> rEm = new ArrayList<String>();
		String eDate = "";
		for (Element eD: docDates) {
			eDate = eD.nextSibling().toString();
			if (eDate.length() > 8) { eDate = "20"+eDate.substring(0,8).replace(".","-");}
			//System.out.println("DATE : => "+ eDate);
			
			rEm.add(eDate);
		}
		
		/** get AUTHOR List **/
		Elements docAuthors = doc.select(".news_list p[class=source] a");
		//System.out.println("Authors: => \n"+docAuthors.toString());
		ArrayList<String> rAuthors = new ArrayList<String>();
		for (Element eA: docAuthors) {
			String eAuthor = eA.text();
			//System.out.println("AUTHOR : "+eAuthor);
			
			rAuthors.add(eAuthor);
		}		
				
		String tab = "\t";	
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
				String content = ohmynews.getNewsOhmynewsContent(rLinks.get(senum), encoding).get("content");
				String[] keyset = rLinks.get(senum).toString().split("/");
				//System.out.println("keyset0__ = "+keyset[5].toString());
				String[] keyset0 = keyset[5].split("=");
				//System.out.println("keyset0 = "+keyset0.toString());
				String keyset2 = keyset0[1];
				/** 기사 리스트 OUTPUT Form **/
				
				content =  (getwww.removeTag(content));		
				/* String outText1 = "ohmynews"+keyset2 + tab + rDate
						+ tab + (!"".equals(rAuthors.get(senum)) ? rAuthors.get(senum) : "unknown")
						+ tab + getwww_mod1.removeTag(rTitles.get(senum)) + tab + rLinks.get(senum) + tab + getwww_mod1.removeTag(content);
				*/
				
				String uuid = UUID.randomUUID().toString();  uuid = uuid.replace("-", "");
				String author = (!"".equals(rAuthors.get(senum)) ? rAuthors.get(senum) : "unknown");
				String outText = 
						prefix 
						+ tab + uuid 
						+ tab + newsCate 
						+ tab + rDate 
						+ tab + prefix 
						+ tab + author
						+ tab + getwww.removeTag(rTitles.get(senum)) 
						+ tab + rLinks.get(senum) 
						+ tab + getwww.removeTag(content)
						+ tab + keyset2;
				
				//System.out.println(outText+"\n");

				if("file".equals(getwww.runningMode)) {					

					outFwriter.write(outText+"\n");
				} else if("console".equals(getwww.runningMode)) {
					System.out.println(outText);
				}
				
				//Map<String,Object> replyMap = Ohmynews.getNewsOhmynewsReply(keyset[9].replace(".html",""),1,null, outFwriterReply);
				//System.out.println("Reply : \n"+ replyMap.get("reply"));

				getSum++;				
			}
			senum++;
		}
		return getSum;
	}
	
	public static Map<String, String> getNewsOhmynewsContent(String url, String encoding) throws Exception {
		String rsbody = getwww.getURLBody(url,encoding);
		Map<String,String> content = ohmynews.parseHtmlOhmynewsContent(rsbody);
		//System.out.println(url+"'th => \n"+ content.get("content"));		
		return content;
	}	
	
	public static Map<String, String> parseHtmlOhmynewsContent(String rsbody) throws ParseException {
		Document doc = Jsoup.parse(rsbody);
		Map<String, String> resultMap = new HashMap<String,String>();
		String content = "";
		
		/** get Content FirstLine **/
		try {
		Elements docC1 = doc.select(".at_contents");
		content = docC1.text();
		//System.out.println("XXX:"+content);		
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
		
		/** get Content List **/
		try {
		Elements docC3 = doc.select("#contents div[class=article_txt] br");

		for (Element el3: docC3) {
			Node knode = (Node) el3.nextSibling();
			if (knode != null && knode.toString().length() > 2) {
				String nodeTxt = (!knode.toString().contains("href")) ? getwww.removeTag(knode.toString()) : "";
				//System.out.println(nodeTxt);
				content = content + " " + nodeTxt;
			}			
		}	
		} catch (Exception e) {
			//
			e.printStackTrace();
		}
		
		//System.out.println("content: \n"+ content +"\n");
		resultMap.put("content", content);
		
		return resultMap;
	}
	
	public static Map<String, Object> getNewsOhmynewsReply(String newsId, int pageno, Map<String, Object> replyMap, OutputStreamWriter outFwriterReply) throws Exception {
		//System.out.println("* Start get reply =================================================================================================");
		String encoding = "euc_kr";
		
		String url = "http://m100.Ohmynews.com/svc/guest/list.html?article="+newsId;
		int getSum = 0;
		
		if (pageno == 1) { getSum = 99; replyMap = new HashMap<String, Object>(); replyMap.put("newsId", newsId); }  // 초기값 초기MAP 설정  getSum=99 는 2페이지는 무조건 검색하기 위함
		
		url = url + "&pn="+Integer.toString(pageno);
		String rsbody = getwww.getURLBody(url,encoding);		
		replyMap = ohmynews.parseHtmlOhmynewsReply(rsbody, replyMap);
		getSum = Integer.parseInt(replyMap.get("getSum").toString());		
		searchNum += getSum;		
		//System.out.println("NewsID : "+newsId+"'th REPLY => " + "getting count : "+getSum);
		List<String> reply = (List<String>) replyMap.get("reply");
		//if (reply != null) { System.out.println("REPLY :"+reply.toString()); }
		
		if (getSum > 0) { 
			ohmynews.getNewsOhmynewsReply(newsId, ++pageno, replyMap, outFwriterReply);
			// # 이번 페이지에 읽어온 댓글이 있을 경우 다음 댓글 페이지를 검색하도록 조정 -재귀호출
		} else {
			// FILE WRITING
			/** 기사 댓글 리스트 OUTPUT Form**/
			if (reply != null && reply.size() > 0) { 
				for (String reTxt: reply) {
					//System.out.println("  RE: "+reTxt.toString());

					
					try {
						if("file".equals(getwww.runningMode)) {
							outFwriterReply.write(reTxt.toString()+"\n"); 
						} else if ("console".equals(getwww.runningMode)) {
							System.out.println(reTxt.toString()+"\n");
						}
					} catch (Exception e) { e.printStackTrace(); }
					
				}				
			}
		}
		
		return replyMap;
	}
	
	public static Map<String, Object> parseHtmlOhmynewsReply(String rsbody, Map<String, Object> replyMap) throws ParseException {
		Document doc = Jsoup.parse(rsbody);
		if (replyMap == null) { replyMap = new HashMap<String, Object>(); }
		
		List<String> reply = (ArrayList<String>) replyMap.get("reply");
		if (reply == null) { reply = new ArrayList<String>(); }
		
		/** get reply count **/
		String replyCount = "0";
		int getSum = 0;
		
		try {
			Elements docReply = doc.select(".com_count a");
			//System.out.println("VVV count : "+docReply);			
			replyCount = docReply.text();		
			//System.out.println("REPLY Count : "+replyCount);		
		} catch (Exception e) {
			//
		}
		
		/** get REPLY List per Page_No **/
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
					
					/** 댓글 리스트 OUTPUT Formatting **/
					reply.add("ohmynews"+ replyMap.get("newsId").toString() + "\t" + replyUser.get(i).toString() + "\t"   
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