package com.juvenxu.portableconfig;

import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.logging.Logger;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author juven
 */
public abstract class AbstractTraverser
{
  protected final Logger log;

  protected final List<ContentFilter> contentFilters;

  public AbstractTraverser(Logger log, List<ContentFilter> contentFilters)
  {
    this.log = log;

    this.contentFilters = contentFilters;
  }

  public abstract void traverse(PortableConfig portableConfig) throws IOException;

  protected boolean hasContentFilter(final String contentName)
  {
    return getContentFilter(contentName) != null;
  }

  protected ContentFilter getContentFilter(final String contentName)
  {
    for (ContentFilter contentFilter : contentFilters)
    {
      if (contentFilter.accept(contentName))
      {
        return contentFilter;
      }
    }

    return null;
  }
}
