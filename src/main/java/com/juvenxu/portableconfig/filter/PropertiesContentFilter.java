package com.juvenxu.portableconfig.filter;

import com.juvenxu.portableconfig.ContentFilter;
import com.juvenxu.portableconfig.IReplaceRecord;
import com.juvenxu.portableconfig.model.Replace;
import org.codehaus.plexus.component.annotations.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author juven
 */
@Component(role = ContentFilter.class, hint = "properties")
public class PropertiesContentFilter extends LineBasedContentFilter implements IReplaceRecord
{

  protected  List<String> replacedEntries = new ArrayList<String>();
  @Override
  public boolean accept(String contentType)
  {
    return ".properties".equals(contentType);
  }
  static List<Replace> checkword= new ArrayList<Replace>();
  @Override
  protected String filterLine(String line, List<Replace> replaces)
  {
    if (isComment(line))
    {
      return line;
    }
    checkword=replaces;
    final String equalsMark = "=";
    final int equalsMarkIndex = line.indexOf(equalsMark);
    final String colonMark = ":";
    final int colonMarkIndex = line.indexOf(colonMark);

    if (equalsMarkIndex != -1)
    {
      return replaceLineWith(line, replaces, equalsMark, equalsMarkIndex);
    }

    if (colonMarkIndex != -1)
    {
      return replaceLineWith(line, replaces, colonMark, colonMarkIndex);
    }

    return line;
  }

  private String replaceLineWith(String line, List<Replace> replaces, String equalsMark, int equalsMarkIndex)
  {
    String key = line.substring(0, equalsMarkIndex).trim();
    int i=0;
    for (final Replace replace : replaces)
    {
      if (replace.getKey().equals(key))// find the key and replace the value
      {
        replacedEntries.add(key+"="+replace.getValue());// if replaced then add a key, then upper object can get this info.
        checkword.remove(i);
        i++;
        return key + equalsMark + replace.getValue();
      }
    }

    return line;
  }

  private boolean isComment(String line)
  {
    return line.startsWith("#") || line.startsWith("!");
  }

  @Override
  public List<String> getReplacedEntry() {
    return replacedEntries;
  }
}
