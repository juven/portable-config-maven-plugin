package com.juvenxu.portableconfig;

import org.apache.commons.io.IOUtils;
import org.apache.maven.plugin.logging.Log;

import javax.activation.DataSource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author juven
 */
public class DefaultPortableConfigEngine implements PortableConfigEngine
{
  private PortableConfigBuilder portableConfigBuilder;

  private List<ContentFilter> contentFilters = new ArrayList<ContentFilter>();

  //TODO I don't want to bring in any maven stuff here, so should clean it in the future
  private Log log;

  public DefaultPortableConfigEngine(Log log)
  {
    this.log = log;
    this.portableConfigBuilder = new DefaultPortableConfigBuilder();
    contentFilters.add(new PropertiesContentFilter());
    contentFilters.add(new XmlContentFilter());
  }

  @Override
  public void replaceDirectory(DataSource portableConfigDataSource, File directory) throws IOException
  {
    PortableConfig portableConfig = buildPortableConfig(portableConfigDataSource);

    AbstractTraverser traverser = new DirectoryTraverser(log, directory);

    traverser.traverse(portableConfig);
  }

  @Override
  public void replaceJar(DataSource portableConfigDataSource, File jar) throws IOException
  {
    PortableConfig portableConfig = buildPortableConfig(portableConfigDataSource);

    AbstractTraverser traverser = new JarTraverser(log, jar);

    traverser.traverse(portableConfig);
  }

  private PortableConfig buildPortableConfig(DataSource portableConfigDataSource) throws IOException
  {
    InputStream inputStream = portableConfigDataSource.getInputStream();

    try
    {
      return portableConfigBuilder.build(portableConfigDataSource.getInputStream());
    }
    finally
    {
      IOUtils.closeQuietly(inputStream);
    }

  }

}
