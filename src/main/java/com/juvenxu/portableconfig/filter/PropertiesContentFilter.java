package com.juvenxu.portableconfig.filter;

import com.juvenxu.portableconfig.ContentFilter;
import com.juvenxu.portableconfig.model.Replace;
import org.codehaus.plexus.component.annotations.Component;

import java.io.*;
import java.util.List;
import java.util.Properties;

/**
 * @author juven
 */
@Component(role = ContentFilter.class, hint = "properties")
public class PropertiesContentFilter implements ContentFilter
{
  @Override
  public boolean accept(String contentName)
  {
    return contentName.endsWith(".properties");
  }

  @Override
  public void filter(Reader reader, Writer writer, List<Replace> replaces) throws IOException
  {
    Properties properties = new Properties();

    properties.load(reader);

    for (Replace replace : replaces)
    {
      if (properties.containsKey(replace.getKey()))
      {
        properties.setProperty(replace.getKey(), replace.getValue());
      }
    }

    properties.store(writer, null);
  }
}
