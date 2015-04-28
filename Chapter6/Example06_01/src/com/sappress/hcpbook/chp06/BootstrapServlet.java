package com.sappress.hcpbook.chp06;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.sappress.hcpbook.chp06.persistence.JPAUtilities;

/**
 * Servlet implementation class BootstrapServlet
 */
public class BootstrapServlet extends HttpServlet 
{
  private static final long serialVersionUID = 1L;

  @PersistenceContext
  private EntityManager em;

  @Override
  public void init() throws ServletException
  {
    JPAUtilities.setEntityManagerFactory(em.getEntityManagerFactory());
  }
}