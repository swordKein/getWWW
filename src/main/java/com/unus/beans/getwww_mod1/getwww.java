package com.unus.beans.getwww_mod1;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.unus.beans.getwww_mod1.blog.daumBlog;
import com.unus.beans.getwww_mod1.blog.naverBlog;
import com.unus.beans.getwww_mod1.newssite.chosun;
import com.unus.beans.getwww_mod1.newssite.daum;
import com.unus.beans.getwww_mod1.newssite.daumHandler;
import com.unus.beans.getwww_mod1.newssite.daylian;
import com.unus.beans.getwww_mod1.newssite.donga;
import com.unus.beans.getwww_mod1.newssite.hani;
import com.unus.beans.getwww_mod1.newssite.joongang;
import com.unus.beans.getwww_mod1.newssite.khan;
import com.unus.beans.getwww_mod1.newssite.kookmin;
import com.unus.beans.getwww_mod1.newssite.ohmynews;
import com.unus.beans.getwww_mod1.newssite.seoul;
import com.unus.beans.getwww_mod1.sns.facebookImpl;
import com.unus.beans.getwww_mod1.sns.twitterImpl;
import com.unus.beans.getwww_mod1.util.CommonUtils;
import com.unus.beans.getwww_mod1.util.DateUtils;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.utils.URIBuilder;
//import org.json.JSONArray;
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpException;
//import org.apache.http.HttpResponse;
//import org.apache.http.client.ClientProtocolException;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.config.AuthSchemes;
//import org.apache.http.client.config.CookieSpecs;
//import org.apache.http.client.config.RequestConfig;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.client.utils.URIBuilder;
//import org.apache.http.impl.client.HttpClientBuilder;
//import org.apache.http.util.EntityUtils;

//import com.supinan.custom.util.SupinanThreadPool;

public class getwww {
	public static int searchNum = 0;
	public static int tryCount = 0;
	
	// runningMode에 따라 테스트 시에는 console 출력,  파일 생성 시는 file로 지정

	public static String filePrefix;    // daylian, daum
	public static String runningMode;    /* console / file */
	public static String newsCate;     // (다음만 적용) 뉴스 카테고리, poli:정치, soci:사회, glob:국제, econ:경제
	public static String dest;			// searching ID or TXT
	public static String startDate;
	public static String endedDate;

	//public static String tmpPath = "/home/beans/tmp/";
	public static String tmpPath = "O:\\COLL\\tmp\\";
	//public static String filePath = "/home/beans/getin/";
	public static String filePath = "O:\\COLL\\getin\\";

	//public static String path = "O:\\COLL\\";
	
