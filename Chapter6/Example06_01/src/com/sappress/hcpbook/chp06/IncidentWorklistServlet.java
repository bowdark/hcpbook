package com.sappress.hcpbook.chp06;

import java.io.IOException;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sappress.hcpbook.chp06.persistence.Incident;
import com.sappress.hcpbook.chp06.persistence.IncidentBean;

/**
 * Servlet implementation class IncidentWorklistServlet
 */
@WebServlet("/IncidentWorklistServlet")
public class IncidentWorklistServlet extends HttpServlet 
{
	private static final long serialVersionUID = 1L;

	@EJB
	private IncidentBean incidentBean;
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	  throws ServletException, IOException 
	{
	  // Load the incident worklist and direct the user to the worklist editor:
    List<Incident> incidents = incidentBean.getAllIncidents(); 
    request.setAttribute("incidents", incidents);
    
    RequestDispatcher rd = request.getRequestDispatcher("IncidentWorklist.jsp");
    rd.forward(request, response);
	} // -- protected void doGet() -- //

} // -- public class IncidentWorklistServlet -- //