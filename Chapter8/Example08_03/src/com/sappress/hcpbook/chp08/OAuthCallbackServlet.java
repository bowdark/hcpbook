package com.sappress.hcpbook.chp08;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("serial")
public class OAuthCallbackServlet extends HttpServlet 
{
  private static final Logger logger = LoggerFactory.getLogger(OAuthCallbackServlet.class);
  
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
	  throws ServletException, IOException 
	{    
	  try
	  {
	    // Fetch the authorization code returned from the OAuth authorization server:
	    String authCode = request.getParameter("code");
	    logger.info("Received authorization code [" + authCode + "]");
	    
	    // Exchange the authorization code for an access token:
	    OAuthClient client = OAuthClient.getInstance();
	    client.retrieveAccessToken(authCode);
	    logger.info("Received access token [" + client.getAccessToken() + "]");
	    
	    // Redirect the user back to the main page:
	    response.sendRedirect("index.do");
	  }
	  catch (IOException ioe)
	  {
	    logger.error(getStackTraceAsString(ioe));
	  }
	} // -- protected void doGet() -- //
	
	private String getStackTraceAsString(Throwable t)
	{
	  try
	  {
	    StringWriter sw = new StringWriter();
	    PrintWriter pw = new PrintWriter(sw);
	    
	    t.printStackTrace(pw);
	    
	    return sw.toString();
	  }
	  catch (Exception ex)
	  {
	    return t.getMessage();
	  }
	} // -- private String getStackTraceAsString() -- //

} // -- End of class OAuthCallbackServlet -- //