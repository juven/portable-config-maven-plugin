package com.juvenxu.portableconfig;

import java.io.IOException;

import javax.activation.DataSource;

import com.juvenxu.portableconfig.model.ValuePool;

/**
 * @author juven
 */
public interface ValuePoolSource{
	ValuePool load(DataSource dataSource) throws IOException;
}
