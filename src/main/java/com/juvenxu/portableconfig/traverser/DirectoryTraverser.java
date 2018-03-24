package com.juvenxu.portableconfig.traverser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.codehaus.plexus.component.annotations.Component;

import com.juvenxu.portableconfig.AbstractTraverser;
import com.juvenxu.portableconfig.ContentFilter;
import com.juvenxu.portableconfig.model.ConfigFile;
import com.juvenxu.portableconfig.model.PortableConfig;

/**
 * @author juven
 */
@Component(role = AbstractTraverser.class, hint = "directory")
public class DirectoryTraverser extends AbstractTraverser{
	@Override
	public void traverse(PortableConfig portableConfig, File directory) throws IOException{
		for(ConfigFile configFile : portableConfig.getConfigFiles()){
			File file = new File(directory, configFile.getPath());
			if(!file.exists() || file.isDirectory()){
				getLogger().warn(String.format("File: %s does not exist or is a directory.", file.getPath()));
				continue;
			}
			if(!hasContentFilter(configFile.getType())){
				getLogger().warn(String.format("Ignore replacing: %s", file.getPath()));
				continue;
			}
			getLogger().info(String.format("Replacing file: %s", file.getPath()));
			File tmpTxt = File.createTempFile(Long.toString(System.nanoTime()), ".txt");
			ContentFilter contentFilter = getContentFilter(configFile.getType());
			InputStream fileIS = new FileInputStream(file);
			OutputStream tmpOS = new FileOutputStream(tmpTxt);
			try{
				contentFilter.filter(fileIS, tmpOS, configFile.getReplaces());
			}finally{
				IOUtils.closeQuietly(fileIS);
				IOUtils.closeQuietly(tmpOS);
			}
			FileUtils.copyFile(tmpTxt, file);
			FileUtils.forceDeleteOnExit(tmpTxt);
		}
	}
}
