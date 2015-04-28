package com.sappress.hcpbook.chp08;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("serial")
public class ClientBootstrapServlet extends HttpServlet 
{
  // Constant declarations:
  private static final Logger logger = LoggerFactory.getLogger(ClientBootstrapServlet.class);
  
	public void init(ServletConfig config) throws ServletException 
	{
	  // Fetch the OAuth configuration details from the web.xml file:
    String clientId = config.getInitParameter("OAuthClientId");
    String hcpAccount = config.getInitParameter("HCPAccount");
    String hcpHost = config.getInitParameter("HCPHost");
    String apiApp = config.getInitParameter("APIApplication");
    String apiPath = config.getInitParameter("APIPath");
    
    if ((clientId == null) || (clientId.equals("")))
      throw new ServletException("Could not determine OAuth client ID. Check your web.xml file.");
    
    if ((hcpAccount == null) || (hcpAccount.equals("")) ||
        (hcpHost == null) || (hcpHost.equals("")))
      throw new ServletException("Could not determine HCP account information. Check your web.xml file.");
    
    // Store the configuration details in the OAuthClient singleton instance:
    OAuthClient client = OAuthClient.getInstance();
    client.setClientId(clientId);
    client.setHcpAccount(hcpAccount);
    client.setHcpHost(hcpHost);
    client.setApiApplication(apiApp);
    client.setApiPath(apiPath);
	} // -- public void init() -- //

	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
	  throws ServletException, IOException 
	{
	  // Method-Local Data Declarations:
	  RequestDispatcher rd = null;
	  
	  // Check to see if the client has authenticated with OAuth:
	  OAuthClient client = OAuthClient.getInstance();
	  if (client.hasAccessToken())
	  {
	    logger.info("Token: " + client.getAccessToken());
	    
	    // If so, go ahead and route them to the application:
	    rd = request.getRequestDispatcher("SearchFlights.jsp");
	    rd.forward(request, response);
	  }
	  else
	  {
	    // Otherwise, redirect to the OAuth authorization URL:
	    client.setCallbackDetails(request);
	    String authEndpoint = client.getAuthEndpoint();
	    logger.debug("Redirecting to authorization endpoint: " + authEndpoint);
	    response.sendRedirect(authEndpoint);
	  }
	} // -- protected void doGet() -- //

} // -- End of class ClientBootstrapServlet -- //