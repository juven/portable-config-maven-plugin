package com.juvenxu.portableconfig.filter

import org.codehaus.plexus.PlexusTestCase
import org.scalatest.{BeforeAndAfter, FunSpec}
import org.scalatest.matchers.ShouldMatchers
import com.juvenxu.portableconfig.ContentFilter
import com.juvenxu.portableconfig.model.Replace
import java.io.{StringReader, StringWriter}

import collection.JavaConversions._

/**
 * @author juven
 */
class SXmlContentFilterTest extends PlexusTestCase with FunSpec with ShouldMatchers with BeforeAndAfter {

  private val sut = lookup(classOf[ContentFilter], "xml")


  describe("XmlContentFilter") {

    it("can filter xml element") {

      val input = """<?xml version="1.0" encoding="UTF-8"?>
                     <server>
                       <host>localhost</host>
                       <port>8080</port>
                     </server>"""

      val replaces = List(new Replace(null, "/server/port", "80"))
      val output = doFilter(input, replaces)
      output should include("<port>80</port>")
    }

    it("can filter xml attribute") {
      val input = """<?xml version="1.0" encoding="UTF-8"?>
                     <server region="us">
                       <host>localhost</host>
                     </server>"""

      val replaces = List(new Replace(null, "/server/@region", "us"))
      val output = doFilter(input, replaces)
      output should include( """<server region="us">""")
    }


  }


  def doFilter(input: String, replaces: List[Replace]) = {
    val writer = new StringWriter()
    sut.filter(new StringReader(input), writer, replaces)
    writer.toString
  }

}
