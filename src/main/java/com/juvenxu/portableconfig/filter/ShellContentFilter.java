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
  public void filter(InputStream inputStream, OutputStream outputStream, List<Replace> replaces) throws IOException
  {
    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));

    while (reader.ready())
    {
      String line = reader.readLine();

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

      writer.write(line);
      writer.newLine();
    }

    writer.flush();
  }

}
