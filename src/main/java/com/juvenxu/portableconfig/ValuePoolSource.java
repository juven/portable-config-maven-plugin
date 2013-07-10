package com.juvenxu.portableconfig;

import com.juvenxu.portableconfig.model.ValuePool;

import javax.activation.DataSource;
import java.io.IOException;

/**
 * @author juven
 */
public interface ValuePoolSource
{
  ValuePool load(DataSource dataSource) throws IOException;
}
