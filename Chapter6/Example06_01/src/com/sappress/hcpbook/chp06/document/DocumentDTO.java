package com.sappress.hcpbook.chp06.document;

import java.io.Serializable;

public class DocumentDTO implements Serializable
{
  private static final long serialVersionUID = 1L;

  private String id;
  private String name;
  private String createdBy;
  private long size;
  private String mimeType;
  private byte[] payload;
  
  public DocumentDTO()
  {
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

  public String getCreatedBy()
  {
    return createdBy;
  }

  public void setCreatedBy(String createdBy)
  {
    this.createdBy = createdBy;
  }

  public long getSize()
  {
    return size;
  }

  public void setSize(long size)
  {
    this.size = size;
  }

  public byte[] getPayload()
  {
    return payload;
  }

  public void setPayload(byte[] payload)
  {
    this.payload = payload;
    setSize(payload.length);
  }

  public String getMimeType()
  {
    return mimeType;
  }

  public void setMimeType(String mimeType)
  {
    this.mimeType = mimeType;
  }
  
  public String toJSON()
  {
    return "{\"id\": \"" + getId() + "\"," +
            "\"name\": \"" + getName() + "\"," +
            "\"createdBy\": \"" + getCreatedBy() + "\"," +
            "\"size\": \"" + getSize() + "\"," +
            "\"mimeType\": \"" + getMimeType() + "\"}";
  }
  
} // -- End of class DocumentDTO -- //