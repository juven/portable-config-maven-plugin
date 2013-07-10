package com.juvenxu.portableconfig.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author juven
 */
public class ValuePool
{
  public Map<String, String> getValues()
  {
    return Collections.unmodifiableMap(values);
  }

  private Map<String, String> values = new HashMap<String, String>();

  public String get(String key)
  {
    return values.get(key);
  }

  public String put(String key, String value)
  {
    return values.put(key, value);
  }

  public boolean containsKey(Object key)
  {
    return values.containsKey(key);
  }

  public void putAll(Map<? extends String, ? extends String> m)
  {
    values.putAll(m);
  }
}
