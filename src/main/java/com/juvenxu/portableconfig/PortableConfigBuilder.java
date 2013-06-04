package com.juvenxu.portableconfig;

import java.io.InputStream;

/**
 * @author juven
 */
public interface PortableConfigBuilder
{
  PortableConfig build(InputStream portableConfigFile);
}
