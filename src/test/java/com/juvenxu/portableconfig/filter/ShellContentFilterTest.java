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
public class ShellContentFilterTest extends AbstractContentFilterTest
{

  @Override
  public void setUp() throws Exception
  {
    sut = lookup(ContentFilter.class, "shell");
  }

  public void testFilteringShellVariablesWithDoubleQuotes() throws Exception
  {
    String input = "OUT_HOME=\"/tmp/out\"\n" +
            "APP_HOME=\"/tmp/app\"\n";

    List<Replace> replaces = new ArrayList<Replace>();
    replaces.add(new Replace("OUT_HOME", null, "/var/out"));

    String output = doFilter(input, replaces);
    Assert.assertThat(output, containsString("OUT_HOME=\"/var/out\""));
    Assert.assertThat(output, containsString("APP_HOME=\"/tmp/app\""));
  }

  public void testFilteringShellVariablesWithSingleQuotes() throws Exception
  {
    String input = "OUT_HOME='/tmp/out'\n" +
            "APP_HOME='/tmp/app'\n";

    List<Replace> replaces = new ArrayList<Replace>();
    replaces.add(new Replace("OUT_HOME", null, "/var/out"));

    String output = doFilter(input, replaces);
    Assert.assertThat(output, containsString("OUT_HOME='/var/out'"));
    Assert.assertThat(output, containsString("APP_HOME='/tmp/app'"));
  }

  public void testFilteringShellVariablesWithoutQuotes() throws Exception
  {
    String input = "OUT_HOME=/tmp/out\n" +
            "APP_HOME=/tmp/app\n";

    List<Replace> replaces = new ArrayList<Replace>();
    replaces.add(new Replace("OUT_HOME", null, "/var/out"));

    String output = doFilter(input, replaces);
    Assert.assertThat(output, containsString("OUT_HOME=/var/out"));
    Assert.assertThat(output, containsString("APP_HOME=/tmp/app"));
  }

  public void testFilteringExportedShellVariables() throws Exception
  {
    String input = "export OUT_HOME=\"/tmp/out\"\n" +
            "export APP_HOME=\"/tmp/app\"\n";

    List<Replace> replaces = new ArrayList<Replace>();
    replaces.add(new Replace("OUT_HOME", null, "/var/out"));

    String output = doFilter(input, replaces);
    Assert.assertThat(output, containsString("export OUT_HOME=\"/var/out\""));
    Assert.assertThat(output, containsString("export APP_HOME=\"/tmp/app\""));
  }
}