package com.sappress.hcpbook.chp06.persistence;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.core.connectivity.api.http.HttpDestination;
import com.sappress.hcpbook.chp06.document.AttachmentProcessorBean;

/**
 * Session Bean implementation class IncidentBean
 */
@Stateless
@LocalBean
public class IncidentBean 
{
  // Constant Declarations:
  private static final Logger logger = LoggerFactory.getLogger(IncidentBean.class);
  
  @PersistenceContext
  private EntityManager em;
  
  @Resource
  private DataSource dataSource;
  
  @EJB
  private AttachmentProcessorBean attachmentProcessorBean;  
  
  @Resource(mappedName="GoogleGeocodeDest")
  private HttpDestination geocodeDestination;
  
  @SuppressWarnings("unchecked")
  public List<Incident> getAllIncidents()
  {
    return em.createNamedQuery("AllIncidents").getResultList();
  }

  public List<Incident> getIncidentsByDate(Date fromDate)
  {
    TypedQuery<Incident> query =
      em.createQuery("SELECT i FROM Incident i WHERE i.ReportedOn BETWEEN ?1 and ?2", Incident.class);
    query.setParameter(1, fromDate);
    query.setParameter(2, new Date(System.currentTimeMillis()));
    
    return query.getResultList();
  }
  
  public Incident getIncidentById(String id)
  {
    return em.find(Incident.class, id);
  }
  
  public List<Incident> getIncidentsByInvolvedPerson(String firstName, String lastName)
    throws SQLException
  {
    // Method-Local Data Declarations:
    Connection conn = null;
    List<Incident> incidents = new ArrayList<Incident>();
    
    try
    {
      // Build the dynamic SQL query to fetch the incidents:
      StringBuffer query = new StringBuffer();
      String fname = ((firstName == null) ? "" : firstName.replaceAll("\\*", "%"));
      String lname = ((firstName == null) ? "" : lastName.replaceAll("\\*", "%"));
      
      query.append("SELECT i.id, i.title, i.reportedon FROM incident AS i INNER JOIN involvedperson AS p ");
      query.append("ON i.id = p.incident_id ");
      
      String operator = "WHERE";
      if (! fname.equals("")) 
      {        
        if (fname.contains("%"))
          query.append(operator + " p.firstname LIKE ? ");
        else
          query.append(operator + " p.firstname = ? ");
        
        operator = "AND ";
      }
      
      if (! lname.equals(""))
      {
        if (lname.contains("%"))
          query.append(operator + " p.lastname LIKE ?");
        else
          query.append(operator + " p.lastname = ?");
      }
      
      logger.info(query.toString());
      
      // Create a database connection:
      conn = dataSource.getConnection();
      
      // Create a statement object to encapsulate the query:
      PreparedStatement ps = conn.prepareStatement(query.toString());
      
      if (! fname.equals(""))
      {
        ps.setString(1, fname);
        
        if (! lname.equals(""))
          ps.setString(2, lname);
      }
      else
      {
        if (! lname.equals(""))
          ps.setString(1, lname);
      }
      
      // Execute the query:
      ResultSet rs = ps.executeQuery();
      
      // Copy the results into a DTO to be transferred to the frontend:
      while (rs.next())
      {
        Incident incident = new Incident();
        
        incident.setId(rs.getString(1));
        incident.setTitle(rs.getString(2));
        incident.setReportedOn(rs.getDate(3));
        
        incidents.add(incident);
      }
    }
    finally
    {
      // Always remember to release the connection to conserve resources:
      if (conn != null)
        conn.close();
    }
    
    return incidents;
  } // -- public List getIncidentsByInvolvedPerson() -- //
  
  public Incident addIncident(Incident incident)
  {
    em.persist(incident);
    em.flush();
    return incident;
  }
  
  public Incident updateIncident(Incident incident)
  {
    return em.merge(incident);
  }
  
  public void removeIncident(Incident incident)
  {
    // Remove all the attachments linked to the incident from the Document service:
    for (Attachment a : incident.getAttachments())
      attachmentProcessorBean.removeAttachment(a.getId());
    
    // Then, remove the incident itself:
    em.remove(em.merge(incident));
  }
  
  public void removePerson(InvolvedPerson person)
  {
    em.remove(em.merge(person));
  }
  
  public void removePerson(String personId)
  {
    InvolvedPerson person = em.find(InvolvedPerson.class, personId);
    if (person == null)
      return;
    
    removePerson(person);
  }
  
  public void removeAttachment(Attachment attachment)
  {
    em.remove(em.merge(attachment));
  }
  
  public void removeAttachment(String attachmentId)
  {
    Attachment a = em.find(Attachment.class, attachmentId);
    if (a == null)
      return;
    
    removeAttachment(a);
  }
  
  public void loadGeoCoordinates(Incident incident, String apiKey)
  {
    try
    {
      // Build an HTTP connection:
      HttpClient httpClient = geocodeDestination.createHttpClient();
      
      // Now, build the HTTP GET request:
      String url = geocodeDestination.getURI() + "?address=" + incident.getLocation().getAddress().getAddressString() +
          "&key=" + apiKey;
      logger.debug(url);
      HttpGet httpGet = new HttpGet(url);
      
      // Submit the request:
      HttpResponse httpResponse = httpClient.execute(httpGet);
      int statusCode = httpResponse.getStatusLine().getStatusCode();
      if (statusCode != HttpServletResponse.SC_OK)
        throw new IOException("The API call failed with a status code of " + statusCode);
      
      // Parse the response:
      HttpEntity entity = httpResponse.getEntity();
      if (entity == null)
        throw new IOException("The JSON response was empty.");
      
      String payload = IOUtils.toString(entity.getContent());
      JSONObject jsonResponse = (JSONObject) JSONValue.parse(payload);
      JSONArray results = (JSONArray) jsonResponse.get("results");
      
      if ((results != null) && (results.size() > 0))
      {
        JSONObject result = (JSONObject) results.get(0);
        JSONObject geometry = (JSONObject) result.get("geometry");
        if (geometry != null)
        {
          JSONObject location = (JSONObject) geometry.get("location");
          if (location != null)
          {            
            incident.getLocation().getAddress().setLatitude((Double) location.get("lat"));
            incident.getLocation().getAddress().setLongitude((Double) location.get("lng"));
            updateIncident(incident);
          }
        }
      }
    }
    catch (Exception ex)
    {
      logger.error("Could not load geocoordinates for the incident's location", ex);
    }
  }
} // -- public class IncidentBean -- //