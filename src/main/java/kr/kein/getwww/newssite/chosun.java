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

public class chosun {
	public static int searchNum = 0;
	public static int tryCount = 0;

	public static String prefix = "chosun";
	//public static String getwww_mod1.newsCate = getwww_mod1.newsCate;
	/** 헤더 정보
	 * 구분코드 : TAB(\t)
	 * 수집사이트코드	UUID	카테고리2(정치=poli)	작성일	신문사코드	기자명	제목	본문링크	본문	뉴스ID
	 * 
	 * 댓글 헤더 정보
	 * 수집사이트코드	UUID	카테고리2(정치=poli	작성일	컨텐츠ID	댓글ID	댓글	댓글작성자ID	댓글작성자명		좋아요개수
	 */
	
	public static void getNewsChosun(int pageno, OutputStreamWriter outFwriter, OutputStreamWriter outFwriterReply) throws Exception{
		//System.out.println("* Start =================================================================================================");
		
		String reqHost = "http://news.chosun.com";
		int reqPort = 80;
		String reqPath = "/svc/list_in/list.html?catid=2";
		
		//String query = "김무성";
		//String query = "";
		//String reqQuery = URLEncoder.encode(query, "UTF-8");
		int getSum = 0;		
		if ( pageno == 1 ) { getSum = 99; }
		
		String reqPageno = Integer.toString(pageno);
		Map<String,String> reqparam = new HashMap<String,String>();
		//reqparam.put("reqQuery", reqQuery);
		reqparam.put("reqPageKey","pn");
		reqparam.put("reqPageno", reqPageno);
		
		String rsbody = getwww.getHtmlBody(reqHost, reqPort, reqPath, reqparam, "euc_kr");
		//rsbody = convertEuckrToUtf8(rsbody);
		getSum = chosun.parseHtmlChosun(rsbody, outFwriter, outFwriterReply);
		searchNum += getSum;
		//System.out.println(getDate+"'th News "+ pageno + " PAGE getting count : "+getSum);
	}
	
	public static int parseHtmlChosun(String rsbody, OutputStreamWriter outFwriter, OutputStreamWriter outFwriterReply) throws Exception {
		Document doc = Jsoup.parse(rsbody,"UTF-8");
		
		/** get TITLE List **/
		Elements docTitles = doc.select("div#list_area dl dt span[id=tit]");
		//System.out.println("Titles: => \n"+docTitles.toString());
		ArrayList<String> rTitles = new ArrayList<String>();
		for (Element et: docTitles) {		
			rTitles.add(et.text());
			//System.out.println("title : "+et.text().toString());
		}
//		for (String st: rTitles) {
//			System.out.println("Title: "+st.toString());
//		}
		
		/** get Link URL List **/
		Elements docLinks = doc.select("div#list_area dl dt span[id=tit] a[href]");
		ArrayList<String> rLinks = new ArrayList<String>();
		for (Element el: docLinks) {
			rLinks.add(el.attr("abs:href"));
			//System.out.println("link : "+el.attr("abs:href").toString());
		}
		//System.out.println("Links: "+rLinks.toString());
		
		/** get New DATE List **/
		Elements docEm = doc.select("div#list_area dl dt span[id=date]");
		//System.out.println("date : "+docEm.toString());
		ArrayList<String> rEm = new ArrayList<String>();
		for (Element et: docEm) {
			if (et.text().length() > 3) {
				String dtText = et.text().substring(0, 10).replace( ".","-").trim();
				rEm.add(dtText);
			//System.out.println("date : "+dtText);
			}
		}
		
		/** get New Author List **/
		Elements docAuthor = doc.select("div#list_area dl dd.ref");
		//System.out.println("authors : "+docAuthor.toString());
		
		ArrayList<String> rAu = new ArrayList<String>();
		for (Element ea: docAuthor) {
			String eaText = ea.text().toString();
			//System.out.println("ORG author : "+eaText);
			if (eaText.length() < 2) { eaText="unknown"; } 
			else { 
				if (eaText.contains("|")) {
					if (eaText.lastIndexOf(" | ") > 0) {
						eaText = eaText.substring(0,eaText.lastIndexOf(" | "));
					} else {
						eaText = "unknown";
					}
				}
			}
			//System.out.println("author : "+eaText);
			rAu.add(eaText);			
		}
				
		String tab = "\t";	
		int senum = 0;   // 뉴스의 SEQ
		int getSum = 0;  // 읽어온 뉴스 개수
		for (String se: rEm) {	
			String rDate = se.replace(".","");
			//System.out.print("rdate = "+rDate+"\n");
			//System.out.print("rLinks = "+rLinks.toString()+"\n");
			//if (se.replace(".","").trim().equals(getDate)) {
			if(rLinks.get(senum) != null && rLinks.get(senum).length() > 3) {
				if(!rLinks.get(senum).toString().startsWith("http://premium")) {
					String encoding = "UTF-8";
					if (rLinks.get(senum).toString().startsWith("http://news")) { encoding = "euc_kr"; }
						String content = chosun.getNewsChosunContent(rLinks.get(senum), encoding).get("content");
						String[] keyset = rLinks.get(senum).toString().split("/");
						
						/** 기사 리스트 OUTPUT Form **/
						content =  (getwww.removeTag(content));		
						/*
						String outText1 = "chosun"+keyset[9].replace(".html","") + tab + rDate
								+ tab + getwww_mod1.removeTag(rAu.get(senum))
								+ tab + getwww_mod1.removeTag(rTitles.get(senum)) + tab + rLinks.get(senum) + tab + getwww_mod1.removeTag(content);
						*/
						String uuid = UUID.randomUUID().toString();  uuid = uuid.replace("-", "");
						String author = getwww.removeTag(rAu.get(senum));
						String outText = 
								prefix 
								+ tab + uuid 
								+ tab + getwww.newsCate 
								+ tab + rDate 
								+ tab + prefix 
								+ tab + author
								+ tab + getwww.removeTag(rTitles.get(senum)) 
								+ tab + rLinks.get(senum) 
								+ tab + getwww.removeTag(content)
								+ tab + keyset[9].replace(".html","");
						
						//System.out.println(outText+"\n");
						// #TODO .write 실제 파일에 쓰기
						if("file".equals(getwww.runningMode)) {	
							outFwriter.write(outText+"\n");
						} else {
							System.out.println(outText);
						}
						
						Map<String,Object> replyMap = chosun.getNewsChosunReply(keyset[9].replace(".html",""),1,null, outFwriterReply);
						//System.out.println("Reply : \n"+ replyMap.get("reply"));
						getSum++;
				}
			//}
			senum++;
			}
		}
		return getSum;
	}
	
