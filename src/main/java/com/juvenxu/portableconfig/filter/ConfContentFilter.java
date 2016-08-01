package com.juvenxu.portableconfig.filter;

import com.juvenxu.portableconfig.ContentFilter;
import com.juvenxu.portableconfig.model.Replace;
import org.codehaus.plexus.component.annotations.Component;

import java.io.*;
import java.util.List;

/**
 * @author juven
 */
@Component(role = ContentFilter.class, hint = "conf")
public class ConfContentFilter extends LineBasedContentFilter
{
  @Override
  public boolean accept(String contentType)
  {
    return ".conf".equals(contentType);
  }

  @Override
  protected String filterLine(String line, List<Replace> replaces)
  {
    if (line.startsWith("#"))
    {
      return line;
    }

    if (line.contains("="))
    {
      int pos = line.indexOf("=");
      String key = line.substring(0, pos);

      for (Replace replace : replaces)
      {
        if (replace.getKey().equals(key))
        {
          return key + "=" + replace.getValue();
        }
      }
    }

    return line;
  }

  @Override
  public List<String> getReplacedEntry() {
    return null;
  }
}
