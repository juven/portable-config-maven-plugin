package com.juvenxu.portableconfig.model;

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

