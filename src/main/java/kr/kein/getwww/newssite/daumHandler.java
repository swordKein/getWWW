package kr.kein.getwww.newssite;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import kr.kein.getwww.getwww;
import kr.kein.getwww.util.FileUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class daumHandler extends Thread {
	private static final Logger logger = LoggerFactory.getLogger(daumHandler.class);
	
	public static final String siteCode = "daum";
	public static final String header = "\"siteCode\"	\"uuid\"	\"newsCate\"	\"rDate\"	\"sourceSite\"	\"author\"	\"title\"	\"link\"	\"content\"	\"newsOriginId\"	\"agreeCnt\"\n";
	public static final String headerReply = "\"siteCode\"	\"uuid\"	\"type\"	\"rDate\"	\"author\"	\"content\"	"
						+ "\"newsOriginId\"	\"parentId\"	\"commentId\"	\"agreeCnt\"	\"deniedCnt\"	\"childCnt\"      ";
	
	public static Map<String, Object> getDaum(String dtDate) {
		// 주어진 조건에 따라 파일 오픈
		Map<String, Object> oFileMap = FileUtils.fileOpener(getwww.tmpPath, siteCode, "news_Poli", dtDate, ".csv");
		OutputStreamWriter outFwriter = (OutputStreamWriter) oFileMap.get("fsw"); 
		
		Map<String, Object> rFileMap = FileUtils.fileOpener(getwww.tmpPath, siteCode, "news_poli_Reply", dtDate, ".csv");
		OutputStreamWriter outFwriterReply = (OutputStreamWriter) rFileMap.get("fsw"); 
				
		try {
			outFwriter.write(header);
		} catch (IOException e) {			e.printStackTrace();		}
		
		try {
			outFwriterReply.write(headerReply);
		} catch (IOException e) {			e.printStackTrace();		}
		
		System.out.println("### news header :: " + header);
		System.out.println("### news reply header :: " + headerReply);
		
		//String reqQuery = URLEncoder.encode(query, "UTF-8");
		
		/** 검색할 경우 사용
		//int pageNo = 1;
		//System.out.println("\n>>>>>>>>>>>>>>> Date = "+dtDate.toString()+" :: "+ pageNo + " 'th page ");
		//int endCnt = daumProcess.searchDaumNews(pageNo, dtDate.replace("-",""), outFwriter, outFwriterReply);
		
		// 페이지 표기가 61 ~ 70 처럼 표현되는데 뒷 부분 숫자가 0으로 끝나지 않으면 다음 페이지가 없다고 간주
		while (endCnt == ( pageNo * 10 )) {			
		**/
		
		String prcDate = "";
		Map<String, Object> resultMap = new HashMap<String, Object>();
		for(int i = 1; i < 5; i++) {
			System.out.println("\n>>>>>>>>>>>>>>> Date = "+dtDate.toString()+" :: "+ i + " 'th page ");
			try {
				prcDate = daumProcess.searchDaumNews(i, dtDate.replace("-",""), "poli", outFwriter, outFwriterReply);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		FileUtils.fileCloser(rFileMap);
		FileUtils.fileCloser(oFileMap);	
		
		return resultMap;
	}
}