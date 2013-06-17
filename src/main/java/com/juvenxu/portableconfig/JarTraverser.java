package com.juvenxu.portableconfig;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.maven.plugin.logging.Log;

import java.io.*;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;

/**
 * @author juven
 */
public class JarTraverser extends AbstractTraverser
{
  private final File jar;

  public JarTraverser(Log log, File jar)
  {
    super(log);
    this.jar = jar;
  }

  @Override
  public void traverse(PortableConfig portableConfig) throws IOException
  {

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
}
