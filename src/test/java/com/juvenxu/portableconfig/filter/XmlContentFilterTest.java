package com.juvenxu.portableconfig.filter;

import com.juvenxu.portableconfig.ContentFilter;
import com.juvenxu.portableconfig.model.Replace;
import org.codehaus.plexus.PlexusTestCase;
import org.junit.Assert;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;

/**
 * @author juven
 */
public class XmlContentFilterTest extends PlexusTestCase
{
  private ContentFilter sut;

  @Override
  public void setUp() throws Exception
  {
    sut = lookup(ContentFilter.class, "xml");
  }

  public void testFilteringNothing() throws Exception
  {

    String input = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<server>\n" +
            "  <host>localhost</host>\n" +
            "  <port>8080</port>\n" +
            "</server>";
    List<Replace> replaces = new ArrayList<Replace>();

    OutputStream outputStream = new ByteArrayOutputStream();

    sut.filter(new ByteArrayInputStream(input.getBytes()), outputStream, replaces);

    String output = outputStream.toString();
    Assert.assertThat(output, containsString("<server>"));
    Assert.assertThat(output, containsString("<host>localhost</host>"));
    Assert.assertThat(output, containsString("<port>8080</port>"));
    Assert.assertThat(output, containsString("</server>"));
  }


  public void testFilteringXmlWithoutNamespace() throws Exception
  {
    String input = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<server>\n" +
            "  <host>localhost</host>\n" +
            "  <port>8080</port>\n" +
            "</server>";

    List<Replace> replaces = new ArrayList<Replace>();
    replaces.add(new Replace(null, "/server/port", "80"));
    OutputStream outputStream = new ByteArrayOutputStream();

    sut.filter(new ByteArrayInputStream(input.getBytes()), outputStream, replaces);

    String output = outputStream.toString();
    Assert.assertThat(output, containsString("<port>80</port>"));
  }

  public void testFilteringXmlWithNamespace() throws Exception
  {
    String input = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<web-app xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://java.sun.com/xml/ns/javaee\"\n" +
            "         xsi:schemaLocation=\"http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd\"\n" +
            "         version=\"2.5\">\n" +
            "    <display-name>test</display-name>\n" +
            "    <welcome-file-list>\n" +
            "        <welcome-file>index.html</welcome-file>\n" +
            "    </welcome-file-list>\n" +
            "</web-app>\n";

    List<Replace> replaces = new ArrayList<Replace>();
    replaces.add(new Replace(null, "/web-app/display-name", "staging"));
    OutputStream outputStream = new ByteArrayOutputStream();

    sut.filter(new ByteArrayInputStream(input.getBytes()), outputStream, replaces);

    String output = outputStream.toString();
    Assert.assertThat(output, containsString("<display-name>staging</display-name>"));
  }
}
