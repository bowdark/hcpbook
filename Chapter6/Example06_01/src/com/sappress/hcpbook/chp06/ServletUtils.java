package com.sappress.hcpbook.chp06;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletRequest;

public class ServletUtils
{
  /**
   * Parse out a JSON payload
   * @param request
   * @return
   * @throws IOException
   */
  @SuppressWarnings("unused")
  public static String getJSONPayload(HttpServletRequest request)
    throws IOException
  {
    BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
    if (br != null)
      return br.readLine();

    return "";
  } // -- public static String getJSONPayload() -- //
}