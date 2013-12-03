package com.juvenxu.portableconfig.filter;

import com.juvenxu.portableconfig.ContentFilter;
import com.juvenxu.portableconfig.model.Replace;
import com.ximpleware.*;
import org.apache.commons.io.IOUtils;
import org.codehaus.plexus.component.annotations.Component;

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

  private static final String CUSTOMIZED_NAMESPACE_PREFIX = "portableconfig";

  @Override
  public boolean accept(String contentType)
  {
    return (".xml").equals(contentType);
  }

  @Override
  public void filter(InputStream fileIS, OutputStream tmpOS, List<Replace> replaces) throws IOException
  {
    try
    {
      VTDGen vg = new VTDGen();
      vg.setDoc(IOUtils.toByteArray(fileIS));
      vg.parse(true);
      VTDNav vn = vg.getNav();
      AutoPilot ap = new AutoPilot(vn);

      addAllNamespaces(vn, ap);
      XMLModifier xm = new XMLModifier(vn);
      int i;
      for (Replace replace : replaces)
      {
        ap.selectXPath(replace.getXpath());
        while ((i = ap.evalXPath()) != -1)
        {
          if (replace.getXpath().contains("text()"))
          {
            xm.updateToken(i, replace.getValue());
          }
          else
          {
            xm.updateToken(i + 1, replace.getValue());
          }
        }

      }
      xm.output(tmpOS);
    }
    catch (XPathEvalException e)
    {
      throw new IOException("Failed to XPath evaluation", e);
    }
    catch (EOFException e)
    {
      throw new IOException("Failed to EOF", e);
    }
    catch (TranscodeException e)
    {
      throw new IOException("Failed to transcode characters ", e);
    }
    catch (NavException e)
    {
      throw new IOException("Failed in navigation phase", e);
    }
    catch (EncodingException e)
    {
      throw new IOException("Failed to parse spacial character encoding", e);
    }
    catch (XPathParseException e)
    {
      throw new IOException("Failed to the construction of XPathExpr ", e);
    }
    catch (EntityException e)
    {
      throw new IOException("Failed for any invalid entity reference during parsing", e);
    }
    catch (ParseException e)
    {
      throw new IOException("Failed to parsing XML", e);
    }
    catch (ModifyException e)
    {
      throw new IOException("Failed to modification of XML", e);
    }

  }

  private void addAllNamespaces(VTDNav vn, AutoPilot ap) throws NavException
  {
    String token = null;
    String nsPrefix = null;
    String nsUrl = null;
    int tokenCount = vn.getTokenCount();
    int i = vn.getCurrentIndex() + 1;
    while (i < tokenCount)
    {
      int type = vn.getTokenType(i);
      // quickly skip non-xmlns attrs
      while (type == VTDNav.TOKEN_ATTR_NAME)
      {
        i += 2;
        type = vn.getTokenType(i);
      }
      if (type == VTDNav.TOKEN_ATTR_NS)
      {
        token = vn.toNormalizedString(i);
        nsPrefix = token.substring(token.indexOf(":") + 1);
        nsUrl = vn.toNormalizedString(i + 1);
        ap.declareXPathNameSpace(nsPrefix, nsUrl);
      }
      else if (type == VTDNav.TOKEN_ATTR_VAL)
      {
      }
      else
      {
        break;
      }
      i++;
    }
  }
}
