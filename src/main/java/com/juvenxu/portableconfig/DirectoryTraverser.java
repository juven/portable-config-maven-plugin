package com.juvenxu.portableconfig;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.codehaus.plexus.component.annotations.Component;

import java.io.*;

/**
 * @author juven
 */
@Component(role = AbstractTraverser.class, hint = "directory")
public class DirectoryTraverser extends AbstractTraverser
{

  @Override
  public void traverse(PortableConfig portableConfig, File directory) throws IOException
  {
    for (ConfigFile configFile : portableConfig.getConfigFiles())
    {
      File file = new File(directory, configFile.getPath());

      if (!file.exists() || file.isDirectory())
      {
        getLogger().warn(String.format("File: %s does not exist or is a directory.", file.getPath()));

        continue;
      }

      if (!hasContentFilter(configFile.getPath()))
      {
        getLogger().warn(String.format("Ignore replacing: %s", file.getPath()));

        continue;
      }

      getLogger().info(String.format("Replacing file: %s", file.getPath()));

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
