package com.sappress.hcpbook.chp06.document;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.apache.chemistry.opencmis.commons.exceptions.CmisNameConstraintViolationException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisRuntimeException;
import org.apache.commons.io.IOUtils;

import com.sap.ecm.api.EcmService;
import com.sap.ecm.api.RepositoryOptions;
import com.sap.ecm.api.RepositoryOptions.Visibility;
import com.sappress.hcpbook.chp06.persistence.Attachment;
import com.sappress.hcpbook.chp06.persistence.Incident;

/**
 * Session Bean implementation class AttachmentProcessorBean
 */
@Stateless
@LocalBean
public class AttachmentProcessorBean 
{  
  /**
   * Create a new file attachment
   * 
   * @param incidentId
   * @param fileName
   * @param mimeType
   * @param payload
   * @return
   * @throws IOException
   */
  public String createAttachment(String incidentId,
                                 String fileName,
                                 String mimeType,
                                 byte[] payload)
    throws IOException
  {
    try
    {
      // Find the incident folder:
      Session session = getCmisSession();
      Folder rootFolder = session.getRootFolder();
      Folder incFolder = null;
      
      ItemIterable<CmisObject> folders = rootFolder.getChildren();
      for (CmisObject obj : folders)
      {
        if (obj instanceof Folder)
        {
          incFolder = (Folder) obj;
          if (incFolder.getId().equals(incidentId))
            break;
        }
      }
      
      // If a folder doesn't exist, create one:
      if (incFolder == null)
      {
        Map<String, String> folderProps = new HashMap<String, String>();
        folderProps.put(PropertyIds.OBJECT_TYPE_ID, "cmis:folder");
        folderProps.put(PropertyIds.NAME, incidentId);
        
        incFolder = rootFolder.createFolder(folderProps);
      }
      
      // Create an attachment document:
      Map<String, Object> fileProps = new HashMap<String, Object>();
      fileProps.put(PropertyIds.OBJECT_TYPE_ID, "cmis:document");
      fileProps.put(PropertyIds.NAME, fileName);
      InputStream is = new ByteArrayInputStream(payload);
      
      ContentStream contentStream = 
        session.getObjectFactory().createContentStream(fileName, payload.length, mimeType, is);
      
      Document attDoc = incFolder.createDocument(fileProps,  contentStream, VersioningState.NONE);
      return attDoc.getId();
    }
    catch (CmisNameConstraintViolationException cncve)
    {
      throw new IOException("File " + fileName + " already exists.", cncve);
    }
    catch (Exception ex)
    {
      throw new IOException(ex.getMessage(), ex);
    }
  } // -- public String createAttachment() -- //
  
  /**
   * Remove an attachment from the repository
   * @param objectId
   */
  public void removeAttachment(String objectId)
  {
    Session session = getCmisSession();
    session.getObject(objectId).delete();
  } // -- public void removeAttachment() -- //
  
  /**
   * Retrieve a list of attachment document DTOs
   * @param attachments
   * @return
   */
  public List<DocumentDTO> getAttachmentsForIncident(Incident incident)
    throws IOException
  {
    Session session = getCmisSession();
    List<DocumentDTO> documents = new ArrayList<DocumentDTO>();
    
    try
    {
      for (Attachment att : incident.getAttachments())
        documents.add(getAttachmentById(att.getId(), session));
      
      return documents;
    }
    catch (IOException ioe)
    {
      throw ioe;
    }
    catch (Exception ex)
    {
      throw new IOException(ex.getMessage(), ex);
    }
  } // -- public List getAttachmentDocuments() -- //
  
  /**
   * Lookup an attachment by it's object id
   * @param id
   * @param session
   * @return
   * @throws IOException
   */
  public DocumentDTO getAttachmentById(String id, Session session)
    throws IOException
  {
    if (session == null)
      session = getCmisSession();
    
    org.apache.chemistry.opencmis.client.api.Document document =
      (org.apache.chemistry.opencmis.client.api.Document) session.getObject(id);
    
    DocumentDTO doc = new DocumentDTO();
    doc.setId(id);
    doc.setName(document.getContentStreamFileName());
    doc.setCreatedBy(document.getCreatedBy());
    doc.setSize(document.getContentStreamLength());
    doc.setMimeType(document.getContentStreamMimeType());

    byte[] payload = IOUtils.toByteArray(document.getContentStream().getStream());   
    doc.setPayload(payload);
    
    return doc;
  } // -- public DocumentDTO get AttachmentById() -- //

  /**
   * Obtain a CMIS session instance
   * @return
   */
  private Session getCmisSession()
  {
    EcmService ecmService = null;
    
    try
    {
      InitialContext ctx = new InitialContext();
      ecmService = (EcmService) ctx.lookup("java:comp/env/EcmService");
      return ecmService.connect(IRepositoryConstants.UNIQUE_NAME, IRepositoryConstants.SECRET_KEY);
    }
    catch (NamingException ne)
    {
      throw new CmisRuntimeException(ne.getMessage(), ne);
    }
    catch (CmisObjectNotFoundException confe)
    {
      RepositoryOptions options = new RepositoryOptions();
      options.setUniqueName(IRepositoryConstants.UNIQUE_NAME);
      options.setRepositoryKey(IRepositoryConstants.SECRET_KEY);
      options.setVisibility(Visibility.PROTECTED);
      ecmService.createRepository(options);
      
      return ecmService.connect(IRepositoryConstants.UNIQUE_NAME, IRepositoryConstants.SECRET_KEY);
    }
  } // -- private Session getCmisSession() -- //
  
} // -- End of class AttachmentProcessorBean -- //