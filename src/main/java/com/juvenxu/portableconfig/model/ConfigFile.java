package com.juvenxu.portableconfig.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author juven
 */
public class ConfigFile
{
  private final String path;

  private final String type;

  private List<Replace> replaces = new ArrayList<Replace>();

  public ConfigFile(String path)
  {
    this(path, path.substring(path.lastIndexOf('.')));
  }

  public ConfigFile(String path, String type)
  {
    this.path = path;

    this.type = type;
  }

  public String getPath()
  {
    return path;
  }

  public String getType()
  {
    return type;
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
    if (o == null || getClass() != o.getClass()) return false;

    ConfigFile that = (ConfigFile) o;

    if (!path.equals(that.path)) return false;
    if (!replaces.equals(that.replaces)) return false;
    if (!type.equals(that.type)) return false;

    return true;
  }

  @Override
  public int hashCode()
  {
    int result = path.hashCode();
    result = 31 * result + type.hashCode();
    result = 31 * result + replaces.hashCode();
    return result;
  }
}