	public static Map<String, String> getNewsChosunContent(String url, String encoding) throws Exception {
		String rsbody = getwww.getURLBody(url,encoding);
		Map<String,String> content = chosun.parseHtmlChosunContent(rsbody);
		//System.out.println(url+"'th => \n"+ content.get("content"));
		
		return content;
	}
	
	public static Map<String, String> parseHtmlChosunContent(String rsbody) throws ParseException {
		Document doc = Jsoup.parse(rsbody);
		Map<String, String> resultMap = new HashMap<String,String>();
		
		/** get Content First line **/
		String content = "";
		try {
			
			Element docContent1 = doc.select(".par div#player0").last();	
			
			if (docContent1 != null && docContent1.nextSibling() != null) {
				//System.out.println("xxx:"+docContent1.toString());
				//System.out.println("xxx:"+docContent1.nextSibling().toString());
				content = docContent1.nextSibling().toString(); 
			}  else {
				//System.out.println("size doc1 : "+content.length());
			}
			if (content.length() < 2) {
				Element docContent2 = doc.select("body p").last();
				//System.out.println("xxx:"+docContent2.text().toString());
				content = docContent2.text().toString();
			}						
		} catch (Exception e) {
			//
			e.printStackTrace();
		}
		
		/** get Content List **/
		try {
		Elements docContent2 = doc.select(".par br");

		for (Element el2: docContent2) {
			Node knode = (Node) el2.nextSibling();
			if (knode != null && knode.toString().length() > 2) {
				//System.out.println("ZZZ:"+knode);
				content = content + " " + knode.toString().replace("<br>", "");
			}			
		}	
				
		/** get BIZ Content List **/
		//System.out.println("size befor BIZ process : "+content.length() + ":: cotent => \n"+content);
		if (content.length() < 5 || content.startsWith("입력")) {
			content = "";
			try {
				Elements docContent3 = doc.select(".article br");
				//System.out.println("YYY:"+docContent3);					
				for (Element el3: docContent3) {
					Node mnode = (Node) el3.nextSibling();					
					if (mnode != null && mnode.toString().length() > 2) {
						//System.out.println("YYY:"+mnode.toString());
						content = content + " " + mnode.toString().replace("<br>", "");
					}			
				}
			} catch (Exception e) {
				//
				e.printStackTrace();
			}
		}
		
		if (content.length() < 25 || content.startsWith("입력")) {
			content = "";
			Element docContent4 = doc.select("body #img_pop0").last();
			Node node5 = docContent4.nextSibling();
			while (node5 != null && node5.nextSibling() != null && !node5.toString().startsWith("<!--")) {				
				String nodeTxt = node5.toString().replaceAll("<br>","").replaceAll("<!-- google_ad_section_end -->","")
						.replaceAll("<!-- video layer setting e-->","")
						.replaceAll("<!-- photo setting e-->","");
				if (nodeTxt.length() > 1) {
					//System.out.println(nodeTxt);
					content = content + " " + nodeTxt;
				}
				node5 = node5.nextSibling();				
			}
		}
		
		} catch (Exception e) {
			//
		}
		//System.out.println("content: \n"+content);
		resultMap.put("content", content);
		
		return resultMap;
	}
	
