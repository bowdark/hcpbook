package com.sappress.hcpbook.chp06.persistence;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@SuppressWarnings("serial")
@Embeddable
public class RegionPK implements Serializable
{
  @Column(name="COUNTRY_CODE", insertable=true, nullable=false)
  private String countryCode;
  
  @Column(name="CODE", insertable=true, nullable=false)
  private String code;

  public RegionPK() { }
  
  public RegionPK(String countryCode, String code)
  {
    setCountryCode(countryCode);
    setCode(code);
  }
  
  public String getCountryCode()
  {
    return countryCode;
  }

  public void setCountryCode(String countryCode)
  {
    this.countryCode = countryCode;
  }

  public String getCode()
  {
    return code;
  }

  public void setCode(String code)
  {
    this.code = code;
  }
  
  public boolean equals(Object obj)
  {
    if (! (obj instanceof RegionPK))
      return false;
    
    RegionPK rpk = (RegionPK) obj;
    return this.countryCode.equals(rpk.countryCode) && this.code.equals(rpk.code);
  }
  
  public int hashCode()
  {
    return this.countryCode.hashCode() ^ this.code.hashCode();
  }
  
}