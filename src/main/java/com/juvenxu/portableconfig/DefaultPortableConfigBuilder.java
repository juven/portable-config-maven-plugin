package com.juvenxu.portableconfig;

import org.codehaus.plexus.component.annotations.Component;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author juven
 */
@Component(role=PortableConfigBuilder.class)
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
        ConfigFile configFile = new ConfigFile(configFileElement.getAttribute("path").getValue());

        for (Element replaceElement : configFileElement.getChildren("replace"))
        {
          if (replaceElement.getAttribute("key") != null)
          {
            configFile.addReplace(new Replace(replaceElement.getAttribute("key").getValue(), null, replaceElement.getText()));
          }
          else if (replaceElement.getAttribute("xpath") != null)
          {
            configFile.addReplace(new Replace(null, replaceElement.getAttribute("xpath").getValue(), replaceElement.getText()));
          }
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
