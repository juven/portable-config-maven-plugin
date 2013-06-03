package com.juvenxu.portableconfig;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

/**
 * @author juven
 */
public class DefaultPortableConfigEngineTest
{

  private PortableConfigEngine sut;

  @Before
  public void setup()
  {
    URL targetDirectory = this.getClass().getResource("/to_be_replaced");
    sut = new DefaultPortableConfigEngine(targetDirectory);
  }

  @Test(expected = PortableConfigException.class)
  public void GIVEN_portable_config_file_path_does_not_exist_WHEN_run_engine_THEN_should_get_exception() throws Exception
  {
    applyPortableConfigXml("path_does_not_exist.xml");
  }

  @Test
  public void GIVEN_portable_config_replacing_properties_in_a_file_WHEN_run_engine_THEN_all_properties_should_be_replaced() throws Exception
  {
    applyPortableConfigXml("replace_properties_in_a_file.xml");

    Properties result = getResultProperties("db.properties");
    Assert.assertEquals("192.168.1.100", result.getProperty("mysql.host"));
    Assert.assertEquals("juven", result.getProperty("mysql.username"));
    Assert.assertEquals("test", result.getProperty("mysql.password"));
  }

  @Test
  public void GIVEN_portable_config_replacing_two_properties_files_WHEN_run_engine_THEN_both_file_should_be_replaced() throws Exception
  {
    applyPortableConfigXml("replace_two_properties_files.xml");

    Properties dbProperties = getResultProperties("db.properties");
    Assert.assertEquals("192.168.1.100", dbProperties.getProperty("mysql.host"));

    Properties serverProperties = getResultProperties("server.properties");
    Assert.assertEquals("80", serverProperties.getProperty("server.port"));
  }

  private void applyPortableConfigXml(String configXml) throws PortableConfigException
  {
    InputStream portableConfigFile = this.getClass().getResourceAsStream("/portable_config/" + configXml);
    sut.apply(portableConfigFile);
  }

  private Properties getResultProperties(String propertiesFile) throws IOException
  {
    Properties result = new Properties();
    result.load(this.getClass().getResourceAsStream("/to_be_replaced/" + propertiesFile));
    return result;
  }
}
