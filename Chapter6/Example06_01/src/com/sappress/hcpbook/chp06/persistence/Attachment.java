package com.sappress.hcpbook.chp06.persistence;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;

@Entity
public class Attachment implements Serializable
{
  private static final long serialVersionUID = 1L;

  @Id
  private String id;
  
  @ManyToOne
  @JoinColumn(name="INCIDENT_ID", referencedColumnName="ID")
  private Incident incident;

  @Version
  private Long version;
  
  public Attachment() {}
  
  public Attachment(String objectId)
  {
    setId(objectId);
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
  
  public Long getVersion()
  {
    return version;
  }

} // -- End of class Attachment -- //