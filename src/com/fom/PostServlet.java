package com.fom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.Friend;
import facebook4j.Post;
import facebook4j.PostUpdate;
import facebook4j.Reading;
import facebook4j.ResponseList;
import facebook4j.auth.AccessToken;
import facebook4j.internal.org.json.JSONArray;
import facebook4j.internal.org.json.JSONException;
import facebook4j.internal.org.json.JSONObject;

import com.google.gdata.client.youtube.YouTubeQuery;
import com.google.gdata.client.youtube.YouTubeService;
import com.google.gdata.data.Category;
import com.google.gdata.data.extensions.Rating;
import com.google.gdata.data.media.mediarss.MediaThumbnail;
import com.google.gdata.data.youtube.VideoEntry;
import com.google.gdata.data.youtube.VideoFeed;
import com.google.gdata.data.youtube.YouTubeMediaContent;
import com.google.gdata.data.youtube.YouTubeMediaGroup;
import com.google.gdata.data.youtube.YtStatistics;
import com.google.gdata.util.ServiceException;
import com.google.gson.Gson;

public class PostServlet extends HttpServlet {
    private static final long serialVersionUID = 4179545353414298791L;
    private static final String YOUTUBE_URL = "http://gdata.youtube.com/feeds/api/videos";
    private static final String YOUTUBE_EMBEDDED_URL = "http://www.youtube.com/v/";
    
//    private Map<String,Integer> FBLikeMap = new HashMap<String,Integer>();
//	private Map<String,Float> YTLikeMap = new HashMap<String,Float>();
//	private Map<String,Integer> statusAndCommentRatingMap = new HashMap<String,Integer>();
    
