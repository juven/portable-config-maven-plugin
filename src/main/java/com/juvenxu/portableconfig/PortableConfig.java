package com.juvenxu.portableconfig;

import java.util.ArrayList;
import java.util.List;

/**
 * @author juven
 */
public class PortableConfig
{
  public List<ConfigFile> getConfigFiles()
  {
    return configFiles;
  }

  public void setConfigFiles(List<ConfigFile> configFiles)
  {
    this.configFiles = configFiles;
  }

  private List<ConfigFile> configFiles = new ArrayList<ConfigFile>();

  @Override
  public boolean equals(Object o)
  {
    if (this == o) return true;
    if (!(o instanceof PortableConfig)) return false;

    PortableConfig that = (PortableConfig) o;

    if (!configFiles.equals(that.configFiles)) return false;

    return true;
  }

  @Override
  public int hashCode()
  {
    return configFiles.hashCode();
  }
}

class ConfigFile
{
  private final String path;

  private List<Replace> replaces = new ArrayList<Replace>();

  ConfigFile(String path)
  {
    this.path = path;
  }

  String getPath()
  {
    return path;
  }

  List<Replace> getReplaces()
  {
    return replaces;
  }
  void addReplace(Replace replace)
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

class Replace
{
  private final String key;

  private final String xpath;

  private final String value;

  Replace(String key, String xpath, String value)
  {
    this.key = key;
    this.xpath = xpath;
    this.value = value;
  }

  String getKey()
  {
    return key;
  }

  String getValue()
  {
    return value;
  }

  String getXpath()
  {
    return xpath;
  }

  @Override
  public boolean equals(Object o)
  {
    if (this == o) return true;
    if (!(o instanceof Replace)) return false;

    Replace replace = (Replace) o;

    if (key != null ? !key.equals(replace.key) : replace.key != null) return false;
    if (value != null ? !value.equals(replace.value) : replace.value != null) return false;
    if (xpath != null ? !xpath.equals(replace.xpath) : replace.xpath != null) return false;

    return true;
  }

  @Override
  public int hashCode()
  {
    int result = key != null ? key.hashCode() : 0;
    result = 31 * result + (xpath != null ? xpath.hashCode() : 0);
    result = 31 * result + (value != null ? value.hashCode() : 0);
    return result;
  }
}
