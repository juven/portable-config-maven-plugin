package com.juvenxu.portableconfig;

import org.codehaus.plexus.component.annotations.Component;
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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * @author juven
 */
@Component(role = ContentFilter.class, hint = "xml")
public class XmlContentFilter implements ContentFilter
{
  @Override
  public boolean accept(String contentName)
  {
    return contentName.endsWith(".xml");
  }

  @Override
  public void filter(InputStream inputStream, OutputStream outputStream, List<Replace> replaces) throws IOException
  {
    SAXBuilder saxBuilder = new SAXBuilder();

    Document doc = null;

    try
    {
      doc = saxBuilder.build(inputStream);
    }
    catch (JDOMException e)
    {
      throw new IOException("Failed to build Xml document.", e);
    }


    for (Replace replace : replaces)
    {
      XPathFactory xPathFactory = XPathFactory.instance();

      XPathExpression xPathExpression = null;

      String rootNamespaceURI = doc.getRootElement().getNamespaceURI();

      if (StringUtils.isEmpty(rootNamespaceURI))
      {
        xPathExpression = xPathFactory.compile(replace.getXpath());
      }
      else
      {
        String customizedNamespacePrefix = "portableconfig";
        Namespace rootNamespace = Namespace.getNamespace(customizedNamespacePrefix, doc.getRootElement().getNamespaceURI());
        String expression = replace.getXpath().replace("/", "/" + customizedNamespacePrefix + ":");

        xPathExpression = xPathFactory.compile(expression, Filters.fpassthrough(), null, rootNamespace);
      }

      List<Element> elements = xPathExpression.evaluate(doc);

      if (!elements.isEmpty())
      {
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
