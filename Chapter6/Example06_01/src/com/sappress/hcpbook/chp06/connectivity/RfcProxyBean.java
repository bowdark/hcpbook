package com.sappress.hcpbook.chp06.connectivity;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import com.sap.conn.jco.AbapException;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoRepository;
import com.sap.conn.jco.JCoTable;
import com.sappress.hcpbook.chp06.persistence.Country;
import com.sappress.hcpbook.chp06.persistence.Region;

/**
 * Session Bean implementation class RfcProxyBean
 */
@Stateless
@LocalBean
public class RfcProxyBean 
{
  private static final String DEFAULT_DESTINATION = "ABAPOnPremise";
  
  public RfcProxyBean() { }

  /**
   * This method is used to lookup the master set of countries defined in the
   * on-premise SAP system.
   * 
   * @return
   * @throws AbapException
   * @throws JCoException
   */
  public List<Country> getCountries() throws AbapException, JCoException
  {
    // Method-Local Data Declarations:
    List<Country> countryList = new ArrayList<Country>();
    
    try
    {
      // Access the default JCo destination:
      JCoDestination jcoDest = 
        JCoDestinationManager.getDestination(DEFAULT_DESTINATION);
      
      // Call Z_RFC_GET_COUNTRIES to fetch the country list:
      JCoRepository jcoRepository = jcoDest.getRepository();
      JCoFunction jcoFunction = jcoRepository.getFunction("Z_RFC_GET_COUNTRIES");
      
      jcoFunction.execute(jcoDest);
      
      // Copy the results into the country list:
      JCoTable tabCountries = jcoFunction.getTableParameterList().getTable("COUNTRIES");
      for (int row = 0; row < tabCountries.getNumRows(); row++, tabCountries.nextRow())
        countryList.add(new Country(tabCountries.getString("LAND1"),
                                    tabCountries.getString("LANDX")));
    }
    catch (AbapException ae)
    {
      throw ae;
    }
    catch (JCoException je)
    {
      throw je;
    }
    
    // Return the result set:
    return countryList;
  } // -- public List getCountries() -- //
  
  /**
   * This method is used to lookup the master set of regions.
   * @return
   * @throws AbapException
   * @throws JCoException
   */
  public List<Region> getRegions()
    throws AbapException, JCoException
  {
    // Method-Local Data Declarations:
    List<Region> regionList = new ArrayList<Region>();
    
    try
    {
      // Access the default JCo destination:
      JCoDestination jcoDest = 
        JCoDestinationManager.getDestination(DEFAULT_DESTINATION);
      
      // Call Z_RFC_GET_REGIONS_BY_COUNTRY to fetch the region list:
      JCoRepository jcoRepository = jcoDest.getRepository();
      JCoFunction jcoFunction = jcoRepository.getFunction("Z_RFC_GET_REGIONS");
      jcoFunction.execute(jcoDest);
      
      // Copy the results into the region list:
      JCoTable tabRegions = jcoFunction.getTableParameterList().getTable("REGIONS");
      for (int row = 0; row < tabRegions.getNumRows(); row++, tabRegions.nextRow())
        regionList.add(new Region(tabRegions.getString("LAND1"),
                                  tabRegions.getString("BLAND"),
                                  tabRegions.getString("BEZEI")));
    }
    catch (AbapException ae)
    {
      throw ae;
    }
    catch (JCoException je)
    {
      throw je;
    }
    
    // Return the result set:
    return regionList;
  } // -- public List getRegions() -- //
  
  /**
   * This method is used to lookup the set of regions defined for a given country code.
   * @param country
   * @return
   * @throws AbapException
   * @throws JCoException
   */
  public List<Region> getRegionsByCountry(String country)
    throws AbapException, JCoException
  {
    // Method-Local Data Declarations:
    List<Region> regionList = new ArrayList<Region>();
    
    try
    {
      // Access the default JCo destination:
      JCoDestination jcoDest = 
        JCoDestinationManager.getDestination(DEFAULT_DESTINATION);
      
      // Call Z_RFC_GET_REGIONS_BY_COUNTRY to fetch the region list:
      JCoRepository jcoRepository = jcoDest.getRepository();
      JCoFunction jcoFunction = jcoRepository.getFunction("Z_RFC_GET_REGIONS_BY_COUNTRY");
      jcoFunction.getImportParameterList().setValue("COUNTRY_CODE", country);
      jcoFunction.execute(jcoDest);
      
      // Copy the results into the region list:
      JCoTable tabRegions = jcoFunction.getTableParameterList().getTable("REGIONS");
      for (int row = 0; row < tabRegions.getNumRows(); row++, tabRegions.nextRow())
        regionList.add(new Region(country,
                                  tabRegions.getString("BLAND"),
                                  tabRegions.getString("BEZEI")));
    }
    catch (AbapException ae)
    {
      throw ae;
    }
    catch (JCoException je)
    {
      throw je;
    }
    
    // Return the result set:
    return regionList;
  } // -- public List getRegionsByCountry() -- //

} // -- public class RfcProxyBean -- //