	public static void main(String[] args) throws Exception{		
		
		FileOutputStream fos = null;
		FileOutputStream fosReply = null;
		OutputStreamWriter outFwriter = null;
		OutputStreamWriter outFwriterReply = null;
		
		FileOutputStream sf1 = null;
		FileOutputStream tm1 = null;
		FileOutputStream of1 = null;
		OutputStreamWriter sf1Writer = null;
		OutputStreamWriter tm1Writer = null;
		OutputStreamWriter of1Writer = null;
		
		if (args == null || args.length < 3) { 
			System.out.println("Usage:  java -jar beans_getwww_mod1.jar %SITE_CODE %RUNNING_MODE %CATEGORY %START_DATE");
			System.out.println("		%SITE_CODE 		= daum / facebook / twitter / naverblog ");
			System.out.println("		%RUNNING_MODE		= file or console ");
			System.out.println("		%CATEGORY		= poli , soci , glob , econ , id=$id, txt=$txt");
			System.out.println("		(optional) %START_DATE		= 2014-01-01 ");
			System.out.println("		(optional) %END_DATE			= 2014-01-01 ");
			System.exit(0); 
		}
		
		filePrefix		= args[0]; 		// daylian, daum
		runningMode  	= args[1];    	/* console / file */
		// (다음일 경우) 뉴스 카테고리, poli:정치, soci:사회, glob:국제, econ:경제  // (페이스북일 경우) id=검색할 ID, txt=검색할문구
		if (args[2].indexOf("=") > 0) { 
			newsCate 	= args[2].split("=")[0];
			dest		= args[2].split("=")[1];
		} else {
			newsCate 		= args[2];
		}
		startDate 		= (args.length > 3) ? args[3] : "";    	// 시작일 yyyymmdd
		endedDate		= (args.length > 4) ? args[4] : "";    	// 종료일 yyyymmdd

		//  파일명과 검색조건에 쓰일 시작,종료일자 취득 	
		String etDate = DateUtils.getLocalDate();
		String dtDate = "";		
		if (!"".equals(startDate)) { 
			dtDate = startDate; 
		} else {
			dtDate = DateUtils.calculateDate(5, -1, etDate);
		}
		
		//String path = "/home/beans/temp_news/"+newsCate+"/";
		//String path = "o:\\test_local\\";
		//String path = "";
		String path = tmpPath;
		
		
		String startTag = "__From_"+dtDate;		
		String searchId = "";
		String searchStr = "";
		if ("id".equals(newsCate)) { searchId = dest; }
		else if ("txt".equals(newsCate)) { searchStr = dest;  startTag = "__" + searchStr.replace(" ", "_") + startTag; }
		else { searchStr = args[2]; }
		
		if ("file".equals(runningMode)
			&& !"twitter".equals(filePrefix)	
				) {
			
			String fileName = path +filePrefix+"_"+newsCate+startTag.replace("-","")+".csv";
			
			fos = new FileOutputStream(fileName);
			outFwriter = new OutputStreamWriter(fos, "UTF-8");

			if ("facebook".equals(filePrefix) || "joongang".equals(filePrefix)
					|| "daum".equals(filePrefix)
					) {
				String replyFileName = path +filePrefix+"_"+newsCate+startTag.replace("-","")+"_REPLY.csv";

				fosReply = new FileOutputStream(replyFileName);
				outFwriterReply = new OutputStreamWriter(fosReply, "UTF-8");				
			}
			
			/*
			File f4 = new File("O:\\test_local\\ttt.csv");
			String testTxt = CommonUtils.findFileEncoding(f4);
			System.out.println(" test Encoding File :: "+testTxt);
			*/
			
		}
		
		/** fileprefix에 다라 분기처리 **/
		switch(filePrefix) {		
			case "joongang" :
				dtDate = "20131001";				
				while (!dtDate.equals("20150101")) {
					for (int i=1; i<51; i++) {
						joongang.getNewsJoongang(i, dtDate, outFwriter, outFwriterReply);
					}
					dtDate = DateUtils.calculateDate(GregorianCalendar.DATE, 1, dtDate);
				}
			break;
			
			case "kookmin" :
				dtDate = "20140826";				
				while (!dtDate.equals("20150101")) {
					for (int i=1; i<51; i++) {
						kookmin.getNewsKookmin(i, dtDate, outFwriter, outFwriterReply);
					}
					dtDate = DateUtils.calculateDate(GregorianCalendar.DATE, 1, dtDate);
				}
			break;
			
			case "seoul" :
				dtDate = "20131001";				
				while (!dtDate.equals("20150101")) {
					for (int i=1; i<51; i++) {
						seoul.getNewsSeoul(i, dtDate, outFwriter, outFwriterReply);
					}
					dtDate = DateUtils.calculateDate(GregorianCalendar.DATE, 1, dtDate);
				}
			break;
			
			case "chosun" :
				for (int i=1950; i>0; i--) {
					chosun.getNewsChosun(i, outFwriter, outFwriterReply);
				}			
			break;
			
			case "daylian" :		
				for (int i=7230; i>0; i--) {
					daylian.getNewsDaylian(i, dtDate, outFwriter, outFwriterReply);
				}
			break;		
			
			case "donga" :
				dtDate = "20131001";				
				while (!dtDate.equals("20150101")) {
					for (int i=1; i<51; i++) {
						donga.getNewsdonga(i, dtDate, outFwriter, outFwriterReply);
					}
					dtDate = DateUtils.calculateDate(GregorianCalendar.DATE, 1, dtDate);
				}
			break;
			
			case "hani" :
				for (int i=7500; i>0; i--) {
					hani.getNewshani(i, null, outFwriter, outFwriterReply);
				}			
			break;
			
			case "khan" :
				for (int i=610; i>0; i--) {
					khan.getNewsKhan(i, null, outFwriter, outFwriterReply);
				}			
			break;
			
			case "ohmynews" :
				for (int i=117; i>0; i--) {
					ohmynews.getNewsOhmynews(i, null, outFwriter, outFwriterReply);
				}			
			break;
			
			case "daum" :
				//daumHandler.getDaum(dtDate.replace("-",""));
				
				String header = "\"siteCode\"	\"uuid\"	\"newsCate\"	\"rDate\"	\"sourceSite\"	\"author\"	\"title\"	\"link\"	\"content\"	\"newsOriginId\"	\"agreeCnt\"\n";
				String headerReply = "\"siteCode\"	\"uuid\"	\"type\"	\"rDate\"	\"author\"	\"content\"	"
									+ "\"newsOriginId\"	\"parentId\"	\"commentId\"	\"agreeCnt\"	\"deniedCnt\"	\"childCnt\"      ";
						
				if("file".equals(runningMode)) {
					outFwriter.write(header);
					outFwriterReply.write(headerReply);
				} else {
					System.out.println(header);
					System.out.println(headerReply);
				}
				
				String prcDate = "";
				while (!dtDate.equals(etDate)) {
					//dtDate = DateUtils.getChangeDate2(dtDate);
					//System.out.println("\n Date = "+dtDate.toString()+" :: "+DateUtils.calculateDate_(0, 1, dtDate));
					for (int i=1; i<5; i++) {

						System.out.println("\n>>>>>>>>>>>>>>> Date = "+dtDate.toString()+" :: "+dtDate + "      ::  "+ i + " 'th page ");
						prcDate = daum.getNewsDaum(i, dtDate.replace("-",""), outFwriter, outFwriterReply);
						//t1 = new ThreadSub();
						//t1.start(i, dtDate, outFwriter, outFwriterReply);
						//Thread.sleep(2000);
						prcDate.replace("-","");
						if (!prcDate.equals(dtDate.replace("-",""))) { i = 999; 
							//System.out.println(">> dtDate="+dtDate+ "&prcDate="+prcDate+"|");
						}
					}

					dtDate = DateUtils.calculateDate(0, 1, dtDate.replace("-", ""));
				}
				
			break;			
			case "facebook" :
				if("file".equals(runningMode)) {
					outFwriter.write("\"siteCode\"	\"uuid\"	\"dataType\"	\"createDate\"	\"authorId\"	\"content\"	\"likeCnt\"\n");
					outFwriterReply.write("\"siteCode\"	\"uuid\"	\"dataTypeReply\"	\"date\"	\"originContentId\"	\"commentId\"	\"commentMessage\"	\"commentAuthorId\"	\"cAuthorName\"	\"likeCnt\"\n");				
				}
				facebookImpl.getFacebookPosts(searchId, 0, outFwriter, outFwriterReply);
			break;		
			case "twitter" :
				String fileHeader="\"sitecode\"	\"uuid\"	\"create_date\"	\"owner\"	\"type\"	\"origin_id\"	\"content_id\"	\"retweetCount\"	\"favoriteCounte\"	\"isReply\"	\"reply_origin_id\"	\"reply_origin_owner\"	\"content\"";
				
				if("file".equals(runningMode)) {
					
				}				
				
				if (!"".equals(searchStr)) {
					if("file".equals(runningMode)) {
						String sf1Name = path + filePrefix+"_searched__" + searchStr.replace(" ", "_") + "__"+DateUtils.getLocalDateTime()+".csv";
						sf1 = new FileOutputStream(sf1Name);
						sf1Writer = new OutputStreamWriter(sf1, "UTF-8");
						sf1Writer.write(fileHeader+"\n");
					}
					
					twitterImpl.getSearchList(searchStr, sf1Writer, null); 
				}
				if (!"".equals(searchId)) {
					if("file".equals(runningMode)) {
						String tm1Name = path + filePrefix+"_timeline__"+DateUtils.getLocalDateTime()+".csv";
						tm1 = new FileOutputStream(tm1Name);
						tm1Writer = new OutputStreamWriter(tm1, "UTF-8");
						tm1Writer.write(fileHeader+"\n");			
					
						/* 오리지널 컨텐츠 파일 */
						String orgfName = path + filePrefix+"_ORG_timeline__"+DateUtils.getLocalDateTime()+".csv";
						of1 = new FileOutputStream(orgfName);
						of1Writer = new OutputStreamWriter(of1, "UTF-8");
						of1Writer.write(fileHeader+"\n");
					}
					twitterImpl.getTimeLines(searchId, null, tm1Writer, of1Writer);					
				}				
			break;		
			case "naverblog" :
				header = "\"siteCode\"	\"uuid\"	\"dataType\"	\"createDate\"	\"sourceSite\"	\"authorId\"	\"title\"	\"link\"	\"content\"	\"originId\"\n";
				if("file".equals(runningMode)) {
					outFwriter.write(header);
				} else {
					System.out.println(header);
				}
				/*
				String edDate = "";
				if (!"".equals(startDate)) {
					edDate = startDate;
				} else {
					edDate = DateUtils.getLocalDate2();
				}
				
				String stDate = DateUtils.calculateDate_(5, -30, edDate.replace("-",""));
				
				String searchTxt = dest;
				
				System.out.println("\nNaverBlog :: searching "+ newsCate + "  by  "+stDate+" ~ "+edDate);
				int startPageNo = 1;
				int cnt = naverBlog.getNaverBlog(searchTxt, startPageNo, stDate, edDate, outFwriter, outFwriterReply);
				*/
				
				String endDate = "";
				if (!"".equals(startDate)) {
					dtDate = startDate.replace("-", "");
				} else {
					dtDate = DateUtils.getLocalDate2();
					dtDate = dtDate.replace("-", "");
					dtDate = DateUtils.calculateDate2(5,  -1, dtDate);
					dtDate = dtDate.replace("-", "");
				}
				if (!"".equals(endedDate)) {
					endDate = endedDate.replace("-",  "");
				} else {
					endDate = DateUtils.getLocalDate();					
					endDate = endDate.replace("-", "");
					//endDate = DateUtils.calculateDate2(5,  1, endDate);
					//endDate = endDate.replace("-", "");
				}
				String searchTxt = dest;
				
				int cnt = 0;
				
				while (Integer.valueOf(dtDate) < Integer.valueOf(endDate)) {
					String toDate = dtDate.replace("-", "");

					System.out.println("dtDate :: "+dtDate + "	toDate :: "+ toDate);
							//+ "   :> since  endDate : "+endDate);
					
					cnt = naverBlog.getNaverBlog(searchTxt, 1, dtDate, toDate, outFwriter, outFwriterReply);
					System.out.println("cnt :: "+cnt);

					dtDate = DateUtils.calculateDate(0, 1, dtDate);
				}
				
			break;		
			case "daumblog" :
				if (!"".equals(startDate)) {
					dtDate = startDate;
				} else {
					dtDate = DateUtils.getLocalDate2();
				}
				
				String stDate = DateUtils.calculateDate_(5, -30, dtDate.replace("-",""));
				
				searchTxt = dest;
				
				System.out.println("\nDaumBlog :: searching "+ newsCate + "  by  "+stDate+" ~ "+dtDate);
				int startPageNo = 1;
				cnt = daumBlog.getDaumBlog(searchTxt, startPageNo, stDate, dtDate, outFwriter, outFwriterReply);
			break;	
			default:
				System.out.println("Not found item_code!");
			break;
		}
		
		try {
			if ("file".equals(runningMode)) {
				if (!"twitter".equals(filePrefix)) {					
					if ("facebook".equals(filePrefix) || "joongang".equals(filePrefix)) {					
						outFwriterReply.close();
					}
					outFwriter.close();
				} else {
					if ("txt".equals(newsCate)) {
						sf1Writer.flush();
						sf1Writer.close();
					}
					if ("id".equals(newsCate)) {
						tm1Writer.flush();
						tm1Writer.close();
						of1Writer.close();
						of1Writer.close();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//System.out.println("### => \n"+rsbody.toString());
		
	    //System.out.println("result :: " + results.toString());
	    //for (Integer key : prefs.keySet()) {
	    //    System.out.println("\tkey " + key + " = " + prefs.get(key));
	    //}
	    
	}	
			
	private static void sleep(int i) {
		// TODO Auto-generated method stub
		
	}

	public static String convertEuckrToUtf8(String inputText) throws UnsupportedEncodingException {		
		String convertedText = new String(new String(inputText.getBytes("euc_kr"),"UTF-8"));		
		return convertedText;		
	}
	
	public static String convertUtf8ToEuckr(String inputText) throws UnsupportedEncodingException {		
		String convertedText = new String(new String(inputText.getBytes("UTF-8"),"euc_kr"));		
		return convertedText;		
	}
	
	/* HTML Tag 제거 */ 
	public static String removeTag(String str){		
	  Matcher mat;  
	  try {
	  // script 처리 
	  Pattern script = Pattern.compile("<(no)?script[^>]*>.*?</(no)?script>",Pattern.DOTALL);  
	  mat = script.matcher(str);  
	  str = mat.replaceAll("");  
	  // style 처리
	  Pattern style = Pattern.compile("<style[^>]*>.*</style>",Pattern.DOTALL);  
	  mat = style.matcher(str);  
	  str = mat.replaceAll("");  
	  // tag 처리 
	  //Pattern tag = Pattern.compile("<(\"[^\"]*\"|\'[^\']*\'|[^\'\">])*>");
	  Pattern tag = Pattern.compile("\\<[^\\>]*\\>");
	  mat = tag.matcher(str);  
	  str = mat.replaceAll("");  
	  // ntag 처리 
	  Pattern ntag = Pattern.compile("<\\w+\\s+[^<]*\\s*>");  
	  mat = ntag.matcher(str);  
	  str = mat.replaceAll("");  
	  // entity ref 처리
	  Pattern Eentity = Pattern.compile("&[^;]+;");  
	  mat = Eentity.matcher(str);  
	  str = mat.replaceAll("");
	  // whitespace 처리 
	  Pattern wspace = Pattern.compile("\\s\\s+");  
	  mat = wspace.matcher(str); 
	  str = mat.replaceAll(""); 	          
	  
	  } catch (Exception e)
	  { 
		  //e.printStackTrace();
		  //
	  }
	  return removeTex(str) ;	  
	}
	
	public static String removeTex(String str) {
		// 중앙일보 태그 삭제
		String tmpStr = str;
		tmpStr = tmpStr.replace("'한국언론 뉴스허브'",""); 
		tmpStr = tmpStr.replace("뉴시스통신사.","");
		tmpStr = tmpStr.replace("무단전재-재배포 금지.","");
		tmpStr = tmpStr.replace("무단전재-재배포 금지","");
		tmpStr = tmpStr.replace("Copyright by JTBC,","");
		tmpStr = tmpStr.replace("DramaHouseJcontentHub Co., Ltd.", "");
		tmpStr = tmpStr.replace("All Rights Reserved.","");
		tmpStr = tmpStr.replace("<저작권자ⓒ   .>","");
		tmpStr = tmpStr.replace("저작권자ⓒ.","");
		tmpStr = tmpStr.replace("저작권자 © 뉴스1코리아,","");
		tmpStr = tmpStr.replace("저작권자 ⓒ","");		
		tmpStr = tmpStr.replace("무단전재 및 재배포 금지.","");
		tmpStr = tmpStr.replace("무단전재 및 재배포 금지","");
		tmpStr = tmpStr.replace("ⓒ CBS 노컷뉴스(www.nocutnews.co.kr)","");
		tmpStr = tmpStr.replace("CBS 노컷뉴스(www.nocutnews.co.kr)","");
		tmpStr = tmpStr.replace("JTBC 핫클릭","");
		tmpStr = tmpStr.replace("DramaHouseJcontentHub Co., Ltd.",""); 
		tmpStr = tmpStr.replace("\n","");
		tmpStr = tmpStr.replace("\r","");
		tmpStr = tmpStr.replace("/뉴스1","");
		tmpStr = tmpStr.replaceAll("[a-z0-9]*@newsis.com","");
		tmpStr = tmpStr.replaceAll("[a-z0-9]*@hani.co.kr","");
		tmpStr = tmpStr.replaceAll("[a-z0-9]*@hani.co","");
		tmpStr = tmpStr.replaceAll("[a-z0-9]*@hani","");
		tmpStr = tmpStr.replace("기자 .","기자");
		tmpStr = tmpStr.replace("기자광고","기자");
		tmpStr = tmpStr.replace("기자고","기자");
		tmpStr = tmpStr.replace("기사 더보기 광고","");
		tmpStr = tmpStr.replace(" 광고$","");
		tmpStr = tmpStr.replace("기자","기자 ");
		tmpStr = tmpStr.replaceAll("[a-z0-9]*@kmib.co.kr","");
		tmpStr = tmpStr.replaceAll("[a-z0-9]*@kmib.co","");
		tmpStr = tmpStr.replaceAll("[a-z0-9]*@kmib","");
		tmpStr = tmpStr.replaceAll("[a-z0-9]*@seoul.co.kr","");
		tmpStr = tmpStr.replaceAll("[a-z0-9]*@seoul.co","");
		tmpStr = tmpStr.replaceAll("[a-z0-9]*@seoul","");

		tmpStr = tmpStr.replace("\\t"," ");
		tmpStr = tmpStr.replace("\\r", " ");
		tmpStr = tmpStr.replace("\\n", " ");
		tmpStr = tmpStr.replace("\\^", "");
		tmpStr = tmpStr.replace("\\", "");
		tmpStr = tmpStr.replace("\"", "");
		return tmpStr;
	}
		
	public static String getHtmlBody(String reqHost, int reqPort, String reqPath, Map<String, String> reqparamMap, String encoding) throws HttpException, IOException {
		InputStream resultHtml;
		String result = "";
		
		//String reqUrl = reqHost + ":" + reqPort + reqPath + "?query=" + reqparamMap.get("reqQuery") + "&pageno=" + reqparamMap.get("reqPageno");
		String reqUrl = "";
		if(reqPort > 0) { 
			reqUrl = reqHost + ":" + reqPort + reqPath;
		} else {
			reqUrl = reqHost + reqPath;
		}
		if (reqparamMap != null) {
			String tag = (reqPath.contains("?")) ? "&" : "?";
			
			String pageString = "";
			reqUrl = reqUrl + pageString;
			if (reqparamMap.get("reqDtDate") != null && !reqparamMap.get("reqDtDate").equals("")) {
				pageString = tag + reqparamMap.get("reqDt") + "=" + reqparamMap.get("reqDtDate"); 
			}
			
			if (reqparamMap.get("reqPageno") != null 
					&& !"".equals(reqparamMap.get("reqPageno")) && !"0".equals(reqparamMap.get("reqPageno"))) { 
				//pageString = tag + reqparamMap.get("reqPageKey")+"=" + reqparamMap.get("reqPageno");
				pageString = pageString + "&" + reqparamMap.get("reqPageKey")+"=" + reqparamMap.get("reqPageno");
			}		
			if (reqparamMap.get("reqPageNo") != null 
					&& !"".equals(reqparamMap.get("reqPageNo")) && !"0".equals(reqparamMap.get("reqPageNo"))) { 
				pageString = pageString + "&" + reqparamMap.get("reqPage")+"=" + reqparamMap.get("reqPageNo");
			}	
			if (reqparamMap.get("reqQueryString") != null 
					&& !"".equals(reqparamMap.get("reqQueryString")) && !"0".equals(reqparamMap.get("reqQueryString"))) { 
				pageString = pageString + "&" + reqparamMap.get("reqQuery")+"=" + reqparamMap.get("reqQueryString");
			}
			if (reqparamMap.get("reqStDate") != null 
					&& !"".equals(reqparamMap.get("reqStDate")) && !"0".equals(reqparamMap.get("reqStDate"))) { 
				pageString = pageString + "&" + reqparamMap.get("reqSt")+"=" + reqparamMap.get("reqStDate");
			}
			if (reqparamMap.get("reqEdDate") != null 
					&& !"".equals(reqparamMap.get("reqEdDate")) && !"0".equals(reqparamMap.get("reqEdDate"))) { 
				pageString = pageString + "&" + reqparamMap.get("reqEd")+"=" + reqparamMap.get("reqEdDate");
			}
			
			if(reqUrl.indexOf("daum") > 0) {
				reqUrl = reqUrl + pageString + "&type=tit_cont";
			} else {
				reqUrl = reqUrl + pageString;
			}
		}
		if(!reqUrl.contains("like.daum.net")) {
			System.out.println("request URI: "+reqUrl); 
		}
		
		MultiThreadedHttpConnectionManager manager = new MultiThreadedHttpConnectionManager();
		try {
		HttpClient client = new HttpClient(manager);
		//HttpMethod get = new GetMethod("http://search.chosun.com/search/news.search?query=%EA%B9%80%EB%AC%B4%EC%84%B1&pageno=1");
		client.getParams().setParameter(ClientPNames.COOKIE_POLICY,CookiePolicy.BROWSER_COMPATIBILITY);
		client.getParams().setParameter("http.protocol.single-cookie-header", true);
		HttpMethod get = new GetMethod(reqUrl);
		//System.out.println("send URI: "+get.getURI());
		get.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false)); 
				
		int status = client.executeMethod(get);
		//System.out.println("HTTP response status : "+status);
		resultHtml = get.getResponseBodyAsStream();
				
		if("".equals(encoding)) { encoding = "UTF-8"; }
		result = IOUtils.toString(resultHtml,encoding);
		//System.out.println("HTTP result ::"+result);

		get.releaseConnection();		
		//client.getHttpConnectionManager().releaseConnection(null); 
		} catch (Exception e) { e.printStackTrace(); }
		
		return result;
	}
	
	public static String getHtmlUrlBody(String reqHost, int reqPort, String reqPath, Map<String, String> reqparamMap, String encoding) throws HttpException, IOException {
		InputStream resultHtml;
		String result = "";
		
		//String reqUrl = reqHost + ":" + reqPort + reqPath + "?query=" + reqparamMap.get("reqQuery") + "&pageno=" + reqparamMap.get("reqPageno");
		String tag = (reqPath.contains("?")) ? "&" : "?";
		
		String pageString = "";
		String reqUrl = reqHost + ":" + reqPort + reqPath + pageString;
		if (reqparamMap.get("reqDtDate") != null && !reqparamMap.get("reqDtDate").equals("")) {
			pageString = tag + reqparamMap.get("reqDt") + "=" + reqparamMap.get("reqDtDate"); 
		}
		
		if (reqparamMap.get("reqPageno") != null 
				&& !"".equals(reqparamMap.get("reqPageno")) && !"0".equals(reqparamMap.get("reqPageno"))) { 
			//pageString = tag + reqparamMap.get("reqPageKey")+"=" + reqparamMap.get("reqPageno");
			pageString = pageString + "#" + reqparamMap.get("reqPageKey")+"=" + reqparamMap.get("reqPageno");
		}		
		
		reqUrl = reqUrl + pageString + "&type=title";
		
		System.out.println("request URI: "+reqUrl);
		//getwww_mod1.getURL();
		String[] a = null;
		//renderWeb.main(a);
		
		try {
		/*
		MultiThreadedHttpConnectionManager manager = new MultiThreadedHttpConnectionManager();
		HttpClient client = new HttpClient(manager);
		//HttpMethod get = new GetMethod("http://search.chosun.com/search/news.search?query=%EA%B9%80%EB%AC%B4%EC%84%B1&pageno=1");
		client.getParams().setParameter(ClientPNames.COOKIE_POLICY,CookiePolicy.BROWSER_COMPATIBILITY);
		client.getParams().setParameter("http.protocol.single-cookie-header", true);
		HttpMethod get = new GetMethod(reqUrl);
		//System.out.println("send URI: "+get.getURI());
		get.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));
		*/

		URLConnection urlConnection = new URL(reqUrl).openConnection();
		urlConnection.setUseCaches(false);
		urlConnection.setDoOutput(true); // Triggers POST.
		urlConnection.setRequestProperty("accept-charset", encoding);
		urlConnection.setRequestProperty("content-type", "application/x-www-form-urlencoded");

		OutputStreamWriter writer = null;
		try {
		    //writer = new OutputStreamWriter(urlConnection.getOutputStream(), encoding);
		    //writer.write(query); // Write POST query string (if any needed).
			resultHtml = urlConnection.getInputStream();
		} finally {
		    if (writer != null) try { writer.close(); } catch (IOException logOrIgnore) {}
		}		
				
		//int status = client.executeMethod(get);
		//System.out.println("HTTP response status : "+status);
		//resultHtml = get.getResponseBodyAsStream();
				
		//if("".equals(encoding)) { encoding = "UTF-8"; }
		result = IOUtils.toString(resultHtml,encoding);
		//System.out.println("HTTP result ::"+result);

		//get.releaseConnection();		
		//client.getHttpConnectionManager().releaseConnection(null);

		} catch (Exception e) { e.printStackTrace(); }
		
		return result;
	}
		
	public static String getURLBody(String reqUrl, String encoding) throws HttpException, IOException {
		InputStream resultHtml;
		String result = "";		
		//System.out.println("request URI: "+reqUrl);
		try {
		MultiThreadedHttpConnectionManager manager = new MultiThreadedHttpConnectionManager();
		HttpClient client = new HttpClient(manager);
		client.getParams().setParameter(ClientPNames.COOKIE_POLICY,CookiePolicy.BROWSER_COMPATIBILITY);
		//HttpMethod get = new GetMethod("http://search.chosun.com/search/news.search?query=%EA%B9%80%EB%AC%B4%EC%84%B1&pageno=1");
		HttpMethod get = new GetMethod(reqUrl);
		//System.out.println("send URI: "+get.getURI());
		get.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false)); 
				
		int status = client.executeMethod(get);
		//System.out.println("HTTP response status : "+status);
		resultHtml = get.getResponseBodyAsStream();
				
		if("".equals(encoding)) { encoding = "UTF-8"; }
		result = IOUtils.toString(resultHtml,encoding);

		get.releaseConnection();
		//client.getHttpConnectionManager().releaseConnection(null); 
		} catch (Exception e) { e.printStackTrace(); }
		
		return result;
	}
	
	
	private static URI setURI(String scheme, String host, int port, String path, Map<String, String> paramMap) throws URISyntaxException {
		URIBuilder builder = new URIBuilder();
		builder.setScheme(scheme);
		builder.setHost(host);
		builder.setPort(port);
		builder.setPath(path.indexOf("/") == 0 ? path: "/"+path);
		
		if(paramMap != null && !paramMap.isEmpty()) {
			Set<String> keySet = paramMap.keySet();
			for (Iterator<String> iter = keySet.iterator(); iter.hasNext(); ) {
				String key = iter.next();
				String value = paramMap.get(key);
				builder.setParameter(key, value != null ? value: "");
			}
		}
		URI result = builder.build();
		return result;
	}
	/*
	private static void getURL() {
		String result = "";
		
		Platform.setImplicitExit(false);
		
		Platform.runLater(new Runnable() {
	        @Override
	        public void run() {
	        	WebView browser = new WebView();
	    		WebEngine webEngine = browser.getEngine();
	    		webEngine.load("http://media.daum.net/politics/all/?regdate=20141216#page=1&type=tit_cont");
	    				
	    		System.out.println("WebView : "+webEngine.getDocument().toString());
	        }
	   });
	} */
}