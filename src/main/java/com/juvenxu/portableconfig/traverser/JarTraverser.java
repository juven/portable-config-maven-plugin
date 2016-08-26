package com.juvenxu.portableconfig.traverser;

import com.juvenxu.portableconfig.AbstractTraverser;
import com.juvenxu.portableconfig.ContentFilter;
import com.juvenxu.portableconfig.model.ConfigFile;
import com.juvenxu.portableconfig.model.PortableConfig;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.codehaus.plexus.component.annotations.Component;

import java.io.*;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;

/**
 * @author juven
 */
@Component(role = AbstractTraverser.class, hint = "jar")
public class JarTraverser extends AbstractTraverser
{

  @Override
  public void traverse(PortableConfig portableConfig, File jar) throws IOException
  {

    JarInputStream jarInputStream = new JarInputStream(new FileInputStream(jar));
    Manifest manifest = jarInputStream.getManifest();
    File tmpJar = File.createTempFile(Long.toString(System.nanoTime()), ".jar");
    getLogger().info("Tmp file: " + tmpJar.getAbsolutePath());
    JarOutputStream jarOutputStream = null;
    if(manifest!=null)
    {
        jarOutputStream = new JarOutputStream(new FileOutputStream(tmpJar), manifest);
    }
    else
    {
        jarOutputStream = new JarOutputStream(new FileOutputStream(tmpJar));
    }

    byte[] buffer = new byte[1024];
    while (true)
    {
      JarEntry jarEntry = jarInputStream.getNextJarEntry();

      if (jarEntry == null)
      {
        break;
      }

      getLogger().debug(jarEntry.getName());

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

        if (!hasContentFilter(configFile.getType()))
        {
          continue;
        }

        getLogger().info(String.format("Replacing: %s!%s", jar.getName(), jarEntry.getName()));

        JarEntry filteredJarEntry = new JarEntry(jarEntry.getName());
        jarOutputStream.putNextEntry(filteredJarEntry);

        ContentFilter contentFilter = getContentFilter(configFile.getType());

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

    getLogger().info("Replacing: " + jar.getAbsolutePath() + " with: " + tmpJar.getAbsolutePath());
    FileUtils.copyFile(tmpJar, jar);
    FileUtils.forceDeleteOnExit(tmpJar);
  }
}
