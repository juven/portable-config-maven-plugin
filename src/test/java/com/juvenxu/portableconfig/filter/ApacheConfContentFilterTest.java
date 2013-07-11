package com.juvenxu.portableconfig.filter;

import com.juvenxu.portableconfig.ContentFilter;
import com.juvenxu.portableconfig.model.Replace;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;

/**
 * @author juven
 */
public class ApacheConfContentFilterTest extends AbstractContentFilterTest
{
  @Override
  public void setUp() throws Exception
  {
    sut = lookup(ContentFilter.class, "apache-conf");
  }

  public void testFilteringDirectives() throws Exception
  {
    String input = "ServerRoot /tmp/\n" +
            "PidFile /tmp/httpd.pid";

    List<Replace> replaces = new ArrayList<Replace>();
    replaces.add(new Replace("ServerRoot", null, "/home/admin/run/"));

    String output = doFilter(input, replaces);
    Assert.assertThat(output, containsString("ServerRoot /home/admin/run/"));
    Assert.assertThat(output, containsString("PidFile /tmp/httpd.pid"));
  }

  public void testFilteringDirectivesWithVariousSpaceSeparator() throws Exception
  {
    String input = "ServerRoot  /tmp/\n" +
            "PidFile\t/tmp/httpd.pid\n"+
            "Listen\t\t  8080";

    List<Replace> replaces = new ArrayList<Replace>();
    replaces.add(new Replace("ServerRoot", null, "/home/admin/run/"));
    replaces.add(new Replace("PidFile", null, "/home/admin/run/httpd.pid"));
    replaces.add(new Replace("Listen", null, "80"));

    String output = doFilter(input, replaces);
    Assert.assertThat(output, containsString("ServerRoot  /home/admin/run/"));
    Assert.assertThat(output, containsString("PidFile\t/home/admin/run/httpd.pid"));
    Assert.assertThat(output, containsString("Listen\t\t  80"));
  }


  public void testFilteringCommentShouldBeIgnored() throws Exception
  {
    String input = "#ServerRoot /tmp/";

    List<Replace> replaces = new ArrayList<Replace>();
    replaces.add(new Replace("ServerRoot", null, "/home/admin/run/"));

    String output = doFilter(input, replaces);
    Assert.assertThat(output, containsString("#ServerRoot /tmp/"));
  }

  public void testFilteringBlankLineShouldBeKept() throws Exception
  {
    String input = "ServerRoot /tmp/\n" +
            "\n" +
            "Listen 8080";

    List<Replace> replaces = new ArrayList<Replace>();

    String output = doFilter(input, replaces);
    Assert.assertThat(output, containsString("ServerRoot /tmp/\n" +
            "\n" +
            "Listen 8080"));
  }



}
