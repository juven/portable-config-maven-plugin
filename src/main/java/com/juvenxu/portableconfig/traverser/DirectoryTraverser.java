package com.juvenxu.portableconfig.traverser;

import com.juvenxu.portableconfig.AbstractTraverser;
import com.juvenxu.portableconfig.ContentFilter;
import com.juvenxu.portableconfig.model.ConfigFile;
import com.juvenxu.portableconfig.model.PortableConfig;
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

      if (!hasContentFilter(configFile))
      {
        getLogger().warn(String.format("Ignore replacing: %s", file.getPath()));

        continue;
      }

      getLogger().info(String.format("Replacing file: %s", file.getPath()));

      File tmpTxt = File.createTempFile(Long.toString(System.nanoTime()), ".txt");

      ContentFilter contentFilter = getContentFilter(configFile);
      
      Reader reader = null;

      Writer writer = null;

      try
      {
        reader = new FileReader(file);
        writer = new FileWriter(tmpTxt);

        contentFilter.filter(reader, writer, configFile.getReplaces());
      }
      finally
      {
        IOUtils.closeQuietly(reader);
        IOUtils.closeQuietly(writer);
      }

      FileUtils.copyFile(tmpTxt, file);
    }
  }
}
