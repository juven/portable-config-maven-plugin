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
public class ConfContentFilterTest extends AbstractContentFilterTest
{

  @Override
  public void setUp() throws Exception
  {
    sut = lookup(ContentFilter.class, "conf");
  }

  public void testFilteringConfReplaceShouldBeApplied() throws Exception
  {
    String input = "key1=value1\n" +
            "key2=value2";

    List<Replace> replaces = new ArrayList<Replace>();
    replaces.add(new Replace("key1", null, "replaced"));

    String output = doFilter(input, replaces);
    Assert.assertThat(output, containsString("key1=replaced"));
    Assert.assertThat(output, containsString("key2=value2"));
  }

  public void testFilteringConfCommentShouldBeIgnored() throws Exception
  {
    String input = "#key1=value1\n" +
            "key2=value2";

    List<Replace> replaces = new ArrayList<Replace>();
    replaces.add(new Replace("#key1", null, "replaced"));

    String output = doFilter(input, replaces);
    Assert.assertThat(output, containsString("#key1=value1"));
    Assert.assertThat(output, containsString("key2=value2"));
  }
}
