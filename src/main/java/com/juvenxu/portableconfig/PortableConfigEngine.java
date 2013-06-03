package com.juvenxu.portableconfig;

import java.io.File;
import java.io.InputStream;

/**
 * @author juven
 */
public interface PortableConfigEngine
{
  public void apply(InputStream portableConfigFile) throws PortableConfigException;
}
