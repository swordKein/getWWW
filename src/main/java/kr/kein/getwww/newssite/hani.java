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
import org.jsoup.select.Elements;

public class hani {
	public static int searchNum = 0;
	public static int tryCount = 0;
	
	public static String prefix = "hani";
	public static String newsCate = "poli";
	/** 헤더 정보
	 * 구분코드 : TAB(\t)
	 * 수집사이트코드	UUID	카테고리2(정치=poli)	작성일	신문사코드	기자명	제목	본문링크	본문	뉴스ID
	 *
	 */
	public static final String reqHost = "http://www.hani.co.kr";
	public static final String reqPath = "/arti/politics/?type=0";
	public static final int reqPort = 80;

	public static void getNewshani(int pageno, String dtDate, OutputStreamWriter outFwriter, OutputStreamWriter outFwriterReply) throws Exception{
		//System.out.println("* Start =================================================================================================");
				
		//String query = "김무성";
		//String query = "";
		//String reqQuery = URLEncoder.encode(query, "UTF-8");
		int getSum = 0;		
		if ( pageno == 1 ) { getSum = 0; }
		
		String reqPageno = Integer.toString(pageno);
		Map<String,String> reqparam = new HashMap<String,String>();
		//reqparam.put("reqQuery", reqQuery);
		reqparam.put("reqPageKey","cline");
		reqparam.put("reqPageno", reqPageno);
				
		String rsbody = getwww.getHtmlBody(reqHost, reqPort, reqPath, reqparam, "UTF-8");
		//rsbody = convertEuckrToUtf8(rsbody);
		getSum = hani.parseHtmlhani(rsbody, outFwriter, outFwriterReply);
		searchNum += getSum;
		//System.out.println(getDate+"'th News "+ pageno + " PAGE getting count : "+getSum);
	}
	
	public static int parseHtmlhani(String rsbody, OutputStreamWriter outFwriter, OutputStreamWriter outFwriterReply) throws Exception {
		Document doc = Jsoup.parse(rsbody,"UTF-8");
		
		/** get TITLE & LINK List **/
		Elements docTitles = doc.select("body div[id=container] div[class=sorting-result-section] dl dt");
		//System.out.println("Titles: => \n"+docTitles.toString());
		ArrayList<String> rTitles = new ArrayList<String>();
		ArrayList<String> rLinks = new ArrayList<String>();
		for (Element et: docTitles) {		
			String eTitle = et.text().toString();
			String tmpText = eTitle;
			//eTitle = (eTitle.contains("[")) ? eTitle.substring(0,eTitle.lastIndexOf("[")) : eTitle.toString();
			rTitles.add(eTitle);
			
			String eLink = "";
			try {
				eLink = (tmpText.length() >0) ? reqHost+et.select("a").attr("href").toString()  : "" ;
				//System.out.println("title : "+eTitle  + " && link : " + eLink);
			} catch (Exception e) {
				//
			}
			rLinks.add(eLink);
		}
		
		/** get Date **/
		Elements docDate = doc.select("body div[id=container] div[class=sorting-result-section] dd[class=date]");
		//System.out.println("Dates : => \n"+docDate.toString());
		ArrayList<String> rEm = new ArrayList<String>();
		String eDate = "";
		for (Element el: docDate) {
			//rEm.add(el.attr("abs:href"));
			if (el != null && el.text() != null && el.text().toString().startsWith("20")) {
				eDate = el.text().toString().substring(0,11);
			}
			//System.out.println("date : "+eDate);
			rEm.add(eDate);
		}
		//System.out.println("Links: "+rLinks.toString());
		
				
		String tab = "\t";	
		int senum = 0;   // 뉴스의 SEQ
		int getSum = 0;  // 읽어온 뉴스 개수
		try {
		for (String se: rEm) {	
			String rDate = se.replace(".","");
			//System.out.print("rdate = "+rDate+"\n");
			//System.out.print("rLinks = "+rLinks.toString()+"\n");
			//if (se.replace(".","").trim().equals(getDate)) {
			if(rLinks.get(senum) != null && rLinks.get(senum).length() > 1) {
				//System.out.print("rLink = "+rLinks.get(senum).toString()+"\n");
				String encoding = "UTF-8";
				 
				Map<String, String> contentMap = hani.getNewshaniContent(rLinks.get(senum), encoding);
				String content = contentMap.get("content");
				String author = contentMap.get("author");
				if ("".equals(author)) { author = "unknown"; }
				String[] keyset = rLinks.get(senum).toString().split("/");
				String keyset2 = keyset[6].replaceAll(".html","");
				/** 기사 리스트 OUTPUT Form **/				
				
				/*String outText1 = "hani"+keyset2 + tab + rDate
						+ tab + author
						+ tab + getwww_mod1.removeTag(rTitles.get(senum)) + tab + rLinks.get(senum) + tab + content;
				*/
				
				String uuid = UUID.randomUUID().toString();  uuid = uuid.replace("-", "");
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
								
				//Map<String,Object> replyMap = hani.getNewshaniReply(keyset[9].replace(".html",""),1,null, outFwriterReply);
				//System.out.println("Reply : \n"+ replyMap.get("reply"));
				getSum++;				
			}
			senum++;
		}
		} catch (Exception e) { e.printStackTrace(); }
		return getSum;
	}
	
