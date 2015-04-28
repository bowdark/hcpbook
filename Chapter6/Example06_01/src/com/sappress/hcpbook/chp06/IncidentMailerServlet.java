package com.sappress.hcpbook.chp06;

import java.io.IOException;

import javax.ejb.EJB;
import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.sappress.hcpbook.chp06.connectivity.EmailAgentBean;
import com.sappress.hcpbook.chp06.persistence.Incident;
import com.sappress.hcpbook.chp06.persistence.IncidentBean;

/**
 * Servlet implementation class IncidentMailerServlet
 */
public class IncidentMailerServlet extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
  
	@EJB
  private IncidentBean incidentBean;
	
	@EJB
	private EmailAgentBean emailAgentBean;
	
	private String senderAddress;
	
	public void init() throws ServletException
	{
	  // In order to determine the sender address for the e-mail message, we're
	  // using a Servlet init parameter to pull the value from web.xml. For your
	  // testing, we recommend changing this parameter to the sender address
	  // of your choice.
	  this.senderAddress = getInitParameter("senderAddress");
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	  throws ServletException, IOException 
	{
	  // Parse the JSON request payload:
		String payload = ServletUtils.getJSONPayload(request);
		if (payload == null)
      return;
    
    JSONObject jsonObject = (JSONObject) JSONValue.parse(payload);
    
    String incidentId = (String) jsonObject.get("id");
    if (incidentId == null)
    {
      reportError(response, "Could not determine target incident!");
      return;
    }
    
    String emailAddress = (String) jsonObject.get("emailAddress");
    if (emailAddress == null)
    {
      reportError(response, "Invalid email address provided.");
      return;
    }
    
    // Send the incident notification e-mail message:
    sendEmail(incidentId, emailAddress);
	} // -- protected void doPost() -- //

	/**
	 * Send an incident notification email message
	 * @param incidentId
	 * @param emailAddress
	 * @throws IOException
	 */
	private void sendEmail(String incidentId, String emailAddress)
	  throws IOException
	{
	  // Method-Local Data Declarations:
	  String subject = "Incident E-Mail Notification";
	  String body = null;
	  Incident incident = incidentBean.getIncidentById(incidentId);
	  
	  // Build the notification message:
	  body =
	    "<html>" +
	    "<head></head>" +
	    "<body><h1>Incident Notification</h1>" +
	    "<table>" +
	    "<tr><td>Title</td><td>" + incident.getTitle() + "</td></tr>" +
	    "<tr><td>Description</td><td>" + incident.getDescription() + "</td></tr>" +
	    "<tr><td>Location</td><td>" + incident.getLocation().getName() + "</td></tr>" +
	    "</body>" +
	    "</html>";
	  
	  // Try to send out the message:
	  try
	  {
	    emailAgentBean.sendMessage(this.senderAddress, emailAddress, subject, body);
	  }
	  catch (MessagingException me)
	  {
	    throw new IOException(me.getMessage(), me);
	  }
	} // -- private void sendEmail() -- //
	
  /**
   * Report an error message to the frontend.
   * @param response
   * @param message
   * @throws ServletException
   * @throws IOException
   */
  private void reportError(HttpServletResponse response, String message)
    throws ServletException, IOException
  {
    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    response.getWriter().println(message);
  } // -- private void reportError() -- //

} // -- public class IncidentMailerServlet -- //