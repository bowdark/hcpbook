package com.sappress.hcpbook.chp06.persistence;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 * Session Bean implementation class ConfigurationManagerBean
 */
@Stateless
@LocalBean
public class ConfigurationManagerBean 
{
  @PersistenceContext
  private EntityManager em;

  @SuppressWarnings("unchecked")
  public List<Country> getCountries()
  {
    return em.createNamedQuery("AllCountries").getResultList();
  } // -- public List getCountries() -- //
  
  public List<Region> getRegionsByCountry(String countryCode)
  {
    TypedQuery<Region> query =
      em.createQuery("SELECT r FROM Region r WHERE r.regionPK.countryCode = ?1", Region.class);
    query.setParameter(1, countryCode);
    
    return query.getResultList();
  } // -- public List getRegionsByCountry) -- //
  
  public void addCountry(Country country)
  {
    em.persist(country);
    em.flush();
  } // -- public void addCountry() -- //
  
  public void addRegion(Region region)
  {
    em.persist(region);
    em.flush();
  }
  
  public void refreshDatabase()
  {
    Query qCountries = em.createNativeQuery("DELETE FROM Country");
    qCountries.executeUpdate();
    
    Query qRegions = em.createNativeQuery("DELETE FROM Region");
    qRegions.executeUpdate();
  }
  
} // -- public class ConfigurationManagerBean -- //