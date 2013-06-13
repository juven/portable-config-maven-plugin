package com.juvenxu.portableconfig;

import javax.activation.DataSource;
import java.io.File;
import java.io.InputStream;

/**
 * @author juven
 */
public interface PortableConfigEngine
{
  /**
   * For each file in the directory, do replace
   * @param portableConfig
   * @param directory
   */
  public void replaceDirectory(DataSource portableConfig, File directory);

  /**
   * This will generate a new temporary jar with filtered content, then replace the old one
   * @param portableConfig
   * @param jar
   */
  public void replaceJar(DataSource portableConfig, File jar);
}
