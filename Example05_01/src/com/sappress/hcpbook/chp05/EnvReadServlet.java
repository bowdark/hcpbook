package com.sappress.hcpbook.chp05;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Servlet implementation class EnvReadServlet
 */
@SuppressWarnings("serial")
public class EnvReadServlet extends HttpServlet 
{
  // Create a logger instance:
  private static Logger logger = 
    LoggerFactory.getLogger(EnvReadServlet.class);

  @Override
  public void init() throws ServletException
  {
    logger.debug("Servlet EnvReadServlet has been initialized successfully.");
  }
  
  /**
   * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
   */
  protected void doGet(HttpServletRequest request, HttpServletResponse response) 
    throws ServletException, IOException 
  {
    response.setContentType("text/plain");
    
    response.getWriter().println("Server Environment Variables:\n\n");
    
    Map<String, String> envVarMap = System.getenv();
    for (String key : envVarMap.keySet())
    {
      response.getWriter().println("Variable [" + key + "]: " + envVarMap.get(key));
      logger.info("Variable [" + key + "]: " + envVarMap.get(key));
    }
  }
  
  public void destroy()
  {
    logger.debug("Servlet EnvReadServlet is being destroyed.");
  }
}