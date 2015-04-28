package com.sappress.hcpbook.chp06.connectivity;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.naming.InitialContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Session Bean implementation class EmailAgentBean
 */
@Stateless
@LocalBean
public class EmailAgentBean 
{
  private static final Logger logger = LoggerFactory.getLogger(EmailAgentBean.class);
  
  //@Resource(name="mail/Session")
  //private Session mailSession;
  
  /**
   * Send an HTML e-mail message
   * @param sender
   * @param receiver
   * @param subject
   * @param body
   * @throws MessagingException
   */
  public void sendMessage(String sender,
                          String receiver,
                          String subject,
                          String body)
    throws MessagingException
  {
    Transport transport = null;
    
    try
    {
      InitialContext ctx = new InitialContext();
      Session mailSession = (Session) ctx.lookup("java:comp/env/mail/Session");
      
      MimeMessage message = new MimeMessage(mailSession);
      
      InternetAddress[] recipients = InternetAddress.parse(receiver);
      message.setFrom(new InternetAddress(sender));
      message.setRecipients(RecipientType.TO, recipients);
      message.setSubject(subject, "UTF-8");
      
      Multipart multipart = new MimeMultipart("alternative");
      MimeBodyPart htmlPart = new MimeBodyPart();
      htmlPart.setContent(body, "text/html");
      multipart.addBodyPart(htmlPart);
      message.setContent(multipart);
      
      transport = mailSession.getTransport();
      transport.connect();
      transport.sendMessage(message, message.getAllRecipients());
    }
    catch (Exception ex)
    {
      logger.error(ex.getMessage());
      throw new MessagingException(ex.getMessage(), ex);
    }
    finally
    {
      if (transport != null)
      {
        try
        {
          transport.close();
        }
        catch (MessagingException me) { }
      }
    }
  } // -- public void sendMessage() -- //

} // -- public class EmailAgentBean -- //