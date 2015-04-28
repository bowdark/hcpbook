package com.sappress.hcpbook.chp06;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.apache.commons.io.IOUtils;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sappress.hcpbook.chp06.document.AttachmentProcessorBean;
import com.sappress.hcpbook.chp06.document.DocumentDTO;
import com.sappress.hcpbook.chp06.persistence.Attachment;
import com.sappress.hcpbook.chp06.persistence.ConfigurationManagerBean;
import com.sappress.hcpbook.chp06.persistence.Incident;
import com.sappress.hcpbook.chp06.persistence.IncidentBean;
import com.sappress.hcpbook.chp06.persistence.InvolvedPerson;

/**
 * Servlet implementation class IncidentEditorServlet
 */
@MultipartConfig
public class IncidentEditorServlet extends HttpServlet 
{
  // Constant Declarations:
	private static final long serialVersionUID = 1L;
	private static final String BUFFER_KEY = "incident_buffer";
  private static final String ACTION_KEY = "action";
  private static final String INCIDENT_KEY = "key";
  private static final String PERSON_KEY = "person_id";
  private static final String ATTACHMENT_KEY = "attachment_id";
  private static final String GOOGLE_API_KEY = "google_api_key";
	
  private static final Logger logger = LoggerFactory.getLogger(IncidentEditorServlet.class);
  
	@EJB
	private IncidentBean incidentBean;
	
	@EJB
	private AttachmentProcessorBean attachmentProcessorBean;

	@EJB
	private ConfigurationManagerBean confManagerBean;
  
	private String googleApiKey;
	
	/**
	 * Initialize the servlet
	 */
  public void init() throws ServletException
  {
    // Load the Google API key from the web.xml initialization parameters:
    this.googleApiKey = getInitParameter(GOOGLE_API_KEY);
  } // -- public void init() -- //
  
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	  throws ServletException, IOException 
	{
	  // Method-Local Data Declarations:
    Incident incident = null;
    String incidentId = null;
    String attachmentId = null;
    
	  try
	  {     
	    // Determine the requested action:
	    String actionKey = request.getParameter(ACTION_KEY);
	    IncidentEditorAction action = 
	      IncidentEditorAction.getActionForUrlParameter(actionKey);
	    incidentId = request.getParameter(INCIDENT_KEY);
	    	    
	    switch (action)
	    {
	      case CREATE_INCIDENT:
  	      // Create a new incident record:
	        incident = new Incident();
	        
	        // Direct the user to the JSP-based editor screen:
	        openEditor(request, response, incident);
	        
	        break;
	      case EDIT_INCIDENT:
	        // Fetch the selected incident record:
	        incident = retrieveFromBuffer(request.getSession(), incidentId);
	        
	        if (incident == null)
	        {
	          reportError(response, "Incident " + incidentId + " doesn't exist or couldn't be loaded.");
	          return;
	        }
	        
          // Direct the user to the JSP-based editor screen:
          openEditor(request, response, incident);
	        
	        break;
	      case REMOVE_INCIDENT:
	        // Remove the selected incident record:
          incident = retrieveFromBuffer(request.getSession(), incidentId);
          incidentBean.removeIncident(incident);
	        
	        break;
	      case REMOVE_INVOLVED_PERSON:
	        // Retrieve the target incident record from the buffer:
	        incident = retrieveFromBuffer(request.getSession(), incidentId);
          
          if (incident == null)
          {
            reportError(response, "Incident " + incidentId + " doesn't exist or couldn't be loaded.");
            return;
          }
          
          // Remove the selected person record from the incident:
          String personId = request.getParameter(PERSON_KEY);
          
          logger.info("Deleting person record.");
          incidentBean.removePerson(personId);
          logger.info("Person deleted.");
          
          logger.info("Removing person with ID: " + personId + " from incident " + incidentId);
          if (incident.removeInvolvedPerson(personId))
          {
            logger.info("Person has been removed");
            
            response.getWriter().println("success");
          }
          else
          {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
          }
          
	        break;
	      case GET_GEOCOORDINATES:
	        // Load coordinate data for the incident:
	        incident = retrieveFromBuffer(request.getSession(), incidentId);
	        
          logger.info("Calling coordinate lookup with " + googleApiKey);
          incidentBean.loadGeoCoordinates(incident, googleApiKey);
	        break;
	      case GET_ATTACHMENT:
	        // Fetch the attachment which corresponds with the selected id: 
	        attachmentId = request.getParameter(ATTACHMENT_KEY);
	        if (attachmentId == null)
	        {
	          reportError(response, "Attachment ID is missing or invalid.");
	          return;
	        }
	        
	        DocumentDTO attachment = attachmentProcessorBean.getAttachmentById(attachmentId, null);
	        
	        // Download the file:
	        response.setContentType(attachment.getMimeType());
	        response.setHeader("Content-Disposition", "attachment, filename=\"" + attachment.getName() + "\"");
	        BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());
	        bos.write(attachment.getPayload());
	        bos.close();
	        
	        break;
	      case REMOVE_ATTACHMENT:
          // Retrieve the target incident record from the buffer:
          incident = retrieveFromBuffer(request.getSession(), incidentId);
          
          if (incident == null)
          {
            reportError(response, "Incident " + incidentId + " doesn't exist or couldn't be loaded.");
            return;
          }
          
          // Remove the selected attachment record: 
          try
          {
            attachmentId = request.getParameter(ATTACHMENT_KEY);
            attachmentProcessorBean.removeAttachment(attachmentId);
            
            if (incident.removeAttachment(attachmentId))
            {
              incidentBean.removeAttachment(attachmentId);
              response.getWriter().println("success");
            }
            else
            {
              response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
          }
          catch (Exception e)
          {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
          }
          
	        break;
	      default:
	        return;
	    }	    
	  }
	  catch (Exception ex)
	  {
      reportError(response, ex.getMessage());
	  }
	} // -- protected void doGet() -- //
	
