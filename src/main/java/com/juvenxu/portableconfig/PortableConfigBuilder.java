package com.juvenxu.portableconfig;

import com.juvenxu.portableconfig.model.PortableConfig;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author juven
 */
public interface PortableConfigBuilder
{
  PortableConfig build(InputStream portableConfigFile) throws IOException;
}
