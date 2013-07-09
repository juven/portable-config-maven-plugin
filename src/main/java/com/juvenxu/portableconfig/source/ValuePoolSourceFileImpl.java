package com.juvenxu.portableconfig.source;

import com.juvenxu.portableconfig.ValuePoolSource;
import com.juvenxu.portableconfig.model.Replace;
import com.juvenxu.portableconfig.model.ValuePool;
import org.apache.commons.io.IOUtils;
import org.codehaus.plexus.component.annotations.Component;

import javax.activation.DataSource;
import java.io.*;
import java.util.List;

/**
 * @author juven
 */
@Component(role = ValuePoolSource.class, hint = "file")
public class ValuePoolSourceFileImpl implements ValuePoolSource
{
  @Override
  public ValuePool build(DataSource dataSource) throws IOException
  {
    ValuePool result = new ValuePool();

    InputStream inputStream = dataSource.getInputStream();

    try
    {
      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

      while (bufferedReader.ready())
      {
        String line = bufferedReader.readLine();

        if (line == null)
        {
          break;
        }

        if (line.startsWith("#"))
        {
          continue;
        }

        if (line.contains("="))
        {
          int pos = line.indexOf("=");

          result.put(line.substring(0, pos), line.substring(pos + 1));
        }
      }
    } finally
    {
      IOUtils.closeQuietly(inputStream);
    }

    return result;
  }

}
