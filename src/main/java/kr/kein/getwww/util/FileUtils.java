package kr.kein.getwww.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;

import kr.kein.getwww.getwww;

public class FileUtils {
	public static Map<String, Object> fileOpener(String path, String siteCode, String type, String startDate, String ext) {
		Map<String, Object> fileMap = new HashMap<String,Object>();
		String fileOrg = path + siteCode + "_" + type + "__" + startDate + "." + ext;
		FileOutputStream file = null;
		OutputStreamWriter outF = null;
		try {
			file = new FileOutputStream(fileOrg);
			outF = new OutputStreamWriter(file, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		fileMap.put("fos",file);
		fileMap.put("fsw", outF);
		return fileMap;		
	}
	
	public static void fileCloser(Map<String, Object> fileMap) {
		int rtCode = 0;
		try {
			if(fileMap != null) {
				FileOutputStream file = (FileOutputStream) fileMap.get("fos");
				OutputStreamWriter outF = (OutputStreamWriter) fileMap.get("fsw");
			
				outF.flush();
				outF.close();
				file.close();				
			}
		} catch (Exception e) { e.printStackTrace(); rtCode = -1; }
		
		//return rtCode;
	}
	
	public static int copyFile(String cfName) {
		int resultCode = 0;
		File cf = new File(getwww.tmpPath+cfName);
		
		//String[] nfNms = cfName.split("\\.");
		//String nfTag = nfNms[nfNms.length-1];
		 try {
			File df = new File(getwww.filePath+cfName);
			if(df.exists()) {
				df.delete();
			}
				
			copyWithChannels(cf,df,false);					 
			
		 } catch (Exception e) { e.printStackTrace(); }	 	
		 return resultCode;
	}

	private static void ensureTargetDirectoryExists(File aTargetDir){
	    if(!aTargetDir.exists()){
	      aTargetDir.mkdirs();
	    }
	  }

	public static void copyWithChannels(File aSourceFile, File aTargetFile, boolean aAppend) {
	    System.out.println("Copying files with channels.");
	    ensureTargetDirectoryExists(aTargetFile.getParentFile());
	    FileChannel inChannel = null;
	    FileChannel outChannel = null;
	    FileInputStream inStream = null;
	    FileOutputStream outStream = null;
	    try{
	      try {
	        inStream = new FileInputStream(aSourceFile);
	        inChannel = inStream.getChannel();
	        outStream = new  FileOutputStream(aTargetFile, aAppend);        
	        outChannel = outStream.getChannel();
	        long bytesTransferred = 0;
	        //defensive loop - there's usually only a single iteration :
	        while(bytesTransferred < inChannel.size()){
	          bytesTransferred += inChannel.transferTo(0, inChannel.size(), outChannel);
	        }
	      }
	      finally {
	        //being defensive about closing all channels and streams 
	        if (inChannel != null) inChannel.close();
	        if (outChannel != null) outChannel.close();
	        if (inStream != null) inStream.close();
	        if (outStream != null) outStream.close();
	      }
	    }
	    catch (FileNotFoundException ex){
	      System.out.println("File not found: " + ex.getCause());
	    }
	    catch (IOException ex){
	      System.out.println("IO exception :: "+ex.getCause());
	    }
	  }
		  	
}