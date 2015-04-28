package com.sappress.hcpbook.chp06.persistence;

public enum IncidentStatus
{
  NEW("NEW"),
  OPEN("OPEN"),
  CLOSED("CLOSED");
  
  private String name;
  
  IncidentStatus(String name)
  {
    this.name = name;
  }
  
  public String asString()
  {
    char[] chars = name.toCharArray();
    StringBuffer sb = new StringBuffer();
    
    for (int i = 0; i < chars.length; i++)
    {
      switch (i)
      {
        case 0:
          sb.append(chars[i]);
          break;
        default:
          sb.append(Character.toLowerCase(chars[i]));
      }
    }
    
    return sb.toString();
  }
} // -- public enum IncidentStatus -- //