package com.juvenxu.portableconfig;

import org.apache.commons.io.IOUtils;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;

import javax.activation.DataSource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author juven
 */
@Component(role = PortableConfigEngine.class)
public class DefaultPortableConfigEngine implements PortableConfigEngine
{
  @Requirement
  private PortableConfigBuilder portableConfigBuilder;

  @Requirement(role = ContentFilter.class)
  private List<ContentFilter> contentFilters;

  @Requirement(role = AbstractTraverser.class, hint = "jar")
  private AbstractTraverser jarTraverser;

  @Requirement(role = AbstractTraverser.class, hint = "directory")
  private AbstractTraverser directoryTraverser;

  public DefaultPortableConfigEngine()
  {}

  @Override
  public void replaceDirectory(DataSource portableConfigDataSource, File directory) throws IOException
  {
    directoryTraverser.traverse(buildPortableConfig(portableConfigDataSource), directory);
  }

  @Override
  public void replaceJar(DataSource portableConfigDataSource, File jar) throws IOException
  {
    jarTraverser.traverse(buildPortableConfig(portableConfigDataSource), jar);
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
