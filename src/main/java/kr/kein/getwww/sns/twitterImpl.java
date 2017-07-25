package kr.kein.getwww.sns;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import kr.kein.getwww.*;

import twitter4j.*;
import twitter4j.auth.*;
import kr.kein.getwww.util.DateUtils;

public class twitterImpl {
	public static final String siteCode = "tw";
	public static final String dataType = "tw";	

	public static final String TWITTER_CONSUMER_KEY = "t3ySvN58bGxwXY0MpuvEqigDA";
	public static final String TWITTER_SECRET_KEY = "FM0Jyj5r2nFzhP2ui7aiz5PpsYyBnb0EPUivVf4JbWGZY5R4D9";
	public static String TWITTER_ACCESS_TOKEN = "2951549083-UTe5BtHFI4DH0sQxP7W2UaatNsa4yMTY8PVEdWp";
	public static String TWITTER_ACCESS_TOKEN_SECRET = "uW9n0ej2wrrLj29WdDL4xqPPIu0oT53Al3MFnmKzw2sL5";
	
	public static List<Status> getSearchList(String str, OutputStreamWriter sf1Writer, OutputStreamWriter tm1Writer) {
		List<Status> statuses = new ArrayList<Status>();
		String tab = "\t";
		
		Twitter twitter = getTwitterInstance();
		System.out.println("Twitter searching :: "+str);
		try {
		    Query query = new Query(str);
		    QueryResult result;
		    int j = 0;
		    do {
		        query.setCount(110);
		        query.setSince("2015-08-15");
		        result = twitter.search(query);
		        List<Status> tweets = result.getTweets();
		        
			    System.out.println("QueryResult :: "+result.getQuery()+ " :: "+result.getCount());
			    
			    //if (j < 2) {
			    int i = 0;
		        for (Status status : tweets) {
		        	String tType = "TW";
		    		if (status.getRetweetedStatus() != null) { 		tType = "RT";	} 
		    		else if (status.getInReplyToStatusId() > 0) {	tType = "RP";	}
		    		
		    		String uuid = UUID.randomUUID().toString();  uuid = uuid.replace("-", "");
		    		
		    		String lt = "\"";
		    		String rt = "\"";
		    		
		    		String outText = 
		    				lt + "tw" + rt + tab
		    				+ lt + uuid + rt + tab
		    				+ lt + DateUtils.getDateTime(status.getCreatedAt()) + rt + tab
		    				+ lt + status.getUser().getScreenName()+" ("+status.getUser().getName()+")" + rt + tab
		    				+ lt + tType + rt + tab
			      			+ lt + (status.getRetweetedStatus() != null ? status.getRetweetedStatus().getId() : 0) + rt + tab
			      			+ lt + status.getId() + rt + tab
			      			+ lt + status.getRetweetCount() + rt + tab
			      			+ lt + status.getFavoriteCount() + rt + tab			      			
			      			+ lt + (status.getInReplyToStatusId() > 0 ? true : false)  + rt + tab
			      			+ lt + status.getInReplyToStatusId() + rt + tab
			      			+ lt + status.getInReplyToScreenName() + rt + tab  
			      			+ lt + getwww.removeTag(status.getText()) + rt ;
		    		
		              statuses.add(status);
		              if("file".equals(getwww.runningMode)) {
		            	  try {
							sf1Writer.write(outText+"\n");
						} catch (IOException e) {	e.printStackTrace();	}
		              } else {
		            	  System.out.println(outText);
		              }
				        
		            i++;
		        }
		        Thread.sleep(5000);
		        j++;
		        
		        
		        //System.out.println("next :: ?? "+result.nextQuery().toString());
			    //}
		    } while ((query = result.nextQuery()) != null);
			//} while (j<1);
		    //System.exit(0);
		} catch (Exception te) {
		    te.printStackTrace();
		    System.out.println("Failed to search tweets: " + te.getMessage());
		    //System.exit(-1);
		}
	
	return statuses;
	}
	
	
	public static int getFollowersCount(String userId) {
		int followerPersons = 0;
		Twitter twitter = getTwitterInstance();
		try {
			if("".equals(userId)) { userId = "YooJeongBok"; }	
			long followerCursor = -1;
			IDs followerIds;
			do
			{
				followerIds = twitter.getFollowersIDs(userId, followerCursor);
				//System.out.println("Followers ::" + followerIds.getIDs().length );
				//ResponseList<User> followers = twitter.lookupUsers(followerIds.getIDs());
				followerPersons = followerPersons + followerIds.getIDs().length;              
			        
				//for(long follower : followerIds.getIDs()) { System.out.println (follower); }
			} while((followerCursor = followerIds.getNextCursor()) != 0);                
		} catch (TwitterException te) {
		    te.printStackTrace();
		    System.out.println("Failed to search tweets: " + te.getMessage());
		} finally {
			 System.out.println("All Followers ::" + followerPersons);
		}	
	return followerPersons;
	}
	
	

