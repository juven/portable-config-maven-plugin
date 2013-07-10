package com.juvenxu.portableconfig.cli;

import com.juvenxu.portableconfig.PortableConfigEngine;
import org.apache.commons.cli.*;
import org.codehaus.plexus.DefaultPlexusContainer;
import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.PlexusContainerException;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;

import javax.activation.FileDataSource;
import java.io.File;
import java.io.IOException;

/**
 * @author juven
 */
public class PortableConfigCli
{
  public static void main(String[] args) throws Exception
  {
    Options options = buildOptions();
    CommandLine cmd = new BasicParser().parse(options, args);

    displayHelpIfNeeded(options, cmd);

    File target = new File(cmd.getArgs()[0]);
    File config = new File(cmd.getOptionValue("c"));

    verify(target, config);

    run(target, config);
  }

  private static void run(File target, File config) throws PlexusContainerException, ComponentLookupException, IOException
  {
    PlexusContainer container = new DefaultPlexusContainer();
    PortableConfigEngine engine = container.lookup(PortableConfigEngine.class);

    engine.replace(new FileDataSource(config), target);
  }

  private static void verify(File target, File config)
  {
    if (!target.exists())
    {
      System.err.println("File/Directory: " + target.getAbsolutePath() + " does not exist");
      System.exit(1);
    }

    if (!config.exists())
    {
      System.err.println("Portable Config: " + config.getAbsolutePath() + " does not exist");
      System.exit(1);
    }
  }

  private static void displayHelpIfNeeded(Options options, CommandLine cmd)
  {
    if (cmd.hasOption("h"))
    {
      HelpFormatter formatter = new HelpFormatter();
      formatter.printHelp("java -jar portable-config-maven-plugin-1.0.2-cli.jar", options);
      System.exit(0);
    }
  }

  private static Options buildOptions()
  {
    Options options = new Options();

    Option c = OptionBuilder.withArgName("portableConfigFile").hasArg().withDescription("the portable config file").create("c");
    Option s = OptionBuilder.withArgName("sourceFile").hasArg().withDescription("the source file of config values").create("s");
    Option h = OptionBuilder.withDescription("display help").create("h");

    options.addOption(c);
    options.addOption(h);
    options.addOption(s);
    return options;
  }

}