	/**
	 * This method is used to open up a JSP-based editor for the incident.
	 * @param request
	 * @param response
	 * @param incident
	 * @throws ServletException
	 * @throws IOException
	 */
	private void openEditor(HttpServletRequest request, HttpServletResponse response, Incident incident)
	  throws ServletException, IOException
	{
	  // Copy the detached incident reference into session scope so that we can
    // keep track of it:
	  updateBuffer(request.getSession(), incident);
    
	  // Create bean attributes to bind the incident data to the JSP-based editor form:	  
	  request.setAttribute("incident", incident);
	  request.setAttribute("location", incident.getLocation());
	  request.setAttribute("address", incident.getLocation().getAddress());
	  request.setAttribute("attachments", attachmentProcessorBean.getAttachmentsForIncident(incident));
	  request.setAttribute("regions", confManagerBean.getRegionsByCountry(incident.getLocation().getAddress().getCountry()));
    request.setAttribute("countries", confManagerBean.getCountries());
    
    // Route the user to the incident editor form:
    RequestDispatcher rd = request.getRequestDispatcher("EditIncident.jsp");
    rd.forward(request, response);
	} // -- private void openEditor() -- //
	
	/**
	 * This method is used to update the Session-based incident buffer.
	 * @param session
	 * @param incident
	 */
	private void updateBuffer(HttpSession session, Incident incident)
	{
	  @SuppressWarnings("unchecked")
    Map<String, Incident> incidentMap = (Map<String, Incident>) session.getAttribute(BUFFER_KEY);
	  if (incidentMap == null)
	  {
	    incidentMap = new HashMap<String, Incident>();
	    session.setAttribute(BUFFER_KEY, incidentMap);
	  }
	  
	  incidentMap.put(incident.getId(), incident);
	} // -- private void updateBuffer() -- //
	
