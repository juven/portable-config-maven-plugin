package com.juvenxu.portableconfig;

import org.codehaus.plexus.PlexusTestCase;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 * @author juven
 */
public class DefaultPortableConfigEngineTestBase extends PlexusTestCase
{
  protected String getResultXmlEntry(String xmlFile, String xpath) throws JDOMException, IOException
  {
    SAXBuilder saxBuilder = new SAXBuilder();

    Document doc = saxBuilder.build(this.getClass().getResourceAsStream("/to_be_replaced/" + xmlFile));

    XPathFactory xPathFactory = XPathFactory.instance();
    XPathExpression xPathExpression = xPathFactory.compile(xpath);
    List<Element> elements =  xPathExpression.evaluate(doc);

    return elements.get(0).getValue();
  }

  protected Properties getResultProperties(String propertiesFile) throws IOException
  {
    Properties result = new Properties();
    result.load(this.getClass().getResourceAsStream("/to_be_replaced/" + propertiesFile));
    return result;
  }
}
