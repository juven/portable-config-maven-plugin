package com.juvenxu.portableconfig;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import javax.activation.FileDataSource;
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
public class PortableConfigReplaceMojo extends AbstractMojo
{

  @Parameter(readonly = true, defaultValue = "${project.build.outputDirectory}")
  private File outputDirectory;

  @Parameter
  private File portableConfig;

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException
  {


      PortableConfigEngine portableConfigEngine = new DefaultPortableConfigEngine(
              getLog());

      portableConfigEngine.replaceDirectory(new FileDataSource(portableConfig), outputDirectory);



  }
}
