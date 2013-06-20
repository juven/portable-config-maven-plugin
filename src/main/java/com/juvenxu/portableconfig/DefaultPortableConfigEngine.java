package com.juvenxu.portableconfig;

import org.apache.commons.io.IOUtils;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.logging.AbstractLogEnabled;

import javax.activation.DataSource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author juven
 */
@Component(role = PortableConfigEngine.class)
public class DefaultPortableConfigEngine extends AbstractLogEnabled implements PortableConfigEngine
{
  @Requirement
  private PortableConfigBuilder portableConfigBuilder;

  @Requirement(role = ContentFilter.class)
  private List<ContentFilter> contentFilters;

  public DefaultPortableConfigEngine()
  {}

  @Override
  public void replaceDirectory(DataSource portableConfigDataSource, File directory) throws IOException
  {
    PortableConfig portableConfig = buildPortableConfig(portableConfigDataSource);

    AbstractTraverser traverser = new DirectoryTraverser(getLogger(),contentFilters, directory);

    traverser.traverse(portableConfig);
  }

  @Override
  public void replaceJar(DataSource portableConfigDataSource, File jar) throws IOException
  {
    PortableConfig portableConfig = buildPortableConfig(portableConfigDataSource);

    AbstractTraverser traverser = new JarTraverser(getLogger(),contentFilters, jar);

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
