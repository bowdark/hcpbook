package com.sappress.hcpbook.chp06.document;

import javax.servlet.Servlet;

import com.sap.ecm.api.AbstractCmisProxyServlet;

/**
 * Servlet implementation class CMISProxyServlet
 */
@SuppressWarnings("serial")
public class CMISProxyServlet extends AbstractCmisProxyServlet implements Servlet 
{

  @Override
  protected String getRepositoryKey()
  {
    return IRepositoryConstants.SECRET_KEY;
  }

  @Override
  protected String getRepositoryUniqueName()
  {
    return IRepositoryConstants.UNIQUE_NAME;
  }
}