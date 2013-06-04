package com.juvenxu.portableconfig;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;

/**
 * @author juven
 */
@Mojo(
        name = "replace",
        defaultPhase = LifecyclePhase.PREPARE_PACKAGE
)
public class PortableConfigMojo extends AbstractMojo
{

  @Parameter(readonly = true, defaultValue = "${project.build.outputDirectory}")
  private File outputDirectory;

  @Parameter
  private File portableConfig;

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException
  {
    try
    {
      PortableConfigEngine portableConfigEngine = new DefaultPortableConfigEngine( outputDirectory.toURI().toURL(), getLog());

      portableConfigEngine.apply(new FileInputStream(portableConfig));
    }
    catch (FileNotFoundException e)
    {
      throw new MojoExecutionException( String.format("File %s does not exist.", portableConfig.getAbsolutePath()), e);
    }
    catch (PortableConfigException e)
    {
      throw new MojoFailureException("portable config", e);
    }
    catch (MalformedURLException e)
    {
      throw new MojoExecutionException( String.format("File %s does not exist.", outputDirectory.getAbsolutePath()), e);
    }


  }
}
