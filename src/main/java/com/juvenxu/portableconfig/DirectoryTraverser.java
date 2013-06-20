package com.juvenxu.portableconfig;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.codehaus.plexus.logging.Logger;

import java.io.*;
import java.util.List;

/**
 * @author juven
 */
public class DirectoryTraverser extends AbstractTraverser
{
  private final File directory;

  public DirectoryTraverser(Logger log, List<ContentFilter> contentFilters, File directory)
  {
    super(log, contentFilters);
    this.directory = directory;
  }

  @Override
  public void traverse(PortableConfig portableConfig) throws IOException
  {
    for (ConfigFile configFile : portableConfig.getConfigFiles())
    {
      File file = new File(directory, configFile.getPath());

      if (!file.exists() || file.isDirectory())
      {
        log.warn(String.format("File: %s does not exist or is a directory.", file.getPath()));

        continue;
      }

      if (!hasContentFilter(configFile.getPath()))
      {
        log.warn(String.format("Ignore replacing: %s", file.getPath()));

        continue;
      }

      log.info(String.format("Replacing file: %s", file.getPath()));

      File tmpTxt = File.createTempFile(Long.toString(System.nanoTime()), ".txt");

      ContentFilter contentFilter = getContentFilter(configFile.getPath());

      InputStream inputStream = null;

      OutputStream outputStream = null;

      try
      {
        inputStream = new FileInputStream(file);

        outputStream = new FileOutputStream(tmpTxt);

        contentFilter.filter(inputStream, outputStream, configFile.getReplaces());
      }
      finally
      {
        IOUtils.closeQuietly(inputStream);
        IOUtils.closeQuietly(outputStream);
      }

      FileUtils.copyFile(tmpTxt, file);
    }
  }
}
