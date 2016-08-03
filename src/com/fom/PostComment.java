package com.fom;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.PostUpdate;

/**
 * Servlet implementation class PostComment
 */
public class PostComment extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PostComment() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		Connection conn = null;
		Statement stmt = null;

		try 
		{
			Facebook facebook = (Facebook) request.getSession().getAttribute("facebook");
			String post_id = request.getParameter("post_id");
			String mood = request.getParameter("mood");
			String post_message = request.getParameter("x");
			String URL = request.getParameter("URL");
//			facebook.commentPost(post_id, post_message);
//			facebook.likePost("");
			String message = post_message + "\nhttps://www.youtube.com/watch?v=" + URL + "\n--##FOM" + mood + "##"; 
			
			PostUpdate post = new PostUpdate(new URL("http://frndsofmuz-taeecibhvj.elasticbeanstalk.com/home.jsp#_=_"))
		       .picture(new URL("http://s22.postimg.org/krybej9b1/logo1.jpg"))
		       .name("Friends Of Music: A Whole New Way To Listen To Songs")
		       .description("Facebook4J is a Java library for the Facebook Graph API.");
			
			facebook.postFeed(post);
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
	        Date date = new Date();
	        String dateFormatted = dateFormat.format(date);
			
			String query = "insert into activity (act_event, mood, url, post_id, user_id, activity_date) values( 'share','" + mood +"','"+ URL +"','"+ post_id +"','"+ facebook.getId() + "','" + dateFormatted+"')";
	        Class.forName("com.mysql.jdbc.Driver").newInstance();
       	 conn = DriverManager.getConnection(
  	               "jdbc:mysql://try.cq7ncj7mhv6r.us-east-1.rds.amazonaws.com:3306/fom", "root", "qwerty123");
       	
//       	Connection conn = DriverManager.getConnection(
//	               "jdbc:mysql://localhost:3306/fom", "root", "passwd");
       	//String mood = "sleepy";
       	stmt = conn.createStatement();
       	stmt.executeUpdate(query);

		} 
		catch (FacebookException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
