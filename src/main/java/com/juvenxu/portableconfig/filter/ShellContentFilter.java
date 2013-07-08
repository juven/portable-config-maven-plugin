package com.juvenxu.portableconfig.filter;

import com.juvenxu.portableconfig.ContentFilter;
import com.juvenxu.portableconfig.model.Replace;
import org.codehaus.plexus.component.annotations.Component;

import java.io.*;
import java.util.List;

/**
 * @author juven
 */
@Component(role = ContentFilter.class, hint = "shell")
public class ShellContentFilter implements ContentFilter
{
  @Override
  public boolean accept(String contentName)
  {
    return contentName.endsWith(".sh");
  }

  @Override
  public void filter(Reader reader, Writer writer, List<Replace> replaces) throws IOException
  {
    BufferedReader bufferedReader = new BufferedReader(reader);
    BufferedWriter bufferedWriter = new BufferedWriter(writer);

    while (bufferedReader.ready())
    {
      String line = bufferedReader.readLine();

      if ( line == null )
      {
        break;
      }

      for (Replace replace : replaces)
      {
        if (line.matches("^(export\\s+){0,1}" + replace.getKey() + "=\"[^\"=]*\""))
        {
          line = line.replaceAll("=\"[^\"=]*\"", "=\"" + replace.getValue() + "\"");

          break;
        }

        if (line.matches("^(export\\s+){0,1}" + replace.getKey() + "='[^'=]*'"))
        {
          line = line.replaceAll("='[^'=]*'", "='" + replace.getValue() + "'");

          break;
        }

        if (line.matches("^(export\\s+){0,1}" + replace.getKey() + "=[^'\"=]*"))
        {
          line = line.replaceAll("=[^'\"=]*", "=" + replace.getValue());

          break;
        }
      }

      bufferedWriter.write(line);
      bufferedWriter.newLine();
    }

    bufferedWriter.flush();
  }

}
