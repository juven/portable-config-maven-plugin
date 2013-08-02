package com.juvenxu.portableconfig;


import org.apache.maven.monitor.logging.DefaultLog;
import org.codehaus.plexus.PlexusTestCase;
import org.codehaus.plexus.logging.console.ConsoleLogger;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import org.junit.Before;
import org.junit.Test;

import javax.activation.DataSource;
import javax.activation.FileDataSource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.assertEquals;

/**
 * @author juven
 */
public class DefaultPortableConfigEngineTest extends DefaultPortableConfigEngineTestBase
{

  private PortableConfigEngine sut;

  private File targetDirectory;


  @Override
  public void setUp() throws Exception
  {
    targetDirectory = new File(this.getClass().getResource("/to_be_replaced").toURI());
    sut = lookup(PortableConfigEngine.class);
  }




  public void test_GIVEN_portable_config_replacing_properties_in_a_file_WHEN_run_engine_THEN_properties_should_be_replaced() throws Exception
  {
    applyPortableConfigXml("replace_properties_in_a_file.xml");

    Properties result = getResultProperties("db.properties");
    assertEquals("192.168.1.100", result.getProperty("mysql.host"));
    assertEquals("juven", result.getProperty("mysql.username"));
    assertEquals("test", result.getProperty("mysql.password"));
  }

  public void test_GIVEN_portable_config_replacing_an_ini_file_but_has_type_properties_WHEN_run_engine_THEN_properties_should_be_replaced() throws Exception
  {
    applyPortableConfigXml("replace_ini_but_has_type_properties.xml");

    Properties result = getResultProperties("db.ini");
    assertEquals("192.168.1.100", result.getProperty("mysql.host"));
    assertEquals("juven", result.getProperty("mysql.username"));
    assertEquals("test", result.getProperty("mysql.password"));
  }


  public void test_GIVEN_portable_config_replacing_two_properties_files_WHEN_run_engine_THEN_both_file_should_be_replaced() throws Exception
  {
    applyPortableConfigXml("replace_two_properties_files.xml");

    Properties dbProperties = getResultProperties("db.properties");
    assertEquals("192.168.1.100", dbProperties.getProperty("mysql.host"));

    Properties serverProperties = getResultProperties("server.properties");
    assertEquals("80", serverProperties.getProperty("server.port"));
  }


  public void test_GIVEN_portable_config_replacing_xml_entries_in_a_file_WHEN_run_engine_THEN_entries_should_be_replaced() throws Exception
  {
    applyPortableConfigXml("replace_xml_entries_in_a_file.xml");

    assertEquals("192.168.1.100", getResultXmlEntry("db.xml", "/db/mysql/host"));
    assertEquals("juven", getResultXmlEntry("db.xml", "/db/mysql/username"));
  }


  public void test_GIVEN_portable_config_replacing_two_xml_files_WHEN_run_engine_THEN_both_file_should_be_replaced() throws Exception
  {
    applyPortableConfigXml("replace_two_xml_files.xml");
    assertEquals("192.168.1.100", getResultXmlEntry("db.xml", "/db/mysql/host"));
    assertEquals("80", getResultXmlEntry("server.xml", "/server/port"));
  }


  public void test_GIVEN_portable_config_replacing_properties_file_and_xml_file_WHEN_run_engine_THEN_both_file_should_be_replaced() throws Exception
  {
    applyPortableConfigXml("replace_properties_file_and_xml_file.xml");

    assertEquals("192.168.1.100", getResultProperties("db.properties").getProperty("mysql.host"));
    assertEquals("80", getResultXmlEntry("server.xml", "/server/port"));
  }

  private void applyPortableConfigXml(String configXml) throws URISyntaxException, IOException
  {
    DataSource portableConfig = new FileDataSource( new File( this.getClass().getResource("/portable_config/" + configXml).toURI()));
    sut.replace(portableConfig, targetDirectory);
  }

}
