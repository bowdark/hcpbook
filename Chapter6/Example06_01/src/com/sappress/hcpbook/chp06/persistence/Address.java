package com.sappress.hcpbook.chp06.persistence;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

import com.sap.security.core.server.csi.util.URLEncoder;

@Entity
@Table(name="ADDRESS")
public class Address implements Serializable
{
  private static final long serialVersionUID = 1L;

  @Id
  private String id;

  @Basic
  private String street1 = "";
  
  @Basic
  private String street2 = "";
  
  @Basic
  private String city = "";
  
  @Basic
  private String region = "";
  
  @Basic
  private String country = "";
  
  @Basic
  private String postalCode = "";
  
  @Basic
  private Double longitude = new Double(0.00);
  
  @Basic
  private Double latitude = new Double(0.00);

  @Version
  private Long version;
  
  public Address()
  {
    setId(UUID.randomUUID().toString());
    setCountry("US");
  }

  public String getId()
  {
    return id;
  }

  public void setId(String id)
  {
    this.id = id;
  }
  
  public String getStreet1()
  {
    return street1;
  }

  public void setStreet1(String street1)
  {
    this.street1 = street1;
  }

  public String getStreet2()
  {
    return street2;
  }

  public void setStreet2(String street2)
  {
    this.street2 = street2;
  }

  public String getCity()
  {
    return city;
  }

  public void setCity(String city)
  {
    this.city = city;
  }

  public String getRegion()
  {
    return region;
  }

  public void setRegion(String region)
  {
    this.region = region;
  }

  public String getCountry()
  {
    return country;
  }

  public void setCountry(String country)
  {
    this.country = country;
  }

  public String getPostalCode()
  {
    return postalCode;
  }

  public void setPostalCode(String postalCode)
  {
    this.postalCode = postalCode;
  }
  
  public Double getLongitude()
  {
    return longitude;
  }

  public void setLongitude(Double longitude)
  {
    this.longitude = longitude;
  }

  public Double getLatitude()
  {
    return latitude;
  }

  public void setLatitude(Double latitude)
  {
    this.latitude = latitude;
  }

  public Long getVersion()
  {
    return version;
  }
  
  public String toString()
  {
    StringBuffer buffer = new StringBuffer();
    
    if (getStreet1() != null)
      buffer.append(getStreet1());
    
    if (getStreet2() != null)
      buffer.append("\n" + getStreet2());
    
    if (getCity() != null)
    {
      buffer.append("\n" + getCity());
    
      if (getRegion() != null)
        buffer.append(", " + getRegion());
      
      if (getPostalCode() != null)
        buffer.append(" " + getPostalCode());
      
      if (getCountry() != null)
        buffer.append(" (" + getCountry() + ")");
    }
    else
    {
      if (getRegion() != null)
        buffer.append("\n" + getRegion());
      
      if (getPostalCode() != null)
        buffer.append(" " + getPostalCode());
      
      if (getCountry() != null)
        buffer.append(" (" + getCountry() + ")");
    }
    
    return buffer.toString();
  }

    public String getAddressString() throws UnsupportedEncodingException
    {
      StringBuffer addressString = new StringBuffer();
      addressString.append(getStreet1() + getStreet2() + ", " +
                           getCity() + ", " + getRegion() + ", " + getCountry());
      return URLEncoder.encode(addressString.toString(), "UTF-8");
    }
} // -- public class Address -- //