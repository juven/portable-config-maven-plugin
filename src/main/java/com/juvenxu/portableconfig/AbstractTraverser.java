package com.juvenxu.portableconfig;

import com.juvenxu.portableconfig.model.PortableConfig;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.logging.AbstractLogEnabled;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author juven
 */
public abstract class AbstractTraverser extends AbstractLogEnabled
{
  @Requirement(role = ContentFilter.class)
  protected List<ContentFilter> contentFilters;

  public abstract void traverse(PortableConfig portableConfig, File file) throws IOException;

  protected boolean hasContentFilter(final String contentType)
  {
    return getContentFilter(contentType) != null;
  }

  protected ContentFilter getContentFilter(final String contentType)
  {
    for (ContentFilter contentFilter : contentFilters)
    {
      if (contentFilter.accept(contentType))
      {
        return contentFilter;
      }
    }

    return null;
  }
}
