package com.sappress.hcpbook.chp06;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sappress.hcpbook.chp06.persistence.Incident;
import com.sappress.hcpbook.chp06.persistence.IncidentBean;

/**
 * Servlet implementation class IncidentQueryServlet
 */
public class IncidentQueryServlet extends HttpServlet 
{
  // Constant Declarations:
	private static final long serialVersionUID = 1L;
  private static final String QUERY_KEY = "queryName";
  private static final String INCIDENTS_BY_PERSON = "incidentsByPerson";
	
  @EJB
  private IncidentBean incidentBean;
  
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
	  throws ServletException, IOException 
	{
	  // Method-Local Data Declarations:
	  List<Incident> incidents = null;
	  
	  // Determine the type of query to process:
    String queryName = request.getParameter(QUERY_KEY);
    
    // Process according to the selected query type:
    if (queryName != null)
    {
      if (queryName.equals(INCIDENTS_BY_PERSON))
      {
        try
        {
          String firstName = request.getParameter("firstName");
          String lastName = request.getParameter("lastName");
          
          incidents = incidentBean.getIncidentsByInvolvedPerson(firstName, lastName);
        }
        catch (SQLException sqle)
        {
          throw new IOException(sqle.getMessage(), sqle);
        }
      }
    }
    
    // Output the derived incident list as an HTML table:
    response.setContentType("text/plain");
      
    response.getWriter().println("<table class=\"table-bordered\">");
    response.getWriter().println("<tr>");
    response.getWriter().println("<th>ID</th>");
    response.getWriter().println("<th>Title</th>");
    response.getWriter().println("<th>Reported On</th>");
    response.getWriter().println("</tr>");
    
    if (incidents != null)
    {
      for (Incident i : incidents)
      {
        response.getWriter().println("<tr>");
        response.getWriter().println("<td>" + i.getId() + "</td>");
        response.getWriter().println("<td>" + i.getTitle() + "</td>");
        response.getWriter().println("<td>" + i.getReportedOn() + "</td>");
        response.getWriter().println("</tr>");
      }
    }
    
    response.getWriter().println("</table>");
	} // -- protected void doGet() -- //

} // -- public class IncidentQueryServlet -- //