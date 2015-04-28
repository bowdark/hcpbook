package com.sappress.hcpbook.chp06.persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import org.json.simple.JSONObject;

@Entity
@NamedQuery(name="AllIncidents", query="select i from Incident i")
public class Incident implements Serializable
{
  private static final long serialVersionUID = 1L;
  
  @Id
  private String id;
  
  @Basic
  private String title = "";
  
  @Basic
  private String description = "";
  
  @Basic
  @Temporal(TemporalType.DATE)
  private Date reportedOn;
   
  @OneToOne(cascade=CascadeType.PERSIST)
  @JoinColumn(name="LOCATION_ID", referencedColumnName="ID")
  private Location location = new Location();
  
  @OneToMany(mappedBy="incident", cascade={CascadeType.ALL})
  private List<InvolvedPerson> involvedPersons = new ArrayList<InvolvedPerson>();
  
  @OneToMany(mappedBy="incident", cascade={CascadeType.ALL})
  private List<Attachment> attachments = new ArrayList<Attachment>();

  @Version
  private Long version;
  
  public Incident()
  {
    setId(UUID.randomUUID().toString());
    setReportedOn(new java.util.Date());
  }

  public String getId()
  {
    return id;
  }

  public void setId(String id)
  {
    this.id = id;
  }

  public String getTitle()
  {
    return title;
  }

  public void setTitle(String title)
  {
    this.title = title;
  }

  public String getDescription()
  {
    return description;
  }

  public void setDescription(String description)
  {
    this.description = description;
  }
  
  public Location getLocation()
  {
    if (location == null)
      location = new Location();
    
    return location;
  }

  public void setLocation(Location location)
  {
    this.location = location;
  }

  public Date getReportedOn()
  {
    return reportedOn;
  }

  public void setReportedOn(Date reportedOn)
  {
    this.reportedOn = reportedOn;
  }
  
  public Long getVersion()
  {
    return version;
  }

  public List<InvolvedPerson> getInvolvedPersons()
  {
    return involvedPersons;
  }

  public void setInvolvedPersons(List<InvolvedPerson> involvedPersons)
  {
    this.involvedPersons = involvedPersons;
  }
  
  public void addInvolvedPerson(InvolvedPerson person)
  {
    if (! involvedPersons.contains(person))
    {
      person.setIncident(this);
      involvedPersons.add(person);
    }
  }
  
  public boolean removeInvolvedPerson(InvolvedPerson person)
  {
    return involvedPersons.remove(person);
  }
  
  public boolean removeInvolvedPerson(String personId)
  {
    for (InvolvedPerson person : involvedPersons)
    {
      if (person.getId().equals(personId))
      {
        removeInvolvedPerson(person);
        return true;
      }
    }
    
    return false;
  }

  public List<Attachment> getAttachments()
  {
    return attachments;
  }

  public void setAttachments(List<Attachment> attachments)
  {
    this.attachments = attachments;
  }
  
  public void addAttachment(Attachment attachment)
  {
    if (! attachments.contains(attachment))
    {
      attachment.setIncident(this);
      attachments.add(attachment);
    }
  }
  
  public boolean removeAttachment(Attachment attachment)
  {
    return attachments.remove(attachment);
  }
  
  public boolean removeAttachment(String attachmentId)
  {
    for (Attachment a : attachments)
    {
      if (a.getId().equals(attachmentId))
      {
        removeAttachment(a);
        return true;
      }
    }
    
    return false;
  }
  
  public void copyFromJSON(JSONObject incident)
  {
    // Copy incident-level fields:
    setTitle((String) incident.get("title"));
    setDescription((String) incident.get("description"));
    
    // Copy location fields:
    JSONObject location = (JSONObject) incident.get("location");
    this.location.setName((String) location.get("name"));
    this.location.setDescription((String) location.get("description"));
    
    // Copy location address fields:
    JSONObject address = (JSONObject) location.get("address");
    this.location.getAddress().setStreet1((String) address.get("street1"));
    this.location.getAddress().setStreet2((String) address.get("street2"));
    this.location.getAddress().setCity((String) address.get("city"));
    this.location.getAddress().setRegion((String) address.get("region"));
    this.location.getAddress().setCountry((String) address.get("country"));
    this.location.getAddress().setPostalCode((String) address.get("postalCode"));
  } // -- public void copyFromJSON() -- //

} // -- public class Incident -- //