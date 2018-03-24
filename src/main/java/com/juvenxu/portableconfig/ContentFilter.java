package com.juvenxu.portableconfig;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import com.juvenxu.portableconfig.model.Replace;

/**
 * @author juven
 */
public interface ContentFilter{
	boolean accept(String contentType);

	void filter(InputStream fileIS, OutputStream tmpOS, List<Replace> replaces) throws IOException;
}
