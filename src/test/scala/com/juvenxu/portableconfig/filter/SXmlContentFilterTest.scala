package com.juvenxu.portableconfig.filter

import org.codehaus.plexus.PlexusTestCase
import org.scalatest.{BeforeAndAfter, FunSpec}
import org.scalatest.matchers.ShouldMatchers
import com.juvenxu.portableconfig.ContentFilter
import com.juvenxu.portableconfig.model.Replace
import java.io.{StringReader, StringWriter}

import collection.JavaConversions._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

/**
 * @author juven
 */
@RunWith(classOf[JUnitRunner])
class SXmlContentFilterTest extends PlexusTestCase with FunSpec with ShouldMatchers with BeforeAndAfter {

  private val sut = lookup(classOf[ContentFilter], "xml")


  describe("XmlContentFilter") {

    it("can filter xml element") {
      val input = """<?xml version="1.0" encoding="UTF-8"?>
                    |<server>
                    | <host>localhost</host>
                    | <port>8080</port>
                    |</server>""".stripMargin

      val replaces = List(new Replace(null, "/server/port", "80"))
      val output = doFilter(input, replaces)
      output should include("<port>80</port>")
    }

    it("can filter xml attribute") {
      val input = """<?xml version="1.0" encoding="UTF-8"?>
                    |<server region="us">
                    | <host>localhost</host>
                    |</server>""".stripMargin

      val replaces = List(new Replace(null, "/server/@region", "zh"))
      val output = doFilter(input, replaces)
      output should include( """<server region="zh">""")
    }

    it("can filter xml element with default namespace") {
      val input =
        """<?xml version="1.0" encoding="UTF-8"?>
          |<web-app xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns="http://java.sun.com/xml/ns/javaee"
          | xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd"
          | version="2.5">
          | <display-name>test</display-name>
          | <welcome-file-list>
          |   <welcome-file>index.html</welcome-file>
          | </welcome-file-list>
          |</web-app>
        """.stripMargin

      val replaces = List(new Replace(null, "/web-app/display-name", "staging"))
      val output = doFilter(input, replaces)
      output should include( """<display-name>staging</display-name>""")
    }

    it("can filter xml attribute with default namespace") {
      var input =
      """"""
    }


  }


  def doFilter(input: String, replaces: List[Replace]) = {
    val writer = new StringWriter()
    sut.filter(new StringReader(input), writer, replaces)
    writer.toString
  }

}
