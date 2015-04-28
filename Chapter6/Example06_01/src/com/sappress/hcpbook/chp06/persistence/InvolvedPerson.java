package com.sappress.hcpbook.chp06.persistence;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Version;

import org.json.simple.JSONObject;

@Entity
public class InvolvedPerson implements Serializable
{
  private static final long serialVersionUID = 1L;

  @Id
  private String id;
  
  @ManyToOne
  @JoinColumn(name="INCIDENT_ID", referencedColumnName="ID")
  private Incident incident;
  
  @Basic
  private String firstName;
  
  @Basic
  private String lastName;
  
  @OneToOne(cascade=CascadeType.PERSIST)
  @JoinColumn(name="ADDRESS_ID", referencedColumnName="ID")
  private Address address;
  
  @Basic
  private String contactNumber;
  
  @Basic
  private String emailAddress;

  @Version
  private Long version;
  
  public InvolvedPerson()
  {
    setId(UUID.randomUUID().toString());
  }
  
  public InvolvedPerson(JSONObject jsonObject)
  {
    setId(UUID.randomUUID().toString());
    setFirstName((String) jsonObject.get("firstName"));
    setLastName((String) jsonObject.get("lastName"));
    setContactNumber((String) jsonObject.get("contactNumber"));
    setEmailAddress((String) jsonObject.get("emailAddress"));
  }

  public String getId()
  {
    return id;
  }

  public void setId(String id)
  {
    this.id = id;
  }

  public Incident getIncident()
  {
    return incident;
  }

  public void setIncident(Incident incident)
  {
    this.incident = incident;
  }

  public String getFirstName()
  {
    return firstName;
  }

  public void setFirstName(String firstName)
  {
    this.firstName = firstName;
  }

  public String getLastName()
  {
    return lastName;
  }

  public void setLastName(String lastName)
  {
    this.lastName = lastName;
  }

  public Address getAddress()
  {
    return address;
  }

  public void setAddress(Address address)
  {
    this.address = address;
  }

  public String getContactNumber()
  {
    return contactNumber;
  }

  public void setContactNumber(String contactNumber)
  {
    this.contactNumber = contactNumber;
  }

  public String getEmailAddress()
  {
    return emailAddress;
  }

  public void setEmailAddress(String emailAddress)
  {
    this.emailAddress = emailAddress;
  }
  
  public Long getVersion()
  {
    return version;
  }
  
} // -- public class InvolvedPerson -- //