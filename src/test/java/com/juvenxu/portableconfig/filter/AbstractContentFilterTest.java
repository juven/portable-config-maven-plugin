package com.juvenxu.portableconfig.filter;

import com.juvenxu.portableconfig.ContentFilter;
import com.juvenxu.portableconfig.model.Replace;
import org.apache.commons.io.IOUtils;
import org.codehaus.plexus.PlexusTestCase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * @author juven
 */
public abstract class AbstractContentFilterTest extends PlexusTestCase
{
  protected ContentFilter sut;

  protected String doFilter(String input, List<Replace> replaces) throws IOException
  {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    sut.filter(IOUtils.toInputStream(input), outputStream, replaces);

    return outputStream.toString();
  }
}
