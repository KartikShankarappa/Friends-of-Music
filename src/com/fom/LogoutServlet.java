package com.fom;

import facebook4j.Facebook;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import com.fom.PostServlet.FQLExtractThread;




import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogoutServlet extends HttpServlet 
{
    private static final long serialVersionUID = 5357658337449255998L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {      
        Facebook facebook = (Facebook) request.getSession().getAttribute("facebook");
        String accessToken = "";
        Connection conn = null;
		Statement stmt = null;
        try 
        {
//        	conn = DriverManager.getConnection(
//	 	               "jdbc:mysql://fomdbinstanceidentifier.cypuguixmtxm.us-east-1.rds.amazonaws.com:3306/fomdb", "root", "password");
        	Class.forName("com.mysql.jdbc.Driver").newInstance();
        	conn = DriverManager.getConnection(
    	               "jdbc:mysql://try.cq7ncj7mhv6r.us-east-1.rds.amazonaws.com:3306/fom", "root", "qwerty123");
        	stmt = conn.createStatement();
        	DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
	        Date date = new Date();
	        String dateFormatted = dateFormat.format(date);
//	        
//        	String Query = "select url, mood from activity where user_id = '" + facebook.getId() + "' and event = 'listen' and activity_date = '" + dateFormatted + "'";
//        	ResultSet rset = stmt.executeQuery(Query);
//        	String urlAppend = "";
//        	String mood ="";
//        	while(rset.next()) 
//	        {   
//        		urlAppend +=  rset.getString("url");
//        		urlAppend += "\n";
//        		mood = rset.getString("mood");
//	            
//	        }
//        	String finalString = facebook.getName() + " was listening to " + urlAppend + "-##fom:" + mood + "##";
        	
        	accessToken = facebook.getOAuthAccessToken().getToken();
        } 
        catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        catch (Exception e) 
        {
            throw new ServletException(e);
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
        request.getSession().invalidate();

        // Log Out of the Facebook
        StringBuffer next = request.getRequestURL();
        int index = next.lastIndexOf("/");
        next.replace(index+1, next.length(), "");
        response.sendRedirect("http://www.facebook.com/logout.php?next=" + next.toString() + "&access_token=" + accessToken);
    }
}
