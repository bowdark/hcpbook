package com.sappress.hcpbook.chp06.persistence;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name="REGION")
public class Region implements Serializable
{
  private static final long serialVersionUID = 1L;

  @EmbeddedId
  protected RegionPK regionPK = new RegionPK();
  
  @Basic
  private String description;

  @Version
  private Long version;
  
  public Region() {}
  
  public Region(String countryCode, String code, String description)
  {
    regionPK = new RegionPK(countryCode, code);
    setDescription(description);
  }
  
  public String getCountryCode()
  {
    return regionPK.getCountryCode();
  }
  
  public String getCode()
  {
    return regionPK.getCode();
  }
  
  public String getDescription()
  {
    return description;
  }
  
  public void setDescription(String description)
  {
    this.description = description;
  }

  public RegionPK getRegionPK()
  {
    return this.regionPK;
  }
  
  public Long getVersion()
  {
    return version;
  }
}