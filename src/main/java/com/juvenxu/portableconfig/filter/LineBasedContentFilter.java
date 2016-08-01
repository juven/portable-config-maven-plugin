package com.juvenxu.portableconfig.filter;

import com.juvenxu.portableconfig.ContentFilter;
import com.juvenxu.portableconfig.model.Replace;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author juven
 */
public abstract class LineBasedContentFilter implements ContentFilter {
    protected abstract String filterLine(String line, List<Replace> replaces);

    @Override
    public void filter(InputStream fileIS, OutputStream tmpOS, List<Replace> replaces) throws IOException {
        this.filter(new InputStreamReader(fileIS), new OutputStreamWriter(tmpOS), replaces);
    }

    protected void filter(Reader reader, Writer writer, List<Replace> replaces) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(reader);
        BufferedWriter bufferedWriter = new BufferedWriter(writer);
        List<String> checkflag = new ArrayList<String>();
        while (bufferedReader.ready()) {
            String line = bufferedReader.readLine();
            checkflag.add(line);

            if (line == null) {
                break;
            }

            bufferedWriter.write(filterLine(line, replaces));
            bufferedWriter.newLine();
        }
        for (Replace a : replaces) {
            System.err.println("未找到替换条件" + a.getKey());
        }

        bufferedWriter.flush();
    }
}
