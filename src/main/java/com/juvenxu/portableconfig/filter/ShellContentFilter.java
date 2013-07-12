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
public class ShellContentFilter extends LineBasedContentFilter
{
  @Override
  public boolean accept(String contentName)
  {
    return contentName.endsWith(".sh");
  }

  @Override
  protected String filterLine(String line, List<Replace> replaces)
  {
    for (Replace replace : replaces)
    {
      if (line.matches("^(export\\s+){0,1}" + replace.getKey() + "=\"[^\"=]*\""))
      {
        return line.replaceAll("=\"[^\"=]*\"", "=\"" + replace.getValue() + "\"");
      }

      if (line.matches("^(export\\s+){0,1}" + replace.getKey() + "='[^'=]*'"))
      {
        return line.replaceAll("='[^'=]*'", "='" + replace.getValue() + "'");
      }

      if (line.matches("^(export\\s+){0,1}" + replace.getKey() + "=[^'\"=]*"))
      {
        return  line.replaceAll("=[^'\"=]*", "=" + replace.getValue());
      }
    }

    return line;
  }

}
