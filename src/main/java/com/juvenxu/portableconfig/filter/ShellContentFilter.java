package com.juvenxu.portableconfig.filter;

import java.util.List;

import org.codehaus.plexus.component.annotations.Component;

import com.juvenxu.portableconfig.ContentFilter;
import com.juvenxu.portableconfig.model.Replace;

/**
 * @author juven
 */
@Component(role = ContentFilter.class, hint = "shell")
public class ShellContentFilter extends LineBasedContentFilter{
	@Override
	public boolean accept(String contentType){
		return (".sh").equals(contentType);
	}

	@Override
	protected String filterLine(String line, List<Replace> replaces){
		for(Replace replace : replaces){
			if(line.matches("^(export\\s+){0,1}" + replace.getKey() + "=\"[^\"=]*\"")){
				return line.replaceAll("=\"[^\"=]*\"", "=\"" + replace.getValue() + "\"");
			}
			if(line.matches("^(export\\s+){0,1}" + replace.getKey() + "='[^'=]*'")){
				return line.replaceAll("='[^'=]*'", "='" + replace.getValue() + "'");
			}
			if(line.matches("^(export\\s+){0,1}" + replace.getKey() + "=[^'\"=]*")){
				return line.replaceAll("=[^'\"=]*", "=" + replace.getValue());
			}
		}
		return line;
	}
}
