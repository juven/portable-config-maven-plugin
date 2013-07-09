package com.juvenxu.portableconfig;

import com.juvenxu.portableconfig.model.ConfigFile;
import com.juvenxu.portableconfig.model.PortableConfig;
import com.juvenxu.portableconfig.model.Replace;
import com.juvenxu.portableconfig.model.ValuePool;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.text.StrSubstitutor;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;

import javax.activation.DataSource;
import javax.activation.FileDataSource;
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

  @Requirement(role = ValuePoolSource.class, hint = "file")
  private ValuePoolSource valuePoolSource;

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

  @Override
  public void replace(DataSource portableConfig, File file, File source) throws IOException
  {
    PortableConfig config = buildPortableConfig(portableConfig);

    ValuePool valuePool = valuePoolSource.build(new FileDataSource(source));

    fill(config, valuePool);

    if (file.isDirectory())
    {
      directoryTraverser.traverse(config, file);
    }
    else if (file.isFile())
    {
      jarTraverser.traverse(config, file);
    }
  }

  private void fill(PortableConfig config, ValuePool valuePool)
  {
    for (ConfigFile configFile : config.getConfigFiles())
    {
      for (Replace replace : configFile.getReplaces())
      {
        String value = replace.getValue();

        if (value.contains("${") && value.contains("}"))
        {
          StrSubstitutor strSubstitutor = new StrSubstitutor(valuePool.getValues());

          replace.setValue(strSubstitutor.replace(value));
        }
      }
    }
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
