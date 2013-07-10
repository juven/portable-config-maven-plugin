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

  @Requirement(role = ValuePoolSource.class, hint = "default")
  private ValuePoolSource valuePoolSource;

  @Override
  public void replace(DataSource portableConfig, File file) throws IOException
  {
    PortableConfig config = buildPortableConfig(portableConfig);

    doReplace(config, file);
  }

  private void doReplace(PortableConfig config, File file) throws IOException
  {
    if (file.isFile())
    {
      jarTraverser.traverse(config, file);
    }
    else
    {
      directoryTraverser.traverse(config, file);
    }
  }

  @Override
  public void replace(DataSource portableConfig, File file, File source) throws IOException
  {
    PortableConfig config = buildPortableConfig(portableConfig);

    ValuePool valuePool = valuePoolSource.load(new FileDataSource(source));

    fill(config, valuePool);

    doReplace(config, file);
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
