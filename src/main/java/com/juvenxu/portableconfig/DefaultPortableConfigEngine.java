package com.juvenxu.portableconfig;

import org.apache.maven.plugin.logging.Log;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.Properties;

/**
 * @author juven
 */
public class DefaultPortableConfigEngine implements PortableConfigEngine
{

  private final URL targetDirectory;

  private PortableConfigBuilder portableConfigBuilder;

  //TODO I don't want to bring in any maven stuff here, so should clean it in the future
  private Log log;

  public DefaultPortableConfigEngine(URL targetDirectory, Log log)
  {
    this.targetDirectory = targetDirectory;

    this.log = log;

    this.portableConfigBuilder = new DefaultPortableConfigBuilder();
  }

  @Override
  public void apply(InputStream portableConfigFile) throws PortableConfigException
  {
    PortableConfig portableConfig = portableConfigBuilder.build(portableConfigFile);

    for (ConfigFile configFile : portableConfig.getConfigFiles())
    {
      File file = new File(targetDirectory.getFile(), configFile.getPath());

      if (!file.exists() || file.isDirectory())
      {
        log.warn(String.format("File %s does not exist or is a directory.", file.getPath()));

        break;
      }

      if (configFile.getPath().endsWith(".properties"))
      {
        log.info(String.format("Replacing file: %s", file.getPath()));
        replaceProperties(file, configFile.getReplaces());
      }
      else if (configFile.getPath().endsWith(".xml"))
      {
        log.info(String.format("Replacing file: %s", file.getPath()));
        replaceXml(file, configFile.getReplaces());
      }
      else
      {
        log.warn(String.format("Ignoring file: %s, only .properties and .xml files are supported.", file.getPath()));
      }
    }

  }

  private void replaceXml(File file, List<Replace> replaces) throws PortableConfigException
  {
    if (!file.exists() || file.isDirectory())
    {
      throw new PortableConfigException(String.format("File %s does not exist or is a directory.", file));
    }

    SAXBuilder saxBuilder = new SAXBuilder();

    try
    {
      Document doc = saxBuilder.build(file);

      for (Replace replace : replaces)
      {
        XPathFactory xPathFactory = XPathFactory.instance();
        XPathExpression xPathExpression = xPathFactory.compile(replace.getKey());
        List<Element> elements = xPathExpression.evaluate(doc);

        if (!elements.isEmpty())
        {
          for (Element element : elements)
          {
            element.setText(replace.getValue());
          }
        }
      }

      FileOutputStream fileOutputStream = new FileOutputStream(file);

      XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());

      xmlOutputter.output(doc, fileOutputStream);

    } catch (JDOMException e)
    {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    } catch (IOException e)
    {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    }


  }

  private void replaceProperties(File file, List<Replace> replaces) throws PortableConfigException
  {

    if (!file.exists() || file.isDirectory())
    {
      throw new PortableConfigException(String.format("File %s does not exist or is a directory.", file));
    }

    Properties properties = new Properties();

    InputStream is = null;

    try
    {
      is = new FileInputStream(file);
      properties.load(is);

    } catch (FileNotFoundException e)
    {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    } catch (IOException e)
    {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    } finally
    {
      if (is != null)
      {
        try
        {
          is.close();
        } catch (IOException e)
        {
          e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
      }
    }

    for (Replace replace : replaces)
    {
      if (properties.containsKey(replace.getKey()))
      {
        properties.setProperty(replace.getKey(), replace.getValue());
      }
    }

    OutputStream os = null;

    try
    {
      os = new FileOutputStream(file);
      properties.store(os, null);
    } catch (FileNotFoundException e)
    {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    } catch (IOException e)
    {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    } finally
    {
      if (os != null)
      {
        try
        {
          os.close();
        } catch (IOException e)
        {
          e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
      }
    }
  }

}