	public static List<Status> getTimeLines(String userId, OutputStreamWriter sf1Writer, OutputStreamWriter tm1Writer, OutputStreamWriter of1Writer) {
		List<Status> statuses = new ArrayList<Status>();
		String tab = "\t";
		Twitter twitter = getTwitterInstance();
		System.out.println("twitter :: "+twitter.toString());
		int allSize = 0;

		try {
		    for(int i=1; i<10000; i++) {
		    	Paging page = new Paging(i, 20);
		    	List<Status> statuses1 = twitter.getUserTimeline(userId, page);
		    	
		    	for (Status status : statuses1) {		    		
		    		String tType = "TW";
		    		if (status.getRetweetedStatus() != null) { 		tType = "RT";	} 
		    		else if (status.getInReplyToStatusId() > 0) {	tType = "RP";	
		    		
		    		try {
		    		ResponseList<Status> rtStatus = twitter.getRetweets(status.getInReplyToStatusId());
		    		} catch (Exception e) {
		    			e.printStackTrace(); System.out.println(" :: getRetweets  error :: "+e.getCause());
		    		}
		    		/*
		    		if (rtStatus != null && rtStatus.size() > 0 ) { System.out.println(" :: Retweet Original :: "+rtStatus.toString()); }
		    		
		    		Status rStatus = twitter.showStatus(status.getInReplyToStatusId());
		    		if (rStatus != null) { System.out.println(" :: Status Original :: "+rtStatus.toString()); }
		    		
		    		*/
		    		
		    		}
		    		String uuid = UUID.randomUUID().toString();  uuid = uuid.replace("-", "");
		    		
		    		String outText = 
		    			"tw" + tab
		    			+ uuid + tab
		       			+ DateUtils.getDateTime(status.getCreatedAt()) + tab
		       			+ status.getUser().getScreenName()+" ("+status.getUser().getName()+")" + tab
		      			+ tType + tab +
		      			+ (status.getRetweetedStatus() != null ? status.getRetweetedStatus().getId() : 0) + tab
		       			+ status.getId() + tab
		       			+ status.getRetweetCount() + tab
		      			+ status.getFavoriteCount() + tab			      			
		      			+ (status.getInReplyToStatusId() > 0 ? true : false)  + tab
		       			+ status.getInReplyToStatusId() + tab
		       			+ status.getInReplyToScreenName() + tab  
		       			+ getwww.removeTag(status.getText());
	              
		              statuses.add(status);		              
		              if("file".equals(getwww.runningMode)) {
		            	  try {
							tm1Writer.write(outText+"\n");
						} catch (IOException e) {	e.printStackTrace();	}
		              } else {
		            	  System.out.println(outText);
		              }
		              /*
		              if(status.getRetweetCount() > 0) {
		              	for (Status rt : twitter.getRetweets(status.getId())) {
		              		String srt = rt.toString();
		              		//System.out.println(rt.toString());
		              		//System.out.println(srt);
		              		
	                       System.out.println("@" + tab
	                    		    + status.getId() + tab
	                        		+ rt.getId() + tab
	                    		    + rt.getUser().getId() + " (" + rt.getUser().getScreenName() + ")"
	                    		   );
	                    }
		              } */
		              
		        }       		    	 
		    	//statuses.addAll(statuses1);
		    	int ssize = statuses1.size();
		    	allSize += ssize;
		    	
		    	System.out.println("added TimeLines :: " + i + " 'th :: " + ssize );

		    	Thread.sleep(5000);
		    	
		    	if (ssize == 0) { break; } 
		    	
		    }
		} catch (Exception te) {
		    te.printStackTrace();
		    System.out.println("Failed to search tweets: " + te.getMessage());
		} finally {
			System.out.println("TimeLines size :: "+allSize);
		}
		return statuses;
	}
	
	

//
	public static List<Status> getStatuses(String userId) {
		List<Status> statuses = new ArrayList<Status>();
		String tab = "\t";
		Twitter twitter = getTwitterInstance();
		System.out.println("twitter :: "+twitter.toString());
		int allSize = 0;

		try {
			if("".equals(userId)) { userId = "YooJeongBok"; }
		    for(int i=1; i<10000; i++) {
		    	Paging page = new Paging(i, 100);
		    	List<Status> statuses1 = twitter.getRetweetsOfMe();
		    	
		    	for (Status status : statuses1) {
		    		String rtStatusId = Long.toString(status.getInReplyToStatusId());
		    		rtStatusId.replace("-1","");
		    		
		              System.out.println(
			       			DateUtils.getDateTime(status.getCreatedAt()) + tab
			       			+ status.getAccessLevel() + tab
			       			+ status.getId() + tab
			       			+ status.getInReplyToStatusId() + " (" + status.getInReplyToScreenName() + ")" + tab
			       			//+ status.getFavoriteCount() + tab
			       			+ status.getRetweetedStatus() + tab
			       			//+ status.getSource() + tab
			       			+ status.isRetweet() + tab
			      			+ status.getRetweetCount() + tab
			       			+ status.getUser().getName()+" ("+status.getUser().getScreenName()+")" + tab
			       			+ status.getText().replace("\n", " ")
			       			);
		              statuses.add(status);
		              	              
		        }       		    	 
		    	//statuses.addAll(statuses1);
		    	int ssize = statuses1.size();
		    	allSize += ssize;
		    	System.out.println("added TimeLines ::" + ssize );
		    	if (ssize == 0) { break; } 
		    }
		} catch (TwitterException te) {
		    te.printStackTrace();
		    System.out.println("Failed to search tweets: " + te.getMessage());
		} finally {
			System.out.println("TimeLines size :: "+allSize);
		}
		return statuses;
	}
	
