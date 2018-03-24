package com.juvenxu.portableconfig.filter;

import java.util.List;

import org.codehaus.plexus.component.annotations.Component;

import com.juvenxu.portableconfig.ContentFilter;
import com.juvenxu.portableconfig.model.Replace;

/**
 * @author juven
 */
@Component(role = ContentFilter.class, hint = "properties")
public class PropertiesContentFilter extends LineBasedContentFilter{
	@Override
	public boolean accept(String contentType){
		return ".properties".equals(contentType);
	}

	@Override
	protected String filterLine(String line, List<Replace> replaces){
		if(isComment(line)){
			return line;
		}
		final String equalsMark = "=";
		final int equalsMarkIndex = line.indexOf(equalsMark);
		final String colonMark = ":";
		final int colonMarkIndex = line.indexOf(colonMark);
		if(equalsMarkIndex != -1){
			return replaceLineWith(line, replaces, equalsMark, equalsMarkIndex);
		}
		if(colonMarkIndex != -1){
			return replaceLineWith(line, replaces, colonMark, colonMarkIndex);
		}
		return line;
	}

	private String replaceLineWith(String line, List<Replace> replaces, String equalsMark, int equalsMarkIndex){
		String key = line.substring(0, equalsMarkIndex).trim();
		for(final Replace replace : replaces){
			if(replace.getKey().equals(key)){
				return key + equalsMark + replace.getValue();
			}
		}
		return line;
	}

	private boolean isComment(String line){
		return line.startsWith("#") || line.startsWith("!");
	}
}
