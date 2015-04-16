package com.grandata.commons.files

import scala.io.Source._
import java.io._
import java.util.zip.{GZIPOutputStream, GZIPInputStream}


/**
 * @author esteban
 */
object FileUtils {

  trait StreamConverter {
    def buildStream(f: File): OutputStream
  }

  object IdentityStreamConverter extends StreamConverter {
    def buildStream(f: File): OutputStream = new FileOutputStream(f)
  }

  object GzStreamConverter extends StreamConverter {
    def buildStream(f: File): OutputStream = new GZIPOutputStream(new FileOutputStream(f))
  }

  def printToFile(f: String, d: String): Unit = printToFile(new File(f), d)
  def printToFile(f: File, d: String): Unit = getFiltePrinter(f) { p=> p.println(d) }

  def getFiltePrinter(f: String)(op: java.io.PrintWriter => Unit): Unit =
    getFiltePrinter(new File(f)) _


  def getFiltePrinter(f: File)(op: java.io.PrintWriter => Unit)(implicit converter: StreamConverter = IdentityStreamConverter): Unit = {
    val p = new java.io.PrintWriter(converter.buildStream(f))
    try {
      op(p)
    } finally {
      p.close()
    }
  }

  def fileContent(f: String) = fromFile(f).mkString

  def gzippedFileContent(f: String) =
    fromInputStream(new GZIPInputStream(new BufferedInputStream(new FileInputStream(f)))).mkString

  def gzippedFileLines(f: String) =
    fromInputStream(new GZIPInputStream(new BufferedInputStream(new FileInputStream(f)))).getLines()

  def resourceContent(r: String) = fromURL(getClass.getResource(r)).mkString
}