    EvaluationObject evalObj = new EvaluationObject();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //request.setCharacterEncoding("UTF-8");
       String mood = request.getParameter("mood");
      //System.out.println("the message to be posted is "+mood);
        Facebook facebook = (Facebook) request.getSession().getAttribute("facebook");
        facebook.setOAuthPermissions("ads_management, ads_read, create_event, create_note, email, export_stream, friends_about_me, friends_actions.books, friends_actions.music, friends_actions.news, friends_actions.video, friends_activities, friends_birthday, friends_education_history, friends_events, friends_games_activity, friends_groups, friends_hometown, friends_interests, friends_likes, friends_location, friends_notes, friends_online_presence, friends_photo_video_tags, friends_photos, friends_questions, friends_relationship_details, friends_relationships, friends_religion_politics, friends_status, friends_subscriptions, friends_videos, friends_website, friends_work_history, manage_friendlists, manage_notifications, manage_pages, photo_upload, publish_actions, publish_stream, read_friendlists, read_insights, read_mailbox, read_page_mailboxes, read_requests, read_stream, rsvp_event, share_item, sms, status_update, user_about_me, user_actions.books, user_actions.music, user_actions.news, user_actions.video, user_activities, user_birthday, user_education_history, user_events, user_friends, user_games_activity, user_groups, user_hometown, user_interests, user_likes, user_location, user_notes, user_online_presence, user_photo_video_tags, user_photos, user_questions, user_relationship_details, user_relationships, user_religion_politics, user_status, user_subscriptions, user_videos, user_website, user_work_history, video_upload, xmpp_login");
           evalObj.emptyAll();    
//        System.out.println("Facebook: "+facebook);
           Connection conn = null;
           Statement stmt = null;
        try 
        {
        	/**
        	 * The search method has been deprecated... So not using it... If u wanna use it you gotta use API V 1.0
        	 */
        	/**
        	 * Try to get the comments too... very necessary to do this.... See like_info and comment_info from the "stream" table
        	 * Also check these: message_tags, permalink, type
        	 */
        	/**
        	 * an important point to note that query will be changed slightly we would be required to first get all those posts that have a YT link associated
        	 * next we need to filter them based on the comments and status messages. Check is their a way that you can rank the links. So that the links that have a 
        	 * closer resemblance to mood are displayed first.
        	 */
        
        	String queryStream = "SELECT actor_id, action_links, created_time, message, post_id, like_info "
        				  + "FROM stream "
        				  + "WHERE source_id IN "
        				  	+ "(SELECT uid2 "
        				  	+ "FROM friend "
        				  	+ "WHERE uid1= me()) "
        				  	+ "and strpos(message, 'https://www.youtube.com/watch?v=') >= 0 "
        				  + "ORDER BY created_time DESC LIMIT 100 OFFSET 0"; 
        	
        	/**
        	 * One really good idea would be to initially query all the friends and store their username and password in a has map.
        	 */
        	JSONArray jsonStreamArray2 = facebook.executeFQL(queryStream);
        	//processResults(jsonStreamArray2, facebook);
        	Class.forName("com.mysql.jdbc.Driver").newInstance();
        	 conn = DriverManager.getConnection(
   	               "jdbc:mysql://try.cq7ncj7mhv6r.us-east-1.rds.amazonaws.com:3306/fom", "root", "qwerty123");
        	
//        	Connection conn = DriverManager.getConnection(
// 	               "jdbc:mysql://localhost:3306/fom", "root", "passwd");
        	//String mood = "sleepy";
        	stmt = conn.createStatement();
        	String strSelect = "select mood_syn from mood_synonyms where mood = '"+mood+"'";
        	System.out.println("qqqqqqq" + strSelect);
        	ResultSet rset = stmt.executeQuery(strSelect);
        	List<FQLExtractThread> threadList = new ArrayList<FQLExtractThread>(); 
        	while(rset.next()) 
	        {   // Move the cursor to the next row
	            String mood_syn = rset.getString("mood_syn");
	            FQLExtractThread evaluateThread = new FQLExtractThread(mood_syn, jsonStreamArray2, facebook, evalObj);
	            threadList.add(evaluateThread);
	            evaluateThread.start();
	        }
        	for( FQLExtractThread fql : threadList )
        	{
        		fql.join();
        	}
        	
        	System.out.println("YTLikeMap " + evalObj.YTLikeMap);
        	System.out.println("FBLikeMap " + evalObj.FBLikeMap);
        	System.out.println("statusAndCommentRatingMap " + evalObj.statusAndCommentRatingMap);
        	
        	Map<String, Integer> sortedYTLikeMap = sortByValuesFloat(evalObj.YTLikeMap);
        	Map<String, Integer> sortedFBLikeMap = sortByValuesInteger(evalObj.FBLikeMap);
        	Map<String, Integer> sortedSACLikeMap = sortByValuesInteger(evalObj.statusAndCommentRatingMap);
        	
        	//Iterator it = sortedYTLikeMap.entrySet().iterator();
        	Set<String> keySet = sortedFBLikeMap.keySet();
        	//String key = "";
        	Map<String, Integer> finalLikeHashMap = new HashMap<String, Integer>();
        	Integer value = 0;
        	for( String key : keySet ) 
        	{
        		//key = (String) it.next();
        		int FBLikeRank = 0;
        		int YTLikeRank = 0;
        		int SACLikeRank = 0;
        		if( sortedFBLikeMap.containsKey(key) )
        		{
        			FBLikeRank = sortedFBLikeMap.get(key);
        		}
        		if( sortedYTLikeMap.containsKey(key) )
        		{
        			YTLikeRank = sortedYTLikeMap.get(key);
        		}
        		if( sortedSACLikeMap.containsKey(key) )
        		{
        			SACLikeRank = sortedSACLikeMap.get(key);
        		}
        		value = FBLikeRank + YTLikeRank + SACLikeRank;
        		finalLikeHashMap.put(key, value);
        	}
//        	System.out.println(finalLikeHashMap);
//        	System.out.println(evalObj.FBLikesCountMap);
//        	System.out.println(evalObj.YTViewsCountMap);
        	
        	System.out.println("Printing the original hash final map...........................................");
        	System.out.println("abc" + finalLikeHashMap);
        	JSONArray returnObj = createJSON(finalLikeHashMap, evalObj);
        	String vidJson = new Gson().toJson(returnObj);
        	System.out.println("pqr" +  vidJson);
            //response.setContentType("application/json");
        	response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(vidJson);
            //response.getWriter().write("successful");
            
        } 
        catch (SQLException e) 
        {
        	response.getWriter().write("SQLException" + e.getMessage());
            throw new ServletException(e);
        } 
        catch (FacebookException e) 
        {
        	response.getWriter().write("FacebookException" + e.getMessage());
			e.printStackTrace();
		} 
        catch (InterruptedException e) 
		{
			response.getWriter().write("InterruptedException" + e.getMessage());
			e.printStackTrace();
		} 
        catch (InstantiationException e) 
        {
        	response.getWriter().write("InstantiationException" + e.getMessage());
			e.printStackTrace();
		} 
        catch (IllegalAccessException e) 
        {
        	response.getWriter().write("IllegalAccessException" + e.getMessage());
			e.printStackTrace();
		} 
        catch (ClassNotFoundException e) 
        {
        	response.getWriter().write("SQLException" + e.getMessage());
			e.printStackTrace();
		}
        finally{
        	try {
				stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        //response.sendRedirect(request.getContextPath()+ "/");
        //response.getWriter().write("eeee");
    }
    

	private JSONArray createJSON(Map<String, Integer> finalLikeHashMap,
			EvaluationObject evalObj2) 
	{
		JSONArray returnObject = new JSONArray();
		Map<String, Integer> sortedFinalHashMap = sortByValuesInteger(finalLikeHashMap);
		Set<String> keySet = sortedFinalHashMap.keySet();
		try 
		{
			for( String key : keySet )
			{
				JSONObject dataJSON = new JSONObject();
				String post_id = key.split(":::")[0];
				String URL = key.split(":::")[1];
				//int URLStartIndex = URL.indexOf("https://www.youtube.com/watch?v=");
				
				String URLId = URL.substring(32);
			
				dataJSON.put("post_id", post_id);
				dataJSON.put("URL", URLId);
				dataJSON.put("fblikes", evalObj2.FBLikesCountMap.get(post_id));
				dataJSON.put("ytviews", evalObj2.YTViewsCountMap.get(URL));
				returnObject.put(dataJSON);
			}
			System.out.println("*****************#################******************");
			System.out.println(returnObject);
		} 
		catch (JSONException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return returnObject;
	}


	private Map<String, Integer> sortByValuesInteger(
			Map<String, Integer> integerMap) 
	{
		List list = new LinkedList(integerMap.entrySet());
	       // Defined Custom Comparator here
	       Collections.sort(list, new Comparator() {
	            public int compare(Object o1, Object o2) {
	               return ((Comparable) ((Map.Entry) (o1)).getValue())
	                  .compareTo(((Map.Entry) (o2)).getValue());
	            }
	       });
	       int Count = 1;
	   HashMap<String, Integer> sortedHashMap = new LinkedHashMap<String, Integer>();
	   for (Iterator it = list.iterator(); it.hasNext();) 
	   {
		   Map.Entry entry = (Map.Entry) it.next();
		   sortedHashMap.put((String)entry.getKey(), Count++);
	   }
	   return sortedHashMap;
	}


	private Map<String, Integer> sortByValuesFloat(Map<String, Float> floatMap) 
	{
		List list = new LinkedList(floatMap.entrySet());
	       // Defined Custom Comparator here
	       Collections.sort(list, new Comparator() {
	            public int compare(Object o1, Object o2) {
	               return ((Comparable) ((Map.Entry) (o1)).getValue())
	                  .compareTo(((Map.Entry) (o2)).getValue());
	            }
	       });
	       int count = 1;
	   HashMap<String, Integer> sortedHashMap = new LinkedHashMap<String, Integer>();
	   for (Iterator it = list.iterator(); it.hasNext();) 
	   {
		   Map.Entry entry = (Map.Entry) it.next();
		   sortedHashMap.put((String)entry.getKey(), count++);
	   }
	   return sortedHashMap;
	}

	private static class FQLExtractThread extends Thread
	{
		String searchString;
		Facebook facebook;
		JSONArray jsonStreamArray2;
		private static Integer FBLikessharedLock = 0;
		private static Integer YTLikessharedLock = 0 ;
		private static Integer StatusAndCommentLikessharedLock = 0;
		
		EvaluationObject evalObj;

		public FQLExtractThread(String searchString, JSONArray jsonStreamArray,
				Facebook facebook, EvaluationObject evalObj) 
		{
			this.evalObj = evalObj;
			this.jsonStreamArray2 = jsonStreamArray;
			this.facebook = facebook;
			this.searchString = searchString;
		}

		public void run() 
		{
			try 
			{
				processResults();
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		
		private void processResults() 
		{
			String commentQuery= "";
			int i = 0;
			int j = 0;
			int messageAndCommentVal = 0;
			try
			{
//				Connection conn = DriverManager.getConnection(
//		 	               "jdbc:mysql://fomdbinstanceidentifier.cypuguixmtxm.us-east-1.rds.amazonaws.com:3306/fomdb", "root", "password");
				
				Connection conn = DriverManager.getConnection(
		   	               "jdbc:mysql://try.cq7ncj7mhv6r.us-east-1.rds.amazonaws.com:3306/fom", "root", "qwerty123");
		        	
				Statement stmt = conn.createStatement();
				for (i = 0; i < jsonStreamArray2.length(); i++) 
				{
					JSONObject jsonStreamObject = jsonStreamArray2.getJSONObject(i);
					JSONObject likeInfo = (JSONObject) jsonStreamObject.get("like_info");
					
					String postMessage = (String)jsonStreamObject.get("message");
					String actorID = (String) jsonStreamObject.get("actor_id");
					int createdTime = (Integer) jsonStreamObject.get("created_time");
					String postID = (String) jsonStreamObject.get("post_id");
//					System.out.println(",,,,,,,,,,,,,,,,,,,,,,,,,,,,,,'''''''''''''''''''''''''''''''");
//					System.out.println(" message " + postMessage + " actor_id " + actorID + " created_time " + createdTime + " post_id " + postID);
					
					int likeCount = (Integer) likeInfo.get("like_count");
					evalObj.FBLikesCountMap.put(postID, likeCount);
//					System.out.println("Like Count: " + likeCount);
					
//					setFBLikeCountInMap(likeCount);
					
					String message = (String) jsonStreamObject.get("message");
					System.out.println("##################################################################");
					System.out.println(message);
					/**
					 * handle the case of multiple url's being introduced. All you need to do is write a loop so that you find a link from a starting index. 
					 * {message.indexOf(ch, fromIndex)} Also you need to keep increasing the fromIndex after each link is found.
					 */
					List<String> linksArray = new ArrayList<String>();
					int startIndex = 0;
					if( message.indexOf(searchString) >= 0 )
					{
						messageAndCommentVal += 10;
					}
					while((startIndex + 43) <= message.length())
					{
						startIndex = message.indexOf("https://www.youtube.com/watch?v=",startIndex);
						if( !(startIndex >= 0) )
						{
							break;
						}
						String linkString = message.substring(startIndex, startIndex + 43 );
						linksArray.add(linkString);
//						System.out.println(linkString);
						
					
						commentQuery = "select text from comment where post_id = '" + jsonStreamObject.get("post_id") + "'";
						JSONArray jsonCommentArray = facebook.executeFQL(commentQuery);
						for( j = 0; j < jsonCommentArray.length(); j++ )
						{
//							System.out.println("Comment Text: " + jsonCommentObject.get("text") );
							JSONObject jsonCommentObject = jsonCommentArray.getJSONObject(j);
							String commentString = (String) jsonCommentObject.get("text");
							if( commentString.indexOf(searchString) >= 0 )
							{
								messageAndCommentVal += 5;
							}
						}
						if( messageAndCommentVal > 0 )
						{
							YouTubeService service = new YouTubeService("FOM");
							service.setConnectTimeout(2000); // millis
							YouTubeQuery query = new YouTubeQuery(new URL(YOUTUBE_URL));
							query.setOrderBy(YouTubeQuery.OrderBy.VIEW_COUNT);
							query.setFullTextQuery(linkString);
							query.setSafeSearch(YouTubeQuery.SafeSearch.NONE);
							query.setMaxResults(1);

							VideoFeed videoFeed = service.query(query, VideoFeed.class);	
							List<VideoEntry> videos = videoFeed.getEntries();
							Float videoRating = 0F;
							for (VideoEntry videoEntry : videos) 
							{
//								System.out.println("***************************************************************");
//		                    	System.out.println(videoEntry.getTitle().getPlainText());
								Set<Category> abc = videoEntry.getCategories();
								String videoCatergory = "";
								for(Category cat : abc)
		                    	{
									videoCatergory = cat.getLabel();
		                    	}
								long viewCount = videoEntry.getStatistics().getViewCount();
								evalObj.YTViewsCountMap.put(linkString, viewCount);
								videoRating = videoEntry.getRating().getAverage();
							}
							
							
							setFBLikeCountInMap(likeCount, linkString, postID);
							setYTLikeCountInMap(videoRating, linkString, postID);
							setSACCountInMap(messageAndCommentVal, linkString, postID);
						}
						startIndex+=43;
					}
				}
			} 
			catch (JSONException e) 
			{
				e.printStackTrace();
			} 
			catch (FacebookException e) 
			{
				e.printStackTrace();
			} 
			catch (SQLException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			catch (ServiceException e) 
			{
				e.printStackTrace();
			}
		}

		private void setSACCountInMap(int messageAndCommentVal,
				String linkString, String postID) 
		{
			synchronized (StatusAndCommentLikessharedLock) 
			{
				String key = postID + ":::"+linkString;
				if( evalObj.statusAndCommentRatingMap.containsKey(key) )
				{
					Integer initialScore = evalObj.statusAndCommentRatingMap.get(key);
					Integer valueToInsert = initialScore + messageAndCommentVal;
					evalObj.statusAndCommentRatingMap.put(key, messageAndCommentVal);
				}
				else
				{
					evalObj.statusAndCommentRatingMap.put(key, messageAndCommentVal);
				}
			}
		}

		private void setYTLikeCountInMap(Float videoRating, String linkString,
				String postID) 
		{
			synchronized (YTLikessharedLock) 
			{
				String key = postID + ":::"+linkString;
				if(!evalObj.YTLikeMap.containsKey(key))
				{
					evalObj.YTLikeMap.put(key, videoRating);
				}
			}
		}

		private void setFBLikeCountInMap(int likeCount, String linkString, String postID) 
		{
			synchronized (FBLikessharedLock) 
			{
				String key = postID + ":::"+linkString;
				if(evalObj.FBLikeMap.containsKey(key))
				{
					Integer currentLikeCount = evalObj.FBLikeMap.get(key);
					if (currentLikeCount < likeCount)
					{
						evalObj.FBLikeMap.put(key, likeCount);
					}
				}
				else
				{
					if(likeCount >= 0)
						evalObj.FBLikeMap.put(postID + ":::"+linkString, likeCount);
					else
						evalObj.FBLikeMap.put(postID + ":::"+linkString, 0);
				}
			}
		}
		
		
	}
	
	private class EvaluationObject
	{
		private Map<String,Integer> FBLikeMap = new HashMap<String,Integer>();
		private Map<String,Float> YTLikeMap = new HashMap<String,Float>();
		private Map<String,Integer> statusAndCommentRatingMap = new HashMap<String,Integer>();
		private Map<String,Integer> FBLikesCountMap = new HashMap<String,Integer>();
		private Map<String,Long> YTViewsCountMap = new HashMap<String,Long>();
		public void emptyAll() 
		{
			FBLikeMap.clear();YTLikeMap.clear();statusAndCommentRatingMap.clear();FBLikesCountMap.clear();YTViewsCountMap.clear();
		}
		
	}
    
}