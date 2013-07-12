package com.juvenxu.portableconfig.filter;

import com.juvenxu.portableconfig.ContentFilter;
import com.juvenxu.portableconfig.model.Replace;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author juven
 */
public class PropertiesContentFilterTest extends AbstractContentFilterTest
{
  @Override
  public void setUp() throws Exception
  {
    sut = lookup(ContentFilter.class, "properties");
  }

  public void testLineStartWithSharpIsCommentShouldBeIgnored() throws Exception
  {
    String input = "#key1=value1";

    List<Replace> replaces = new ArrayList<Replace>();
    replaces.add(new Replace("#key1", null, "replaced"));

    String result = doFilter(input, replaces);
    assertThat(result, containsString("#key1=value1"));
  }

  public void testLineStartWithExclamationIsCommentShouldBeIgnored() throws Exception
  {
    String input = "!key1=value1";

    List<Replace> replaces = new ArrayList<Replace>();
    replaces.add(new Replace("!key1", null, "replaced"));

    String result = doFilter(input, replaces);
    assertThat(result, containsString("!key1=value1"));
  }

  public void testLineWithEqualsSeparator() throws Exception
  {
    String input = "key1=value1";

    List<Replace> replaces = new ArrayList<Replace>();
    replaces.add(new Replace("key1", null, "replaced"));

    String result = doFilter(input, replaces);
    assertThat(result, containsString("key1=replaced"));
  }

  public void testLineWithColonSeparator() throws Exception
  {
    String input = "key1:value1";

    List<Replace> replaces = new ArrayList<Replace>();
    replaces.add(new Replace("key1", null, "replaced"));

    String result = doFilter(input, replaces);
    assertThat(result, containsString("key1:replaced"));
  }

  public void testOrderShouldBeKept() throws Exception
  {
    String input = "key1:value1\n" +
            "key3:value3\n" +
            "key2:value2\n" +
            "key4:value4\n";

    List<Replace> replaces = new ArrayList<Replace>();
    replaces.add(new Replace("key1", null, "replaced"));
    replaces.add(new Replace("key2", null, "replaced"));

    String result = doFilter(input, replaces);
    assertThat(result, containsString("key1:replaced\n" +
            "key3:value3\n" +
            "key2:replaced\n" +
            "key4:value4\n"));
  }
}