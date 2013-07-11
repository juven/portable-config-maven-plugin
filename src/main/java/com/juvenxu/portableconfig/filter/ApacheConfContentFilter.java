package com.juvenxu.portableconfig.filter;

import com.juvenxu.portableconfig.ContentFilter;
import com.juvenxu.portableconfig.model.Replace;
import org.codehaus.plexus.component.annotations.Component;

import java.io.*;
import java.util.List;

/**
 * @author juven
 */

@Component(role = ContentFilter.class, hint = "apache-conf")
public class ApacheConfContentFilter implements ContentFilter
{
  @Override
  public boolean accept(String contentName)
  {
    return false;  //To change body of implemented methods use File | Settings | File Templates.
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

    for (Replace replace : replaces)
    {
      if ( line.matches("^" + replace.getKey() + "\\s+.+") )
      {
        return line.replaceAll("(\\s+)(.+)", "$1" + replace.getValue());
      }
    }

    return line;
  }
}
