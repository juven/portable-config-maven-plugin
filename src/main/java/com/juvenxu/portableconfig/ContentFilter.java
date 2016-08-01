package com.juvenxu.portableconfig;

import com.juvenxu.portableconfig.model.Replace;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * @author juven
 */
public interface ContentFilter extends  IReplaceRecord
{
  boolean accept(String contentType);

  void filter(InputStream fileIS, OutputStream tmpOS, List<Replace> replaces) throws IOException;



}

