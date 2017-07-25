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

public class joongang {
	public static int searchNum = 0;
	public static int tryCount = 0;

	public static String prefix = "joongang";
	public static String newsCate = "poli";
	/** 헤더 정보
	 * 구분코드 : TAB(\t)
	 * 수집사이트코드	UUID	카테고리2(정치=poli)	작성일	신문사코드	기자명	제목	본문링크	본문	뉴스ID
	 */

	public static void getNewsJoongang(int pageno, String dtDate, OutputStreamWriter outFwriter, OutputStreamWriter outFwriterReply) throws Exception{
		//System.out.println("* Start =================================================================================================");
		
		String reqHost = "http://article.joins.com";
		int reqPort = 80;
		String reqPath = "/news/list/list.asp?ctg=10";
		
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
		
		reqparam.put("reqDt","dt");
		reqparam.put("reqDtDate", dtDate);
		
		String rsbody = getwww.getHtmlBody(reqHost, reqPort, reqPath, reqparam, "UTF-8");
		//rsbody = convertEuckrToUtf8(rsbody);
		getSum = joongang.parseHtmlJoongang(rsbody, outFwriter, outFwriterReply);
		searchNum += getSum;
		//System.out.println(getDate+"'th News "+ pageno + " PAGE getting count : "+getSum);
	}
	
