package com.juvenxu.portableconfig;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.maven.plugin.logging.Log;

import javax.activation.DataSource;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;

/**
 * @author juven
 */
public class DefaultPortableConfigEngine implements PortableConfigEngine
{
  private PortableConfigBuilder portableConfigBuilder;

  private List<ContentFilter> contentFilters = new ArrayList<ContentFilter>();

  //TODO I don't want to bring in any maven stuff here, so should clean it in the future
  private Log log;

  public DefaultPortableConfigEngine( Log log)
  {
    this.log = log;
    this.portableConfigBuilder = new DefaultPortableConfigBuilder();
    contentFilters.add(new PropertiesContentFilter());
    contentFilters.add(new XmlContentFilter());
  }

  @Override
  public void replaceDirectory(DataSource portableConfigDataSource, File directory) throws IOException
  {

    PortableConfig portableConfig = portableConfigBuilder.build(portableConfigDataSource.getInputStream());

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

  @Override
  public void replaceJar(DataSource portableConfigDataSource, File jar) throws IOException
  {

    PortableConfig portableConfig = portableConfigBuilder.build(portableConfigDataSource.getInputStream());

    JarInputStream jarInputStream = new JarInputStream(new FileInputStream(jar));
    File tmpJar = File.createTempFile(Long.toString(System.nanoTime()), ".jar");
    log.info("Tmp file: " + tmpJar.getAbsolutePath());
    JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(tmpJar));

    byte[] buffer = new byte[1024];
    while (true)
    {
      JarEntry jarEntry = jarInputStream.getNextJarEntry();

      if (jarEntry == null)
      {
        break;
      }

      log.debug(jarEntry.getName());

      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

      while (true)
      {
        int length = jarInputStream.read(buffer, 0, buffer.length);

        if (length <= 0)
        {
          break;
        }

        byteArrayOutputStream.write(buffer, 0, length);

      }

      boolean filtered = false;

      for (ConfigFile configFile : portableConfig.getConfigFiles())
      {
        if (!configFile.getPath().equals(jarEntry.getName()))
        {
          continue;
        }

        if (!hasContentFilter(jarEntry.getName()))
        {
          continue;
        }

        log.info(String.format("Replacing: %s!%s", jar.getName(), jarEntry.getName()));

        JarEntry filteredJarEntry = new JarEntry(jarEntry.getName());
        jarOutputStream.putNextEntry(filteredJarEntry);

        ContentFilter contentFilter = getContentFilter(jarEntry.getName());

        contentFilter.filter(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()), jarOutputStream, configFile.getReplaces());

        filtered = true;
      }

      if (!filtered)
      {
        jarOutputStream.putNextEntry(jarEntry);
        byteArrayOutputStream.writeTo(jarOutputStream);
      }
    }

    IOUtils.closeQuietly(jarInputStream);
    IOUtils.closeQuietly(jarOutputStream);

    log.info("Replacing: " + jar.getAbsolutePath() + " with: " + tmpJar.getAbsolutePath());
    FileUtils.copyFile(tmpJar, jar);
  }


  private boolean hasContentFilter(final String contentName)
  {
    return getContentFilter(contentName) != null;
  }

  private ContentFilter getContentFilter(final String contentName)
  {
    for ( ContentFilter contentFilter : contentFilters)
    {
      if ( contentFilter.accept(contentName))
      {
        return contentFilter;
      }
    }

    return null;
  }
}