	public static Map<String, String> getNewshaniContent(String url, String encoding) throws Exception {
		String rsbody = getwww.getURLBody(url,encoding);
		Map<String,String> content = hani.parseHtmlhaniContent(rsbody);
		//System.out.println(url+"'th => \n"+ content.get("content"));		
		return content;
	}	
	
	public static Map<String, String> parseHtmlhaniContent(String rsbody) throws ParseException {
		Document doc = Jsoup.parse(rsbody);
		Map<String, String> resultMap = new HashMap<String,String>();
		String content = "";
		String author = "";
		
		/** get Content FirstLine **/
		try {
			Elements docC5 = doc.select("#article div[class=article-contents]");
			if (docC5 != null) { 
				content = docC5.text().toString(); 
				content = getwww.removeTex(content);
				if (content.length() > 30) {
					author = content.substring(content.length()-30,content.length());
					//System.out.println("AUT : "+author);
				}
				if (!"".equals(author) && author.contains(".") && author.contains("기자")) {
					if (author.length() - author.lastIndexOf(".")+2 > 0) 
					{ author = author.substring(author.lastIndexOf(".")+2,author.length()); }
					if (author.length() >= author.lastIndexOf("기자")+2) 
					{ author = author.substring(0,author.lastIndexOf("기자")+2); }
				}
			}
			//System.out.println("XXX:"+content);
			//System.out.println("AUTH:"+author);
		} catch (Exception e) {
			//
			e.printStackTrace();
		}
		
		//System.out.println("content: \n"+ content +" author : "+author+"\n");
		resultMap.put("content", content);
		resultMap.put("author", author);
		
		return resultMap;
	}
	
	public static Map<String, Object> getNewshaniReply(String newsId, int pageno, Map<String, Object> replyMap, OutputStreamWriter outFwriterReply) throws Exception {
		//System.out.println("* Start get reply =================================================================================================");
		String encoding = "euc_kr";
		
		String url = "http://m100.hani.com/svc/guest/list.html?article="+newsId;
		int getSum = 0;
		
		if (pageno == 1) { getSum = 99; replyMap = new HashMap<String, Object>(); replyMap.put("newsId", newsId); }  // 초기값 초기MAP 설정  getSum=99 는 2페이지는 무조건 검색하기 위함
		
		url = url + "&pn="+Integer.toString(pageno);
		String rsbody = getwww.getURLBody(url,encoding);		
		replyMap = hani.parseHtmlhaniReply(rsbody, replyMap);
		getSum = Integer.parseInt(replyMap.get("getSum").toString());		
		searchNum += getSum;		
		//System.out.println("NewsID : "+newsId+"'th REPLY => " + "getting count : "+getSum);
		List<String> reply = (List<String>) replyMap.get("reply");
		//if (reply != null) { System.out.println("REPLY :"+reply.toString()); }
		
		if (getSum > 0) { 
			hani.getNewshaniReply(newsId, ++pageno, replyMap, outFwriterReply);
			// # 이번 페이지에 읽어온 댓글이 있을 경우 다음 댓글 페이지를 검색하도록 조정 -재귀호출
		} else {
			// FILE WRITING
			/** 기사 댓글 리스트 OUTPUT Form**/
			if (reply != null && reply.size() > 0) { 
				for (String reTxt: reply) {
					//System.out.println("  RE: "+reTxt.toString());

					
					try { outFwriterReply.write(reTxt.toString()+"\n"); }
					catch (Exception e) { e.printStackTrace(); }
					
				}				
			}
		}
		
		return replyMap;
	}
	
	public static Map<String, Object> parseHtmlhaniReply(String rsbody, Map<String, Object> replyMap) throws ParseException {
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
					reply.add("hani"+ replyMap.get("newsId").toString() + "\t" + replyUser.get(i).toString() + "\t"   
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