package com.juvenxu.portableconfig.model;

/**
 * @author juven
 */
public class Replace
{
  private final String key;

  private final String xpath;

  private final String value;

  public Replace(String key, String xpath, String value)
  {
    this.key = key;
    this.xpath = xpath;
    this.value = value;
  }

  public String getKey()
  {
    return key;
  }

  public String getValue()
  {
    return value;
  }

  public String getXpath()
  {
    return xpath;
  }

  @Override
  public boolean equals(Object o)
  {
    if (this == o) return true;
    if (!(o instanceof Replace)) return false;

    Replace replace = (Replace) o;

    if (key != null ? !key.equals(replace.key) : replace.key != null) return false;
    if (value != null ? !value.equals(replace.value) : replace.value != null) return false;
    if (xpath != null ? !xpath.equals(replace.xpath) : replace.xpath != null) return false;

    return true;
  }

  @Override
  public int hashCode()
  {
    int result = key != null ? key.hashCode() : 0;
    result = 31 * result + (xpath != null ? xpath.hashCode() : 0);
    result = 31 * result + (value != null ? value.hashCode() : 0);
    return result;
  }
}