	/**
	 * This method is used to fetch an incident record from the Session-based buffer.
	 * @param session
	 * @param incidentId
	 * @return
	 */
	private Incident retrieveFromBuffer(HttpSession session, String incidentId)
	{
	  @SuppressWarnings("unchecked")
    Map<String, Incident> incidentMap = (Map<String, Incident>) session.getAttribute(BUFFER_KEY);
	  if (incidentMap == null)
	  {
	    incidentMap = new HashMap<String, Incident>();
      session.setAttribute(BUFFER_KEY, incidentMap);
	  }
	  
	  if (incidentMap.containsKey(incidentId))
	    return incidentMap.get(incidentId);
	  
	  Incident incident = incidentBean.getIncidentById(incidentId);
	  if (incident != null)
	    incidentMap.put(incident.getId(), incident);
	  
	  return incident;
	} // -- private Incident retrieveFromBuffer() -- //
	
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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
	  throws ServletException, IOException 
	{
	  // Method-Local Data Declarations:
	  String incidentId = null;
	  Incident incident = null;
	  IncidentEditorAction action = null;
	  JSONObject jsonObject = null;
	  
	  try
	  {
	    // Determine the target action key:
	    String actionKey = request.getParameter(ACTION_KEY);
	    if (actionKey != null)
	    {
	      incidentId = request.getParameter(INCIDENT_KEY);
	    }
	    else
	    {
  	    // Parse the JSON payload:     
  	    String payload = ServletUtils.getJSONPayload(request);
        if (payload == null)
          return;
        
        jsonObject = (JSONObject) JSONValue.parse(payload);
        actionKey = (String) jsonObject.get(ACTION_KEY);
        incidentId = (String) jsonObject.get("id");
	    }

	    if (actionKey == null)
	    {
	      reportError(response, "Could not determine target action.");
	      return;
	    }
	    
	    action = IncidentEditorAction.getActionForUrlParameter(actionKey);
      if (action == null)
      {
        reportError(response, "Could not determine target action.");
        return;
      }
	    
	    // Fetch the target incident ID:
	    if (incidentId == null)
	    {
	      reportError(response, "Could not determine target incident ID.");
	      return;
	    }
	    
	    // Process based on the selected action:
	    switch (action)
	    {
	      case SAVE_INCIDENT:
    	    // Copy the updated fields to the incident:
	        incident = retrieveFromBuffer(request.getSession(), incidentId);
	        incident.copyFromJSON(jsonObject);
	            	    
    	    // Save the changes:
    	    incidentBean.updateIncident(incident);
    	    
    	    response.getWriter().println("success");
          break;
	      case ADD_ATTACHMENT:
	        // Process an attachment upload request:
	        incident = retrieveFromBuffer(request.getSession(), incidentId);
	        DocumentDTO document = new DocumentDTO();
	        
	        for (Part part : request.getParts())
	        {
	          // Retrieve the file contained within the part object:
	          document.setName(getFileName(part));
	          document.setMimeType(part.getContentType());
	          document.setPayload(IOUtils.toByteArray(part.getInputStream()));
	          
	          String attachmentId =
	            attachmentProcessorBean.createAttachment(incident.getId(), document.getName(), 
	                                                     document.getMimeType(), document.getPayload());
	          document.setId(attachmentId);
	          
	          Attachment attachment = new Attachment();
	          attachment.setId(attachmentId);
	          attachment.setIncident(incident);
	          
	          incident.addAttachment(attachment);
	        }
	        
	        updateBuffer(request.getSession(), incident);
	        
	        response.getWriter().println(document.toJSON());
	        
	        break;
	      case ADD_INVOLVED_PERSON:
	        // Add the selected involved person to the incident:
	        incident = retrieveFromBuffer(request.getSession(), incidentId);
	        InvolvedPerson person = new InvolvedPerson(jsonObject);
	        incident.addInvolvedPerson(person);
	        
	        updateBuffer(request.getSession(), incident);
	        
	        response.getWriter().println("{\"personId\" : \"" + person.getId() + "\"}");
	        
	        break;
        default:
          return;
	    }
	  }
	  catch (Exception ex)
	  {
	    logger.error(ex.getMessage());
	    
     response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
     response.getWriter().println(ex.getMessage());
	  }
	} // -- protected void doPost() -- //
	
	/**
	 * Retrieve the file name for a part in a multi-part request
	 * @param part
	 * @return
	 */
	private String getFileName(Part part)
	{
	  String contentDisp = part.getHeader("content-disposition");
	  if (contentDisp == null)
	    return "";
	  
	  String[] items = contentDisp.split(";");
	  if (items == null)
	    return "";
	  
	  for (String s : items)
	  {
	    if (s.trim().startsWith("filename"))
	      return s.substring(s.indexOf("=") + 2, s.length() - 1);
	  }
	  return "";
	} // -- private String getFileName() -- //

} // -- public class IncidentEditorServlet -- //