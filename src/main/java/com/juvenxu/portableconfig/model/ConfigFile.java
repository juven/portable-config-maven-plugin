package com.juvenxu.portableconfig.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author juven
 */
public class ConfigFile
{
  private final String path;

  private List<Replace> replaces = new ArrayList<Replace>();

  public ConfigFile(String path)
  {
    this.path = path;
  }

  public String getPath()
  {
    return path;
  }

  public List<Replace> getReplaces()
  {
    return replaces;
  }
  public void addReplace(Replace replace)
  {
    this.getReplaces().add(replace);
  }


  @Override
  public boolean equals(Object o)
  {
    if (this == o) return true;
    if (!(o instanceof ConfigFile)) return false;

    ConfigFile that = (ConfigFile) o;

    if (!path.equals(that.path)) return false;
    if (!replaces.equals(that.replaces)) return false;

    return true;
  }

  @Override
  public int hashCode()
  {
    int result = path.hashCode();
    result = 31 * result + replaces.hashCode();
    return result;
  }
}