	public static List<Status> getRetwitters(long id) {
		List<Status> statuses = new ArrayList<Status>();
		String tab = "\t";
		Twitter twitter = getTwitterInstance();
		System.out.println("twitter :: "+twitter.toString());
		int allSize = 0;
		long cursor = -1;

		try {
			if(id < 0) { id = 389301479; }
		    //for(int i=1; i<100; i++) {
		    	//Paging page = new Paging(i, 100);
		    	IDs statuses1 = twitter.getRetweeterIds(id, cursor);
		    	
		    	System.out.println(" IDs :: "+statuses1);
		    	/*for (Status status : statuses1) {
		              System.out.println(
			       			DateUtils.getDateTime(status.getCreatedAt()) + tab
			       			+ status.getId() + tab
			       			+ status.isRetweet() + tab
			      			+ status.getRetweetCount() + tab
			       			+ status.getUser().getName()+" ("+status.getUser().getScreenName()+")" + tab
			       			+ status.getText().replace("\n", " ")
			       			);
		              statuses.add(status);
		        }        		    	 
		    	//statuses.addAll(statuses1);
		    	int ssize = statuses1.size();
		    	allSize += ssize;
		    	System.out.println("added TimeLines ::" + ssize );
		    	*/
		    	//if (ssize == 0) { break; } 
		   // }
		} catch (TwitterException te) {
		    te.printStackTrace();
		    System.out.println("Failed to search tweets: " + te.getMessage());
		} finally {
			System.out.println("TimeLines size :: "+allSize);
		}
		return statuses;
	}
	
	
	public static AccessToken getTwitterAuth() throws TwitterException, IOException {		
		// 이 팩토리인스턴스는 재이용가능하고 thread safe합니다.
	    Twitter twitter = TwitterFactory.getSingleton();
	    twitter.setOAuthConsumer(TWITTER_CONSUMER_KEY,TWITTER_SECRET_KEY);
	    RequestToken requestToken = twitter.getOAuthRequestToken();
	    AccessToken accessToken = null;
	    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	    while (null == accessToken) {
	      System.out.println("Open the following URL and grant access to your account:");
	      System.out.println(requestToken.getAuthorizationURL());
	      System.out.print("Enter the PIN(if aviailable) or just hit enter.[PIN]:");
	      String pin = br.readLine();
	      try{
	         if(pin.length() > 0){
	           accessToken = twitter.getOAuthAccessToken(requestToken, pin);
	         }else{
	           accessToken = twitter.getOAuthAccessToken();
	         }
	      } catch (TwitterException te) {
	        if(401 == te.getStatusCode()){
	          System.out.println("Unable to get the access token.");
	        }else{
	          te.printStackTrace();
	        }
	      }
	    }
	    //향후에 참조용으로 accessToken 을 지속시킨다.
	    //storeAccessToken(twitter.verifyCredentials().getId() , accessToken);
	    TWITTER_ACCESS_TOKEN = accessToken.toString();
	    
	    return accessToken;
	}
	
	public static Twitter getTwitterInstance() {
		//ConfigurationBuilder cb = null;
		//TwitterFactory tf = null;
		//AccessToken authToken = null;
		Twitter twitter = null;
		
		try {
			
	    	AccessToken accesstoken = new AccessToken(TWITTER_ACCESS_TOKEN, TWITTER_ACCESS_TOKEN_SECRET);

	    	twitter = new TwitterFactory().getInstance();

			twitter.setOAuthConsumer(TWITTER_CONSUMER_KEY, TWITTER_SECRET_KEY);	
			
			twitter.setOAuthAccessToken(accesstoken);
			
		    System.out.println("Twitter getAccessToken : "+twitter.verifyCredentials().getId()+" : "+twitter.getOAuthAccessToken());
		    //Status status = twitter.updateStatus(args[0]);
		} catch (Exception ea) {
			ea.printStackTrace();
		}
		/*
		try {
			cb = new ConfigurationBuilder();
			cb.setDebugEnabled(true)
			    .setOAuthConsumerKey(GetTwitter.TWITTER_CONSUMER_KEY)
			    .setOAuthConsumerSecret(GetTwitter.TWITTER_SECRET_KEY)
			    .setOAuthAccessToken(GetTwitter.TWITTER_ACCESS_TOKEN)
			    .setOAuthAccessTokenSecret(GetTwitter.TWITTER_ACCESS_TOKEN_SECRET);
			tf = new TwitterFactory(cb.build());
			twitter = tf.getInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
		return twitter;
	}
}