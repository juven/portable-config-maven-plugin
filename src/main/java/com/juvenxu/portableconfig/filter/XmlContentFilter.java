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
public class XmlContentFilter implements ContentFilter {

    @Override
    public boolean accept(String contentType) {
        return (".xml").equals(contentType);
    }

    @Override
    public void filter(InputStream inputStream, OutputStream outputStream, List<Replace> replaces) throws IOException {
        try {
            VTDGen vtdGen = new VTDGen();
            vtdGen.setDoc(IOUtils.toByteArray(inputStream));
            vtdGen.parse(true);
            VTDNav vtdNav = vtdGen.getNav();
            AutoPilot autoPilot = new AutoPilot(vtdNav);

            addAllNamespaces(vtdNav, autoPilot);
            XMLModifier xmlModifier = new XMLModifier(vtdNav);

            for (Replace replace : replaces) {
                autoPilot.selectXPath(replace.getXpath());
                int i;
                while ((i = autoPilot.evalXPath()) != -1) {
                    if (toReplaceAttribute(replace)) {
                        xmlModifier.updateToken(i + 1, replace.getValue());
                    } else {
                        int textIndex = vtdNav.getText();
                        xmlModifier.updateToken(textIndex, replace.getValue());
                    }
                }
                autoPilot.resetXPath();
            }
            xmlModifier.output(outputStream);
        } catch (XPathEvalException e) {
            throw new IOException("Failed to XPath evaluation", e);
        } catch (EOFException e) {
            throw new IOException("Failed to EOF", e);
        } catch (TranscodeException e) {
            throw new IOException("Failed to transcode characters ", e);
        } catch (NavException e) {
            throw new IOException("Failed in navigation phase", e);
        } catch (EncodingException e) {
            throw new IOException("Failed to parse spacial character encoding", e);
        } catch (XPathParseException e) {
            throw new IOException("Failed to the construction of XPathExpr ", e);
        } catch (EntityException e) {
            throw new IOException("Failed for any invalid entity reference during parsing", e);
        } catch (ParseException e) {
            throw new IOException("Failed to parsing XML", e);
        } catch (ModifyException e) {
            throw new IOException("Failed to modification of XML", e);
        }

    }

    private boolean toReplaceAttribute(Replace replace) {
        return replace.getXpath().matches(".*/@[^/]+$");
    }

    private void addAllNamespaces(VTDNav vtdNav, AutoPilot autoPilot) throws NavException {
        String token = null;
        String nsPrefix = null;
        String nsUrl = null;
        int tokenCount = vtdNav.getTokenCount();
        int i = vtdNav.getCurrentIndex() + 1;
        while (i < tokenCount) {
            int type = vtdNav.getTokenType(i);
            // quickly skip non-xmlns attrs
            while (type == VTDNav.TOKEN_ATTR_NAME) {
                i += 2;
                type = vtdNav.getTokenType(i);
            }
            if (type == VTDNav.TOKEN_ATTR_NS) {
                token = vtdNav.toNormalizedString(i);
                nsPrefix = token.substring(token.indexOf(":") + 1);
                nsUrl = vtdNav.toNormalizedString(i + 1);
                autoPilot.declareXPathNameSpace(nsPrefix, nsUrl);
            }
            i++;
        }
    }
}
