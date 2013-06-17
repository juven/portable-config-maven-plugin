package com.juvenxu.portableconfig;

import org.apache.maven.plugin.logging.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author juven
 */
public abstract class AbstractTraverser
{

  protected Log log;

  protected List<ContentFilter> contentFilters = new ArrayList<ContentFilter>();

  public AbstractTraverser( Log log)
  {
    this.log = log;

    contentFilters.add(new PropertiesContentFilter());
    contentFilters.add(new XmlContentFilter());
  }

  public abstract void traverse(PortableConfig portableConfig) throws IOException;

  protected boolean hasContentFilter(final String contentName)
  {
    return getContentFilter(contentName) != null;
  }

  protected ContentFilter getContentFilter(final String contentName)
  {
    for ( ContentFilter contentFilter : contentFilters)
    {
      if ( contentFilter.accept(contentName))
      {
        return contentFilter;
      }
    }

    return null;
  }
}