	public static int parseHtmlJoongang(String rsbody, OutputStreamWriter outFwriter, OutputStreamWriter outFwriterReply) throws Exception {
		Document doc = Jsoup.parse(rsbody,"UTF-8");
		
		/** get TITLE List **/
		Elements docTitles = doc.select("div#news_list a[class=title_cr]");
		//System.out.println("Titles: => \n"+docTitles.toString());
		ArrayList<String> rTitles = new ArrayList<String>();
		for (Element et: docTitles) {		
			rTitles.add(et.text());
			//System.out.println("title : "+et.text().toString());
		}
		
		/** get Link URL List **/
		Elements docLinks = doc.select("div#news_list a[class=title_cr]");
		//System.out.println("Links: => \n"+docLinks.toString());
		ArrayList<String> rLinks = new ArrayList<String>();
		for (Element el: docLinks) {
			rLinks.add(el.attr("abs:href"));
			//System.out.println("link : "+el.attr("abs:href").toString());
		}
		//System.out.println("Links: "+rLinks.toString());
		
		/** get New DATE List **/
		Elements docEm = doc.select("div#news_list span[class=date]");
		//System.out.println("date : "+docEm.toString());
		ArrayList<String> rEm = new ArrayList<String>();
		for (Element et: docEm) {
			if (et.text().length() > 3) {
				String dtText = et.text().substring(0, 10).replace( ".","-").trim();
				rEm.add(dtText);
				//System.out.println("date : "+dtText);
			}
		}
		
		/** get News Author List **/
		Elements docAuthor = doc.select("div#news_list a[class=read_cr]");
		//System.out.println("authors : "+docAuthor.toString());
		String atag1 = "【";
		String atag2 = "】";
		String atag3 = "[";
		String atag4 = "]";
		ArrayList<String> rAu = new ArrayList<String>();
		for (Element ea: docAuthor) {
			String eaText = "";
			if (ea != null) {
				eaText = ea.text().toString();		
				if (ea.text().length() > 60) { eaText = eaText.substring(0,60); } else { eaText = eaText.substring(0,eaText.length()); }
				String tmpText = eaText;
				//System.out.println("ORG author : "+eaText);
				
				if (eaText.contains(atag1) && eaText.contains(atag2)) {
					eaText = eaText.substring(eaText.indexOf(atag1)+1,eaText.indexOf(atag2));
					//System.out.println("test1    "+eaText);
					if (!eaText.contains("기자")) {
						if (tmpText.indexOf("기자") > tmpText.indexOf(atag2)) {
							eaText = tmpText.substring(tmpText.indexOf(atag2)+1,tmpText.indexOf("기자")+2);
						} else {
							eaText = "unknown"; 
						}
					} 
				} 			
				else if (eaText.contains(atag3) && eaText.contains(atag4)) {
					eaText = eaText.substring(eaText.indexOf(atag3)+1,eaText.indexOf(atag4));
					//System.out.println("test2    "+eaText);
					if (!eaText.contains("기자")) {
						if (tmpText.indexOf("기자") > tmpText.indexOf(atag4)) {
							eaText = tmpText.substring(tmpText.indexOf(atag4),tmpText.indexOf("기자")+2);
						}
					} 
				} else {
					eaText = "unknown";
				}			
			} else {
				eaText = "unknown";
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
			if(rLinks.get(senum) != null && rLinks.get(senum).length() > 1) {
				//System.out.print("rLink = "+rLinks.get(senum).toString()+"\n");
				String encoding = "UTF-8";
				String content = joongang.getNewsJoongangContent(rLinks.get(senum), encoding).get("content");
				String[] keyset = rLinks.get(senum).toString().split("/");
				String keyset2 = keyset[5].substring(keyset[5].indexOf("=")+1,keyset[5].indexOf("&"));
				/** 기사 리스트 OUTPUT Form **/
				
				content =  (getwww.removeTag(content));		
				/* String outText1 = "joongang"+keyset2 + tab + rDate
						+ tab + getwww_mod1.removeTag(rAu.get(senum))
						+ tab + getwww_mod1.removeTag(rTitles.get(senum)) + tab + rLinks.get(senum) + tab + getwww_mod1.removeTag(content);
				*/
				
				String uuid = UUID.randomUUID().toString();  uuid = uuid.replace("-", "");
				String author = getwww.removeTag(rAu.get(senum));
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
				
				//Map<String,Object> replyMap = joongang.getNewsJoongangReply(keyset[9].replace(".html",""),1,null, outFwriterReply);
				//System.out.println("Reply : \n"+ replyMap.get("reply"));
				getSum++;				
			}
			senum++;
		}
		return getSum;
	}
	
	public static Map<String, String> getNewsJoongangContent(String url, String encoding) throws Exception {
		String rsbody = getwww.getURLBody(url,encoding);
		Map<String,String> content = joongang.parseHtmlJoongangContent(rsbody);
		//System.out.println(url+"'th => \n"+ content.get("content"));		
		return content;
	}	
	
	public static Map<String, String> parseHtmlJoongangContent(String rsbody) throws ParseException {
		Document doc = Jsoup.parse(rsbody);
		Map<String, String> resultMap = new HashMap<String,String>();
		String content = "";
		
		/** get Content List **/
		try {
		Elements docContent2 = doc.select(".article_content br");
		//System.out.println("XXX:"+docContent2.toString());		
		for (Element el2: docContent2) {
			Node knode = (Node) el2.nextSibling();
			String nodeText = "";
			if (knode != null) {
				nodeText = getwww.removeTex(knode.toString());
				if (nodeText.indexOf("&lt;") > 0) { nodeText.replace("&lt;","<"); } 
				if (nodeText.indexOf("&gt;") > 0) { nodeText.replace("&gt;",">"); }
			}
			
			if (nodeText != null && nodeText.toString().length() > 4) {
				//String nodeText = knode.toString().replace("&lt;","<").replace("&gt;",">");
				content = content + " " + getwww.removeTag(nodeText);
				//System.out.println("ZZZ:"+nodeText);
			}			
		}	
		//System.out.println("==> " + getwww_mod1.removeTex(content) + "\n");
		//System.out.println("==> " + content);
		
		} catch (Exception e) {
			//
			e.printStackTrace();
		}
		//System.out.println("content: \n"+ content +"\n");
		resultMap.put("content", content);
		
		return resultMap;
	}
	
	public static Map<String, Object> getNewsJoongangReply(String newsId, int pageno, Map<String, Object> replyMap, OutputStreamWriter outFwriterReply) throws Exception {
		//System.out.println("* Start get reply =================================================================================================");
		String encoding = "euc_kr";
		
		String url = "http://m100.joongang.com/svc/guest/list.html?article="+newsId;
		int getSum = 0;
		
		if (pageno == 1) { getSum = 99; replyMap = new HashMap<String, Object>(); replyMap.put("newsId", newsId); }  // 초기값 초기MAP 설정  getSum=99 는 2페이지는 무조건 검색하기 위함
		
		url = url + "&pn="+Integer.toString(pageno);
		String rsbody = getwww.getURLBody(url,encoding);		
		replyMap = joongang.parseHtmlJoongangReply(rsbody, replyMap);
		getSum = Integer.parseInt(replyMap.get("getSum").toString());		
		searchNum += getSum;		
		//System.out.println("NewsID : "+newsId+"'th REPLY => " + "getting count : "+getSum);
		List<String> reply = (List<String>) replyMap.get("reply");
		//if (reply != null) { System.out.println("REPLY :"+reply.toString()); }
		
		if (getSum > 0) { 
			joongang.getNewsJoongangReply(newsId, ++pageno, replyMap, outFwriterReply);
			// # 이번 페이지에 읽어온 댓글이 있을 경우 다음 댓글 페이지를 검색하도록 조정 -재귀호출
		} else {
			// FILE WRITING
			/** 기사 댓글 리스트 OUTPUT Form**/
			if (reply != null && reply.size() > 0) { 
				for (String reTxt: reply) {
					try { 
						if("file".equals(getwww.runningMode)) {					

							outFwriterReply.write(reTxt.toString()+"\n");
						} else if("console".equals(getwww.runningMode)) {
							System.out.println(reTxt.toString());
						}
					}
					catch (Exception e) { e.printStackTrace(); }
					
				}				
			}
		}
		
		return replyMap;
	}
	
	public static Map<String, Object> parseHtmlJoongangReply(String rsbody, Map<String, Object> replyMap) throws ParseException {
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
					reply.add("joongang"+ replyMap.get("newsId").toString() + "\t" + replyUser.get(i).toString() + "\t"   
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