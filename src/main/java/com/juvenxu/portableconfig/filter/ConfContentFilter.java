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
public class ConfContentFilter implements ContentFilter
{
  @Override
  public boolean accept(String contentName)
  {
    return contentName.endsWith(".conf");
  }

  @Override
  public void filter(Reader reader, Writer writer, List<Replace> replaces) throws IOException
  {
    BufferedReader bufferedReader = new BufferedReader(reader);
    BufferedWriter bufferedWriter = new BufferedWriter(writer);

    while (bufferedReader.ready())
    {
      String line = bufferedReader.readLine();

      if (line == null)
      {
        break;
      }

      bufferedWriter.write(filterLine(replaces, line));
      bufferedWriter.newLine();
    }

    bufferedWriter.flush();
  }

  private String filterLine(List<Replace> replaces, String line)
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
}
