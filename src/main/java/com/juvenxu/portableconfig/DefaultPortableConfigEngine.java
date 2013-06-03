package com.juvenxu.portableconfig;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

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

  public DefaultPortableConfigEngine(URL targetDirectory)
  {
    this.targetDirectory = targetDirectory;
  }

  @Override
  public void apply(InputStream portableConfigFile) throws PortableConfigException
  {
    PortableConfig portableConfig = buildPortableConfigFromXml(portableConfigFile);

    for (ConfigFile configFile : portableConfig.getConfigFiles())
    {
      File file = new File(targetDirectory.getFile(), configFile.getPath());

      replaceProperties(file, configFile.getReplaces());
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

  private PortableConfig buildPortableConfigFromXml(InputStream portableConfigFile)
  {
    PortableConfig result = new PortableConfig();

    SAXBuilder saxBuilder = new SAXBuilder();

    try
    {
      Document xmlDoc = saxBuilder.build(portableConfigFile);

      Element rootElement = xmlDoc.getRootElement();

      for (Element configFileElement : rootElement.getChildren())
      {
        ConfigFile configFile = new ConfigFile();

        configFile.setPath(configFileElement.getChild("path").getValue());

        for (Element replaceElement : configFileElement.getChild("replaces").getChildren())
        {
          Replace replace = new Replace();
          replace.setKey(replaceElement.getChild("key").getValue());
          replace.setValue(replaceElement.getChild("value").getValue());

          configFile.getReplaces().add(replace);
        }

        result.getConfigFiles().add(configFile);
      }


    } catch (JDOMException e)
    {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    } catch (IOException e)
    {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    }

    return result;
  }
}
