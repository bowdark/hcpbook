package com.sappress.hcpbook.chp08;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sap.security.um.user.User;
import com.sap.security.um.user.UserProvider;

@SuppressWarnings("serial")
public class HelloServlet extends HttpServlet 
{
  // Constant Declarations:
  public static final String ACTOR_ROLE = "actor";
  public static final String MAGICIAN_ROLE = "magician";
  
  // Instance Attribute Declarations:
  @Resource
  private UserProvider userProvider;
  
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	  throws ServletException, IOException 
	{
	  try
	  {
	    // Fetch the logged on user account details:
	    User user = userProvider.getUser(request.getRemoteUser());	    
	    String firstName = user.getAttribute("firstName");
	    
	    // Check the user's role assignments:
	    if (request.isUserInRole(ACTOR_ROLE))
	      response.getWriter().println("Hello, " + firstName + ". Welcome to the Carl Weathers School of Acting.");
	    else if (request.isUserInRole(MAGICIAN_ROLE))
	      response.getWriter().println("Hello, " + firstName + ". Welcome to the Alliance of Magicians.");
	    else
	      response.getWriter().println("Sorry, " + firstName + ". You're not authorized to see this content.");
	  }
	  catch (Exception ex)
	  {
	    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	    response.getWriter().println(ex.getMessage());
	  }
	} // -- protected void doGet() -- //
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	  throws ServletException, IOException
	{
	  doGet(request, response);  
	}
	
} // -- End of class HelloServlet -- //