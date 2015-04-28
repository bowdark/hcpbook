package com.sappress.hcpbook.chp06.persistence;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name="LOCATION")
public class Location implements Serializable
{
  private static final long serialVersionUID = 1L;

  @Id
  private String id;

  @Basic
  private String name = "";
  
  @Basic
  private String description = "";
  
  @OneToOne(cascade=CascadeType.PERSIST)
  @JoinColumn(name="ADDRESS_ID", referencedColumnName="ID")
  private Address address = new Address();

  @Version
  private Long version;
  
  public Location()
  {
    setId(UUID.randomUUID().toString());
  }
  
  public String getId()
  {
    return id;
  }

  public void setId(String id)
  {
    this.id = id;
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public String getDescription()
  {
    return description;
  }

  public void setDescription(String description)
  {
    this.description = description;
  }
  
  public Address getAddress()
  {
    if (address == null)
      address = new Address();
    
    return address;
  }

  public void setAddress(Address address)
  {
    this.address = address;
  }
  
  public Long getVersion()
  {
    return version;
  }

  public String toString()
  {
    if (getDescription() != null)
      return getName() + " (" + getDescription() + ")";
    else
      return getName();
  }
  
} // -- public class Location -- //