package com.juvenxu.portableconfig;

import org.apache.maven.monitor.logging.DefaultLog;
import org.codehaus.plexus.logging.console.ConsoleLogger;
import org.junit.Before;
import org.junit.Test;

import javax.activation.DataSource;
import javax.activation.FileDataSource;
import java.io.File;
import java.net.URL;

/**
 * @author juven
 */

public class DefaultPortableConfigEngineReplaceJarTest
{

  private PortableConfigEngine sut;


  @Before
  public void setup()
  {
    sut = new DefaultPortableConfigEngine(new DefaultLog(new ConsoleLogger()));
  }

  @Test
  public void replaceJarWebXml() throws Exception
  {
    DataSource potableConfigDataSource = new FileDataSource(new File(this.getClass().getResource("/portable_config/" + "replace_jar_web_xml.xml").toURI()));
    File jar = new File(this.getClass().getResource("/to_be_replaced/" + "portableconfig-demo-1.0-SNAPSHOT.war").toURI());

    sut.replaceJar(potableConfigDataSource, jar);
  }
}
