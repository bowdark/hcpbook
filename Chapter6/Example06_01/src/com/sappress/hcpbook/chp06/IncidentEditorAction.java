package com.sappress.hcpbook.chp06;

public enum IncidentEditorAction
{
  CREATE_INCIDENT,
  EDIT_INCIDENT,
  SAVE_INCIDENT,
  REMOVE_INCIDENT,
  ADD_INVOLVED_PERSON,
  REMOVE_INVOLVED_PERSON,
  ADD_ATTACHMENT,
  GET_ATTACHMENT,
  REMOVE_ATTACHMENT,
  GET_GEOCOORDINATES;
  
  public static IncidentEditorAction getActionForUrlParameter(String parameter)
  {
    if (parameter == null)
      return null;
    else   
      return IncidentEditorAction.valueOf(translateParameter(parameter));
  }
  
  public static String translateParameter(String parameter)
  {
    char[] characters = parameter.toCharArray();
    StringBuffer name = new StringBuffer();
    
    for (int i = 0; i < characters.length; i++)
    {
      if (Character.isUpperCase(characters[i]))
        name.append("_");
      
      name.append(characters[i]);
    }
    
    return name.toString().toUpperCase();
  }
}