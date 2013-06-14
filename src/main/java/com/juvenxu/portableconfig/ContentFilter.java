package com.juvenxu.portableconfig;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * @author juven
 */
public interface ContentFilter
{
  boolean accept(String contentName);

  void filter(InputStream inputStream, OutputStream outputStream, List<Replace> replaces) throws IOException;
}
