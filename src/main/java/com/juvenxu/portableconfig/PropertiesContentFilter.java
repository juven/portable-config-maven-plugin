package com.juvenxu.portableconfig;

import org.codehaus.plexus.component.annotations.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
  public void filter(InputStream inputStream, OutputStream outputStream, List<Replace> replaces) throws IOException
  {
    Properties properties = new Properties();

    properties.load(inputStream);

    for (Replace replace : replaces)
    {
      if (properties.containsKey(replace.getKey()))
      {
        properties.setProperty(replace.getKey(), replace.getValue());
      }
    }

    properties.store(outputStream, null);
  }
}
