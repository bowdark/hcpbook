package com.sappress.hcpbook.chp06.persistence;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name="COUNTRY")
@NamedQuery(name="AllCountries", query="select c from Country c")
public class Country implements Serializable
{
  private static final long serialVersionUID = 1L;

  @Id
  private String code;
  
  @Basic
  private String description;

  @Version
  private Long version;
  
  public Country() {}
  
  public Country(String code, String description)
  {
    setCode(code);
    setDescription(description);
  }
  
  public String getCode()
  {
    return code;
  }
  
  public void setCode(String code)
  {
    this.code = code;
  }
  
  public String getDescription()
  {
    return description;
  }
  
  public void setDescription(String description)
  {
    this.description = description;
  }
  
  public Long getVersion()
  {
    return version;
  }
}