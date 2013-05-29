package com.juvenxu.portableconfig;

import java.io.*;
import java.net.URL;
import java.util.Properties;

/**
 * @author juven
 */
public class DefaultPortableConfigEngine implements PortableConfigEngine
{

  private final URL targetDirectory;

  public DefaultPortableConfigEngine(URL targetDirectory)
  {
    this.targetDirectory = targetDirectory;
  }

  @Override
  public void apply(InputStream portableConfigFile)
  {
    Properties properties = new Properties();

    File file = new File(targetDirectory.getFile(), "db.properties");


    InputStream is = null;

    try
    {
      is = new FileInputStream(file);
      properties.load(is);

    } catch (FileNotFoundException e)
    {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    } catch (IOException e)
    {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    } finally
    {
      if (is != null)
      {
        try
        {
          is.close();
        } catch (IOException e)
        {
          e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
      }
    }


    properties.setProperty("mysql.host", "192.168.1.100");

    OutputStream os = null;

    try
    {
      os = new FileOutputStream(file);
      properties.store(os, null);
    } catch (FileNotFoundException e)
    {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    } catch (IOException e)
    {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    } finally
    {
      if (os != null)
      {
        try
        {
          os.close();
        } catch (IOException e)
        {
          e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
      }
    }


  }
}
