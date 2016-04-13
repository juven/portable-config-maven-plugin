package com.juvenxu.portableconfig.source;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import javax.activation.DataSource;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.text.StrSubstitutor;
import org.codehaus.plexus.component.annotations.Component;

import com.juvenxu.portableconfig.ValuePoolSource;
import com.juvenxu.portableconfig.model.ValuePool;

/**
 * @author juven
 */
@Component(role = ValuePoolSource.class, hint = "default")
public class ValuePoolSourceDefaultImpl implements ValuePoolSource
{
  @Override
  public ValuePool load(DataSource dataSource) throws IOException
  {
    ValuePool result = new ValuePool();

    Map<String, String> rawValues = loadRawValues(dataSource);

    Map<String, String> cleanValues = new HashMap<String, String>();

    processReferences(cleanValues, rawValues);

    result.putAll(cleanValues);

    return result;
  }

  private void processReferences(Map<String, String> cleanValues, Map<String, String> rawValues)
  {
    if (rawValues.isEmpty())
    {
      return;
    }

    int cleanValuesBefore = cleanValues.size();

    for (Map.Entry<String, String> entry : rawValues.entrySet())
    {
      StrSubstitutor strSubstitutor = new StrSubstitutor(cleanValues);
      entry.setValue(strSubstitutor.replace(entry.getValue()));
    }

    for (Map.Entry<String, String> entry : rawValues.entrySet())
    {
      if (!entry.getValue().contains("${"))
      {
        cleanValues.put(entry.getKey(), entry.getValue());
      }
    }

    for (String key : cleanValues.keySet())
    {
      rawValues.remove(key);
    }

    int cleanValuesAfter = cleanValues.size();

    if (cleanValuesBefore == cleanValuesAfter)
    {
      cleanValues.putAll(rawValues);

      return;
    }

    processReferences(cleanValues, rawValues);
  }

  private Map<String, String> loadRawValues(DataSource dataSource) throws IOException
  {
    Map<String, String> values = new HashMap<String, String>();

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

          String key = line.substring(0, pos).trim();
          String value = line.substring(pos + 1).trim();

          values.put(key, value);
        }
      }
    } finally
    {
      IOUtils.closeQuietly(inputStream);
    }

    return values;
  }

}
