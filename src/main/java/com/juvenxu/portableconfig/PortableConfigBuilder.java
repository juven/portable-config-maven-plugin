package com.juvenxu.portableconfig;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author juven
 */
public interface PortableConfigBuilder
{
  PortableConfig build(InputStream portableConfigFile) throws IOException;
}
