package com.juvenxu.portableconfig;

import java.io.IOException;
import java.io.InputStream;

import com.juvenxu.portableconfig.model.PortableConfig;

/**
 * @author juven
 */
public interface PortableConfigBuilder{
	PortableConfig build(InputStream portableConfigFile) throws IOException;
}
