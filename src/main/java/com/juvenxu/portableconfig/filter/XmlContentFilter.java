package com.juvenxu.portableconfig.filter;

import com.juvenxu.portableconfig.ContentFilter;
import com.juvenxu.portableconfig.model.Replace;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.util.StringUtils;
import org.jdom2.*;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

import java.io.*;
import java.util.List;

/**
 * @author juven
 */
@Component(role = ContentFilter.class, hint = "xml")
public class XmlContentFilter implements ContentFilter
{

  private static final String CUSTOMIZED_NAMESPACE_PREFIX = "portableconfig";

  @Override
  public boolean accept(String contentName)
  {
    return contentName.endsWith(".xml");
  }

  @Override
  public void filter(Reader reader, Writer writer, List<Replace> replaces) throws IOException
  {
    SAXBuilder saxBuilder = new SAXBuilder();

    Document doc = null;

    try
    {
      doc = saxBuilder.build(reader);
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
        Namespace rootNamespace = Namespace.getNamespace(CUSTOMIZED_NAMESPACE_PREFIX, doc.getRootElement().getNamespaceURI());

        String expression = addCustomizedNamespacePrefix(CUSTOMIZED_NAMESPACE_PREFIX, replace.getXpath());

        xPathExpression = xPathFactory.compile(expression, Filters.fpassthrough(), null, rootNamespace);
      }

      for (Object obj : xPathExpression.evaluate(doc))
      {
        if (obj instanceof Element)
        {
          ((Element) obj).setText(replace.getValue());
        }
        else if (obj instanceof Attribute)
        {
          ((Attribute) obj).setValue(replace.getValue());
        }
        else
        {
          throw new IOException("Unsupported xpath result object: " + obj.getClass().toString());
        }
      }
    }

    XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());

    xmlOutputter.output(doc, writer);
  }

  private String addCustomizedNamespacePrefix(String customizedNamespacePrefix, String expression)
  {
    return expression.replaceAll("(/+)", "$1" + customizedNamespacePrefix + ":");
  }
}
