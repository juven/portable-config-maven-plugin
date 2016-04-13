package com.juvenxu.portableconfig;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.util.Properties;

import javax.activation.DataSource;
import javax.activation.FileDataSource;

/**
 * @author juven
 */
public class DefaultPortableConfigEngineWithSourceTest extends DefaultPortableConfigEngineTestBase{
	private PortableConfigEngine sut;
	private File targetDirectory;

	@Override
	public void setUp() throws Exception{
		targetDirectory = new File(this.getClass().getResource("/to_be_replaced").toURI());
		sut = lookup(PortableConfigEngine.class);
	}

	public void testReplaceDirectoryWithValueLoadedFromFileSource() throws Exception{
		DataSource portableConfig = new FileDataSource(new File(this.getClass().getResource("/portable_config/" + "replace_with_value_is_placeholder.xml").toURI()));
		File source = new File(this.getClass().getResource("/source/product.properties").toURI());
		sut.replace(portableConfig, targetDirectory, source);
		Properties dbProperties = getResultProperties("db.properties");
		assertThat(dbProperties.getProperty("mysql.host"), equalTo("192.168.1.100"));
		Properties serverProperties = getResultProperties("server.properties");
		assertThat(serverProperties.getProperty("server.port"), equalTo("80"));
	}
}
