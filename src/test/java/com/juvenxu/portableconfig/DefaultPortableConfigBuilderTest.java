package com.juvenxu.portableconfig;


import org.junit.Assert;
import org.junit.Test;

import java.io.InputStream;

/**
 * @author juven
 */
public class DefaultPortableConfigBuilderTest
{
  PortableConfigBuilder sut = new DefaultPortableConfigBuilder();

  @Test
  public void buildATypicalPortableConfig() throws Exception
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

      Assert.assertEquals(expected, actual);
    }
    finally
    {
      inputStream.close();
    }
  }
}
