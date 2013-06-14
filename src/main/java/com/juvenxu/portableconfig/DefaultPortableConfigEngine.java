package com.juvenxu.portableconfig;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.maven.plugin.logging.Log;
import org.codehaus.plexus.util.StringUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

import javax.activation.DataSource;
import java.io.*;
import java.util.List;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;

/**
 * @author juven
 */
public class DefaultPortableConfigEngine implements PortableConfigEngine
{
  private PortableConfigBuilder portableConfigBuilder;

  //TODO I don't want to bring in any maven stuff here, so should clean it in the future
  private Log log;

  public DefaultPortableConfigEngine( Log log)
  {
    this.log = log;

    this.portableConfigBuilder = new DefaultPortableConfigBuilder();
  }

  @Override
  public void replaceDirectory(DataSource portableConfigDataSource, File directory)
  {
    try
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

        File tmpTxt = File.createTempFile(Long.toString(System.nanoTime()), ".txt");

        if (configFile.getPath().endsWith(".properties"))
        {
          log.info(String.format("Replacing file: %s", file.getPath()));

          InputStream inputStream = null;
          OutputStream outputStream = null;

          try
          {
            inputStream = new FileInputStream(file);

            outputStream = new FileOutputStream(tmpTxt);

            filterProperties(inputStream, outputStream, configFile.getReplaces());
          }
          finally
          {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(outputStream);
          }

          FileUtils.copyFile(tmpTxt, file);

        }
        else if (configFile.getPath().endsWith(".xml"))
        {
          log.info(String.format("Replacing file: %s", file.getPath()));

          InputStream inputStream = null;
          OutputStream outputStream = null;

          try
          {
            inputStream = new FileInputStream(file);

            outputStream = new FileOutputStream(tmpTxt);

            filterXml(inputStream, outputStream, configFile.getReplaces());
          } finally
          {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(outputStream);
          }

          FileUtils.copyFile(tmpTxt, file);
        }
        else
        {
          log.warn(String.format("Ignoring file: %s, only .properties and .xml files are supported.", file.getPath()));
        }

      }

    }
    catch (IOException e)
    {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    }
    catch (JDOMException e)
    {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    }
  }

  @Override
  public void replaceJar(DataSource portableConfigDataSource, File jar)
  {
    try
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

        for ( ConfigFile configFile : portableConfig.getConfigFiles())
        {
          if (configFile.getPath().equals(jarEntry.getName()))
          {
            log.info(String.format("Replacing: %s!%s", jar.getName(), jarEntry.getName()));

            JarEntry filteredJarEntry = new JarEntry(jarEntry.getName());
            jarOutputStream.putNextEntry(filteredJarEntry);
            if ( configFile.getPath().endsWith(".xml"))
            {
              filterXml(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()), jarOutputStream, configFile.getReplaces());
            }
            else if (configFile.getPath().endsWith(".properties"))
            {
              filterProperties(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()), jarOutputStream, configFile.getReplaces());
            }

            filtered = true;
          }
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

    } catch (IOException e)
    {
      e.printStackTrace();
    } catch (JDOMException e)
    {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    }

  }

  private void filterProperties(InputStream inputStream, OutputStream outputStream, List<Replace> replaces) throws IOException
  {
    Properties properties = new Properties();

    properties.load(inputStream);

    for (Replace replace : replaces)
    {
      if (properties.containsKey(replace.getKey()))
      {
        properties.setProperty(replace.getKey(), replace.getValue());
      }
    }

    properties.store(outputStream, null);
  }

  private void filterXml(InputStream inputStream, OutputStream outputStream, List<Replace> replaces) throws JDOMException, IOException
  {
    SAXBuilder saxBuilder = new SAXBuilder();

    Document doc = saxBuilder.build(inputStream);



    for (Replace replace : replaces)
    {
      XPathFactory xPathFactory = XPathFactory.instance();

      XPathExpression xPathExpression = null;

      String rootNamespaceURI = doc.getRootElement().getNamespaceURI();

      if (StringUtils.isEmpty(rootNamespaceURI))
      {
        xPathExpression = xPathFactory.compile(replace.getKey());
      }
      else
      {
        String customizedNamespacePrefix = "portableconfig";
        Namespace rootNamespace = Namespace.getNamespace(customizedNamespacePrefix, doc.getRootElement().getNamespaceURI());
        String expression = replace.getKey().replace("/","/" + customizedNamespacePrefix+":");

        xPathExpression = xPathFactory.compile(expression, Filters.fpassthrough(), null, rootNamespace);
      }

      List<Element> elements = xPathExpression.evaluate(doc);

      if (!elements.isEmpty())
      {
        log.debug("Replacing XML entry: " + replace.getKey());
        for (Element element : elements)
        {
          element.setText(replace.getValue());
        }
      }
    }

    XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());

    xmlOutputter.output(doc, outputStream);
  }


}
