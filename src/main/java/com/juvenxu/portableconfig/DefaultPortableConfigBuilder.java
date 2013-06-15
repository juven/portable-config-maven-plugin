package com.juvenxu.portableconfig;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author juven
 */
public class DefaultPortableConfigBuilder implements PortableConfigBuilder
{
  public PortableConfig build(InputStream portableConfigFile) throws IOException
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


    }
    catch (JDOMException e)
    {
      throw new IOException("Failed to build XML document", e);
    }

    return result;
  }
}
