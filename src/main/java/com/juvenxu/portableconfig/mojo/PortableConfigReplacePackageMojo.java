package com.juvenxu.portableconfig.mojo;

import com.juvenxu.portableconfig.PortableConfigEngine;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.util.StringUtils;

import javax.activation.FileDataSource;
import java.io.File;
import java.io.IOException;

/**
 * @author juven
 */
@Mojo(
        name = "replace-package",
        defaultPhase = LifecyclePhase.PACKAGE
)
public class PortableConfigReplacePackageMojo extends AbstractMojo
{

  @Parameter(property = "portableConfig")
  private File portableConfig;

  @Parameter(readonly = true, defaultValue = "${project.packaging}")
  private String packaging;

  @Parameter(readonly = true, defaultValue = "${project.build.directory}/${project.build.finalName}")
  private File outputDirectory;

  @Parameter(readonly = true, defaultValue = "${project.build.directory}/${project.build.finalName}.${project.packaging}")
  private File finalPackage;

  @Component(role = PortableConfigEngine.class)
  private PortableConfigEngine portableConfigEngine;

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException
  {

    if (portableConfig == null || !portableConfig.exists())
    {
      getLog().warn("No portable config file is provided, skipping running.");

      return;
    }

    if (isSupported(packaging))
    {
      try
      {
        if (outputDirectory.exists() && outputDirectory.isDirectory())
        {
          getLog().info("Replacing: " + outputDirectory.getAbsolutePath());

          portableConfigEngine.replace(new FileDataSource(portableConfig), outputDirectory);
        }

        getLog().info("Replacing: " + finalPackage.getAbsolutePath());

        portableConfigEngine.replace(new FileDataSource(portableConfig), finalPackage);
      }
      catch (IOException e)
      {
        throw new MojoExecutionException("Error while replacing package", e);
      }
    }
    else
    {
      getLog().info(String.format("Ignoring packaging %s", packaging));

      return;
    }


  }

  private boolean isSupported(String packaging)
  {
    return "war".equals(packaging) || "jar".equals(packaging);
  }
}
