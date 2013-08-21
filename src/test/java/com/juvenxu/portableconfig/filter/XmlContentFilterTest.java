package com.juvenxu.portableconfig.filter;

import com.juvenxu.portableconfig.ContentFilter;
import com.juvenxu.portableconfig.model.Replace;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;

/**
 * @author juven
 */
public class XmlContentFilterTest extends AbstractContentFilterTest
{
  @Override
  public void setUp() throws Exception
  {
    sut = lookup(ContentFilter.class, "xml");
  }

  public void testFilteringNothing() throws Exception
  {

    String input = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<server>\n" +
            "  <host>localhost</host>\n" +
            "  <port>8080</port>\n" +
            "</server>";
    List<Replace> replaces = new ArrayList<Replace>();

    String output = doFilter(input, replaces);
    Assert.assertThat(output, containsString("<server>"));
    Assert.assertThat(output, containsString("<host>localhost</host>"));
    Assert.assertThat(output, containsString("<port>8080</port>"));
    Assert.assertThat(output, containsString("</server>"));
  }


  public void testFilteringXmlWithoutNamespace() throws Exception
  {
    String input = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<server>\n" +
            "  <host>localhost</host>\n" +
            "  <port>8080</port>\n" +
            "</server>";

    List<Replace> replaces = new ArrayList<Replace>();
    replaces.add(new Replace(null, "/server/port", "80"));
    String output = doFilter(input, replaces);
    Assert.assertThat(output, containsString("<port>80</port>"));
  }

  public void testFilteringXmlWithNamespace() throws Exception
  {
    String input = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<web-app xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://java.sun.com/xml/ns/javaee\"\n" +
            "         xsi:schemaLocation=\"http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd\"\n" +
            "         version=\"2.5\">\n" +
            "    <display-name>test</display-name>\n" +
            "    <welcome-file-list>\n" +
            "        <welcome-file>index.html</welcome-file>\n" +
            "    </welcome-file-list>\n" +
            "</web-app>\n";

    List<Replace> replaces = new ArrayList<Replace>();
    replaces.add(new Replace(null, "/web-app/display-name", "staging"));
    String output = doFilter(input, replaces);
    Assert.assertThat(output, containsString("<display-name>staging</display-name>"));
  }

  public void testFilteringXmlXPathAttribute() throws Exception
  {
    String input = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<server region=\"zh\">\n" +
            "  <host>localhost</host>\n" +
            "</server>";

    List<Replace> replaces = new ArrayList<Replace>();
    replaces.add(new Replace(null, "/server/@region", "us"));
    String output = doFilter(input, replaces);
    Assert.assertThat(output, containsString("<server region=\"us\">"));
  }


  public void testFilteringXmlXPathAttributeWithNameSpace() throws Exception
  {
    String input = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
            + "<beans "
            + "xmlns=\"http://www.springframework.org/schema/beans\"\n"
            + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
            + "xsi:schemaLocation=\"http://www.springframework.org/schema/beans\n"
            + "http://www.springframework.org/schema/beans/spring-beans.xsd\""
            +">"
            + "<bean id=\"transactionManager\" class=\"org.springframework.jndi.JndiObjectFactoryBean\">\n"
            + "<property name=\"jndiName\" value=\"java:jboss/TransactionManager\"/>\n"
            + "<property name=\"resourceRef\" value=\"true\" />\n"
            + "</bean>\n"
            + "</beans>";

    List<Replace> replaces = new ArrayList<Replace>();
    replaces.add(new Replace(null, "//property[@name='jndiName']/@name", "newjndi"));
    String output = doFilter(input, replaces);

    Assert.assertThat(output, containsString("<property name=\"newjndi\" value=\"java:jboss/TransactionManager\" />" ));
  }
  
  public void testFilteringXmlXPathAttributeWithDOCTYPE() throws Exception
  {
    String input = "<?xml version='1.0' encoding='UTF-8'?>"
    		+ "<!DOCTYPE hibernate-configuration PUBLIC "
    		+ "\"-//Hibernate/Hibernate Configuration DTD 3.0//EN\" "
    		+ "\"http://hibernate.sourceforge.net/hibernate-configuration-3.0.dtd\">"
    		+ "<hibernate-configuration>	"
    		+ "<session-factory>	  "
    		+ "<property name=\"hibernate.connection.datasource\">java:jboss/datasources/MAYIDS</property>	"
    		+ "</session-factory></hibernate-configuration>";

    List<Replace> replaces = new ArrayList<Replace>();
    replaces.add(new Replace(null, "//property[@name='hibernate.connection.datasource']", "newdatasource"));
    String output = doFilter(input, replaces);

    Assert.assertThat(output, containsString("<property name=\"hibernate.connection.datasource\">newdatasource</property>" ));
  }

  public void testFilteringXmlElementWithAttribute() throws Exception
  {
    String input = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<server region=\"zh\">\n" +
            "  <host>localhost</host>\n" +
            "  <host id=\"to_be_replaced\">localhost</host>\n" +
            "</server>";

    List<Replace> replaces = new ArrayList<Replace>();
    replaces.add(new Replace(null, "//host[@id='to_be_replaced']", "192.168.1.1"));


    String output = doFilter(input, replaces);
    Assert.assertThat(output, containsString("<host>localhost</host>"));
    Assert.assertThat(output, containsString("<host id=\"to_be_replaced\">192.168.1.1</host>"));
  }


  public void testFilteringXmlWithNameSpaceAndXPathElementWithAttribute() throws Exception
  {

    String input = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<web-app xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://java.sun.com/xml/ns/javaee\"\n" +
            "         xsi:schemaLocation=\"http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd\"\n" +
            "         version=\"2.5\">\n" +
            "    <display-name>test</display-name>\n" +
            "    <servlet>\n" +
            "        <servlet-name>BarServlet</servlet-name>\n" +
            "        <servlet-class>org.foo.BarServlet</servlet-class>\n" +
            "        <init-param>\n" +
            "            <param-name>debug</param-name>\n" +
            "            <param-value id=\"DebugMode\">true</param-value>\n" +
            "        </init-param>\n" +
            "    </servlet>" +
            "</web-app>\n";

    List<Replace> replaces = new ArrayList<Replace>();
    replaces.add(new Replace(null, "//param-value[@id='DebugMode']", "false"));

    String output = doFilter(input, replaces);
    Assert.assertThat(output, containsString("<param-value id=\"DebugMode\">false</param-value>" ));
  }

  public void testFilteringXmlWithNameSpaceAndReplaceWithOriginalNameSpacePrefix() throws Exception
  {
    String input = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<beans xmlns=\"http://www.springframework.org/schema/beans\" " +
                   "xmlns:context=\"http://www.springframework.org/schema/context\" >\n" +
            "<context:property-placeholder ignore-unresolvable=\"true\" local-override=\"true\" />\n" +
            "</beans>\n";


    List<Replace> replaces = new ArrayList<Replace>();
    replaces.add(new Replace(null, "//context:property-placeholder/@local-override", "false"));

    String output = doFilter(input, replaces);
    Assert.assertThat(output, containsString("<context:property-placeholder ignore-unresolvable=\"true\" local-override=\"false\""));
  }

}
