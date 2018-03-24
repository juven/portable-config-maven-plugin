package com.juvenxu.portableconfig.filter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.List;

import com.juvenxu.portableconfig.ContentFilter;
import com.juvenxu.portableconfig.model.Replace;

/**
 * @author juven
 */
public abstract class LineBasedContentFilter implements ContentFilter{
	protected abstract String filterLine(String line, List<Replace> replaces);

	@Override
	public void filter(InputStream fileIS, OutputStream tmpOS, List<Replace> replaces) throws IOException{
		this.filter(new InputStreamReader(fileIS), new OutputStreamWriter(tmpOS), replaces);
	}

	protected void filter(Reader reader, Writer writer, List<Replace> replaces) throws IOException{
		BufferedReader bufferedReader = new BufferedReader(reader);
		BufferedWriter bufferedWriter = new BufferedWriter(writer);
		while(bufferedReader.ready()){
			String line = bufferedReader.readLine();
			if(line == null){
				break;
			}
			bufferedWriter.write(filterLine(line, replaces));
			bufferedWriter.newLine();
		}
		bufferedWriter.flush();
	}
}
