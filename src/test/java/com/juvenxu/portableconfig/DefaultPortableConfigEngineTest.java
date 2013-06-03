package com.juvenxu.portableconfig;


import org.junit.Assert;
import org.junit.Test;

import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

/**
 * @author juven
 */
public class DefaultPortableConfigEngineTest
{
  @Test
  public void GIVEN_portable_config_replacing_property_WHEN_run_engine_THEN_property_should_be_replaced() throws Exception
  {
    URL targetDirectory = this.getClass().getResource("/to_be_replaced");

    PortableConfigEngine sut = new DefaultPortableConfigEngine(targetDirectory);

    InputStream portableConfigFile = this.getClass().getResourceAsStream("/portable_config/replace_property.xml");

    sut.apply(portableConfigFile);

    Properties result = new Properties();
    result.load(this.getClass().getResourceAsStream("/to_be_replaced/db.properties"));
    Assert.assertEquals("192.168.1.100", result.getProperty("mysql.host"));
    Assert.assertEquals("juven", result.getProperty("mysql.username"));
    Assert.assertEquals("test", result.getProperty("mysql.password"));
  }

}
