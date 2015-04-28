package com.sappress.hcpbook.chp06.persistence;

import org.apache.olingo.odata2.jpa.processor.api.ODataJPAContext;
import org.apache.olingo.odata2.jpa.processor.api.ODataJPAServiceFactory;
import org.apache.olingo.odata2.jpa.processor.api.exception.ODataJPARuntimeException;

public class ODataServiceFactory extends ODataJPAServiceFactory
{
  private static final String PUNIT_NAME = "jpaIncidentPU";
  
  @Override
  public ODataJPAContext initializeODataJPAContext()
    throws ODataJPARuntimeException
  {
    ODataJPAContext context = getODataJPAContext();
    context.setEntityManagerFactory(JPAUtilities.getEntityManagerFactory());
    context.setPersistenceUnitName(PUNIT_NAME);
    setDetailErrors(true);
    
    return context;
  }
}