	public static Map<String, Object> getNewsChosunReply(String newsId, int pageno, Map<String, Object> replyMap, OutputStreamWriter outFwriterReply) throws Exception {
		//System.out.println("* Start get reply =================================================================================================");
		String encoding = "euc_kr";
		
		String url = "http://m100.chosun.com/svc/guest/list.html?article="+newsId;
		int getSum = 0;
		
		if (pageno == 1) { getSum = 99; replyMap = new HashMap<String, Object>(); replyMap.put("newsId", newsId); }  // 초기값 초기MAP 설정  getSum=99 는 2페이지는 무조건 검색하기 위함
		
		url = url + "&pn="+Integer.toString(pageno);
		String rsbody = getwww.getURLBody(url,encoding);		
		replyMap = chosun.parseHtmlChosunReply(rsbody, replyMap);
		getSum = Integer.parseInt(replyMap.get("getSum").toString());		
		searchNum += getSum;		
		//System.out.println("NewsID : "+newsId+"'th REPLY => " + "getting count : "+getSum);
		List<String> reply = (List<String>) replyMap.get("reply");
		//if (reply != null) { System.out.println("REPLY :"+reply.toString()); }
		
		if (getSum > 0) { 
			chosun.getNewsChosunReply(newsId, ++pageno, replyMap, outFwriterReply);  // #TODO 이번 페이지에 읽어온 댓글이 있을 경우 다음 댓글 페이지를 검색하도록 조정 -재귀호출 
		} else {
			// FILE WRITING
			/** 기사 댓글 리스트 OUTPUT Form**/
			if (reply != null && reply.size() > 0) { 
				for (String reTxt: reply) {
					// #TODO .write 실제 파일에 쓰기
					if("file".equals(getwww.runningMode)) {	
						try { outFwriterReply.write(reTxt.toString()+"\n"); }
						catch (Exception e) { e.printStackTrace(); }
					} else if("console".equals(getwww.runningMode)) {
						System.out.println("  RE: "+reTxt.toString());
					}
				}				
			}
		}
		
		return replyMap;
	}

	public static Map<String, Object> parseHtmlChosunReply(String rsbody, Map<String, Object> replyMap) throws ParseException {
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
					
					String tab = "\t";
					String uuid = UUID.randomUUID().toString();  uuid = uuid.replace("-", "");
					String nid = replyMap.get("newsId").toString();
					String newsDate = " ";
					String cmmtId = nid + String.valueOf(i);
					String cmmtContent = replyComment.get(i).toString();
					String author = replyUser.get(i).toString();
					String authorName = replyName.get(i);
					String likeCount = replyCommentRateAllow.get(i);
					
					String outReply = 
							prefix 
							+ tab + uuid 
							+ tab + getwww.newsCate 
							+ tab + newsDate
							+ tab + nid
							+ tab + cmmtId
							+ tab + getwww.removeTag(cmmtContent)
							+ tab + author
							+ tab + authorName
							+ tab + likeCount;			

					/** 댓글 리스트 OUTPUT Formatting **/
					/*
					reply.add("chosun"+ replyMap.get("newsId").toString() + "\t" + replyUser.get(i).toString() + "\t"   
						+ replyName.get(i) + "\t" + replyComment.get(i).toString() 
						+ "\t" +replyCommentRateAllow.get(i) + "\t" + replyCommentRateDeny.get(i));
					*/
					reply.add(outReply);
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