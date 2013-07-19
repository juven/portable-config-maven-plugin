package com.juvenxu.portableconfig;

import com.juvenxu.portableconfig.model.Replace;

import java.io.*;
import java.util.List;

/**
 * @author juven
 */
public interface ContentFilter
{
  boolean accept(String contentName);

  void filter(Reader reader, Writer writer, List<Replace> replaces) throws IOException;
}
