package com.juvenxu.portableconfig;


import org.apache.commons.io.IOUtils;
import org.apache.maven.monitor.logging.DefaultLog;
import org.codehaus.plexus.PlexusTestCase;
import org.codehaus.plexus.logging.console.ConsoleLogger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.activation.DataSource;
import javax.activation.FileDataSource;
import java.io.*;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

/**
 * @author juven
 */

public class DefaultPortableConfigEngineReplaceJarTest extends PlexusTestCase
{

  private PortableConfigEngine sut;


  @Override
  public void setUp() throws Exception
  {
    sut = lookup(PortableConfigEngine.class);
  }


  public void testReplaceJarWebXml() throws Exception
  {
    DataSource potableConfigDataSource = new FileDataSource(new File(this.getClass().getResource("/portable_config/" + "replace_jar_web_xml.xml").toURI()));
    File jar = new File(this.getClass().getResource("/to_be_replaced/" + "portableconfig-demo-1.0-SNAPSHOT.war").toURI());

    sut.replace(potableConfigDataSource, jar);

    String entryName = "WEB-INF/classes/db.properties";

    Properties properties = loadJarEntryProperties(jar, entryName);

    Assert.assertEquals("yup replaced", properties.getProperty("mysql.password"));
  }

  private Properties loadJarEntryProperties(File jar, String entryName) throws IOException
  {
    Properties properties = new Properties();

    JarInputStream jarInputStream = new JarInputStream(new FileInputStream(jar));

    while (true)
    {
      JarEntry jarEntry = jarInputStream.getNextJarEntry();

      if (jarEntry == null)
      {
        break;
      }

      if ( jarEntry.getName().equals(entryName))
      {

        byte[] buffer = new byte[1024];

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

        properties.load(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));


      }
    }

    IOUtils.closeQuietly(jarInputStream);

    return properties;
  }
}
