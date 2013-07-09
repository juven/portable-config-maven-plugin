package com.juvenxu.portableconfig;

import javax.activation.DataSource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author juven
 */
public interface PortableConfigEngine
{
  /**
   * For each file in the directory, do replace
   *
   * @param portableConfig
   * @param directory
   */
  public void replaceDirectory(DataSource portableConfig, File directory) throws IOException;

  /**
   * This will generate a new temporary jar with filtered content, then replace the old one
   *
   * @param portableConfig
   * @param jar
   */
  public void replaceJar(DataSource portableConfig, File jar) throws IOException;

  public void replace(DataSource portableConfig, File directory, File source) throws IOException;
}
