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
}

class ConfigFile
{
  private String path;

  String getPath()
  {
    return path;
  }

  void setPath(String path)
  {
    this.path = path;
  }

  List<Replace> getReplaces()
  {
    return replaces;
  }

  void setReplaces(List<Replace> replaces)
  {
    this.replaces = replaces;
  }

  private List<Replace> replaces = new ArrayList<Replace>();
}

class Replace
{
  private String key;

  private String value;

  String getKey()
  {
    return key;
  }

  void setKey(String key)
  {
    this.key = key;
  }

  String getValue()
  {
    return value;
  }

  void setValue(String value)
  {
    this.value = value;
  }
}
