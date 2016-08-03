package com.fom;

import java.io.IOException;
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

/**
 * Servlet implementation class AddListeningActivity
 */
public class AddListeningActivity extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddListeningActivity() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{

		Connection conn = null;
		Statement stmt = null;
		try 
		{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Class.forName("com.mysql.jdbc.Driver").newInstance();
       	 conn = DriverManager.getConnection(
  	               "jdbc:mysql://try.cq7ncj7mhv6r.us-east-1.rds.amazonaws.com:3306/fom", "root", "qwerty123");
       	
//       	Connection conn = DriverManager.getConnection(
//	               "jdbc:mysql://localhost:3306/fom", "root", "passwd");
       	//String mood = "sleepy";
       	stmt = conn.createStatement();
			
			Facebook facebook = (Facebook) request.getSession().getAttribute("facebook");
			String post_id = request.getParameter("post_id");
			String mood  = request.getParameter("mood");
			String url = request.getParameter("URL");
//			facebook.likePost("");
			
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
	        Date date = new Date();
	        String dateFormatted = dateFormat.format(date);
//	        System.out.println(dateFormatted);
			
	        String query = "insert into activity (act_event, mood, url, post_id, user_id, activity_date) values( 'listen','" + mood +"','"+ url +"','"+ post_id +"','"+ facebook.getId() + "','" + dateFormatted+"')";
	        stmt.executeUpdate(query); 
		} 
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FacebookException e) {
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
		}
		finally
		{
			try 
			{
				stmt.close();
				conn.close();
			} catch (SQLException e) 
			{
				e.printStackTrace();
			}
		}
     	
     	
	
	}

}
