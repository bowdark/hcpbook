package com.sappress.hcpbook.chp06.persistence;

import javax.persistence.EntityManagerFactory;

public class JPAUtilities
{
  private static EntityManagerFactory emf;
  
  public static EntityManagerFactory getEntityManagerFactory()
  {
    if (emf == null)
      throw new IllegalArgumentException("EntityManagerFactory is not initialized.");
    
    return emf;
  }
  
  public static void setEntityManagerFactory(EntityManagerFactory emf)
  {
    JPAUtilities.emf = emf;
  }
}