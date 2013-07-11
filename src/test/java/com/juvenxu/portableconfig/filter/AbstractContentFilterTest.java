package com.juvenxu.portableconfig.filter;

import com.juvenxu.portableconfig.ContentFilter;
import com.juvenxu.portableconfig.model.Replace;
import org.codehaus.plexus.PlexusTestCase;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

/**
 * @author juven
 */
public abstract class AbstractContentFilterTest extends PlexusTestCase
{
  protected ContentFilter sut;

  protected String doFilter(String input, List<Replace> replaces) throws IOException
  {
    StringWriter writer = new StringWriter();
    sut.filter(new StringReader(input), writer, replaces);
    return writer.toString();
  }
}
