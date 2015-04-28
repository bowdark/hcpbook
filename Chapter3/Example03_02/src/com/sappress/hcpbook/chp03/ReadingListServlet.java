package com.sappress.hcpbook.chp03;

import java.io.IOException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * This Servlet provides a simple, in-memory database for storing a user's
 * reading list. It's primitive implementation is for demonstrative purposes
 * only. We'll learn how to deal with real-world persistence in Chapter 5.
 */
@WebServlet("/ReadingListServlet")
public class ReadingListServlet extends HttpServlet
{
  // Constant Declarations:
  private static final long serialVersionUID = 1L;
    
  /**
   * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
   */
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    // Fetch the EJB list manager:
    ReadingListManagerBeanLocal readingListManager = getListManager(request.getSession());
    
    // Retrieve the target action from the request parameters:
    String action = request.getParameter("action");
    if ((action != null) && (action.equals("Delete")))
    {
      String title = request.getParameter("title");
      readingListManager.removeTitle(title);
      
      response.sendRedirect("index.jsp");
    }
    else
    {
      RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
      rd.forward(request, response);
    }
  } // -- protected void doGet() -- //

  /**
   * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
  {
    // Fetch the EJB list manager:
    ReadingListManagerBeanLocal readingListManager = getListManager(request.getSession());
    
    // Add the selected title to the list:
    String title = request.getParameter("title");
    if ((title != null) && (! title.equals("")))
      readingListManager.addTitle(title);
        
    // Re-route the user back to the main reading list form:
    RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
    rd.forward(request, response);
  } // -- protected void doPost() -- //
  
  /**
   * Fetch the session EJB from the session context.
   * @param session
   * @return
   */
  private ReadingListManagerBeanLocal getListManager(HttpSession session)
  {
    // Access/create the reading list manager EJB from the session context:   
    ReadingListManagerBeanLocal readingListManager = null;
    readingListManager = (ReadingListManagerBeanLocal) session.getAttribute("readingListManager");
    if (readingListManager == null)
    {
      try
      {
        // If the EJB hasn't been created yet, go ahead and create it:
        InitialContext ctx = new InitialContext();
        readingListManager = (ReadingListManagerBeanLocal) ctx.lookup("java:comp/env/ejb/ReadingListManager");
        
        readingListManager.addTitle("OData and SAP NetWeaver Gateway");
        readingListManager.addTitle("SAP HANA: An Introduction");
        readingListManager.addTitle("SuccessFactors with SAP ERP HCM");
        
        session.setAttribute("readingListManager", readingListManager);
      }
      catch (NamingException ne)
      {
        ne.printStackTrace();
      }
    }
    
    return readingListManager;
  } // -- private ReadingListManagerBeanLocal getListManager() -- //

} // -- End of class ReadingListServlet -- //