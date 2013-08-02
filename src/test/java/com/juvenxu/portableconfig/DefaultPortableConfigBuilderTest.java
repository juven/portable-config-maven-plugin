package com.juvenxu.portableconfig;


import com.juvenxu.portableconfig.model.ConfigFile;
import com.juvenxu.portableconfig.model.PortableConfig;
import com.juvenxu.portableconfig.model.Replace;
import org.codehaus.plexus.PlexusTestCase;
import org.codehaus.plexus.util.StringInputStream;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author juven
 */
public class DefaultPortableConfigBuilderTest extends PlexusTestCase
{
  private PortableConfigBuilder sut;


  @Override
  public void setUp() throws Exception
  {
    super.setUp();
    sut = lookup(PortableConfigBuilder.class);
  }


  public void testBuildATypicalPortableConfig() throws Exception
  {
    InputStream inputStream =  this.getClass().getResourceAsStream("/portable_config/typical.xml");

    try
    {
      PortableConfig actual = sut.build(inputStream);

      PortableConfig expected = new PortableConfig();

      ConfigFile configFileA = new ConfigFile("db.properties");
      configFileA.addReplace(new Replace("mysql.host", null, "192.168.1.100"));
      configFileA.addReplace(new Replace("mysql.username", null, "juven"));

      ConfigFile configFileB = new ConfigFile("WEB-INF/web.xml");
      configFileB.addReplace(new Replace(null, "/web-app/display-name", "replaced"));

      expected.getConfigFiles().add(configFileA);
      expected.getConfigFiles().add(configFileB);

      assertThat(actual, equalTo(expected));
    }
    finally
    {
      inputStream.close();
    }
  }

  public void testBuildPortableConfigWithFileTypeSpecified() throws Exception
  {
    String input = "<portable-config>\n" +
            "    <config-file path=\"web.xml\" type=\".properties\">\n" +
            "        <replace xpath=\"/web-app/display-name\">replaced</replace>\n" +
            "    </config-file>\n" +
            "</portable-config>";

    PortableConfig actual = sut.build( new ByteArrayInputStream(input.getBytes()));

    PortableConfig expected = new PortableConfig();

    ConfigFile configFile = new ConfigFile("web.xml", ".properties");
    configFile.addReplace(new Replace(null, "/web-app/display-name", "replaced"));
    expected.getConfigFiles().add(configFile);

    assertThat(actual, equalTo(expected));
  }
}
