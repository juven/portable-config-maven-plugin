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
   * Replace file with replaces defined in portableConfig, if file is a directory, traverse the directory;
   * if the file is a jar/war, traverse it; other format is not supported
   * @param portableConfig
   * @param file
   * @throws IOException
   */
  public void replace(DataSource portableConfig, File file) throws IOException;

  public void replace(DataSource portableConfig, File file, File source) throws IOException;
}
