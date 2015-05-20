package com.grandata.commons.files

import scala.io.Source._
import java.io._
import java.util.zip.{GZIPOutputStream, GZIPInputStream}
import org.apache.commons.io.{ FileUtils => ApacheFileUtils }
import scala.io.Codec

/** Utility functions for managing files and resources in different formats i.e.: text, binary, gzip, etc.
 *  All the I/O operations are encoded in UTF-8
 *  
  * @author Esteban Donato 
  */
object FileUtils {
  
  /**
   * implicit codec for all the I/O operations
   */
  implicit val codec: Codec = Codec.UTF8

  trait StreamConverter {
    def buildStream(f: File): OutputStream
  }


  object IdentityStreamConverter extends StreamConverter {
    def buildStream(f: File): OutputStream = ApacheFileUtils.openOutputStream(f)
  }

  object GzStreamConverter extends StreamConverter {
    def buildStream(f: File): OutputStream = new GZIPOutputStream(ApacheFileUtils.openOutputStream(f))
  }

  def write(filePath: String, content: String): Unit = printToFile(new File(filePath), content)
  def printToFile(f: String, c: String): Unit = printToFile(new File(f), c)
  
  /** saves content to a text file
    * @param f: file to save the content
    * @param c: content to be saved
    */
  def printToFile(f: File, c: String): Unit = getFiltePrinter(f) { p=> p.println(c) }
  def printToFile(f: String, c: Iterable[String]): Unit = printToFile(new File(f), c)
  
  /** saves content to a text file
    * @param f: file to save the content
    * @param c: content to be saved. Each element of the Iterable represents a new line in the file
    */
  def printToFile(f: File, c: Iterable[String]): Unit = getFiltePrinter(f) { p=> c.foreach(p.println(_)) }

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
  
  /**
    * reads content from a text file
    * @param f file path
    * @return file content as String 
    */
  def fileContent(f: String) = fromFile(f).mkString
  
  /**
    * reads content from a text file
    * @param f file path
    * @return file content as Iterator[String]. Each element of the iterator represents one line in the file
    */
  def fileLines(f: String) = fromFile(f).getLines()
  
  /**
    * reads content from a gzipped file
    * @param f file path
    * @return file content as String 
    */
  def gzippedFileContent(f: String) =
    fromInputStream(new GZIPInputStream(new BufferedInputStream(new FileInputStream(f)))).mkString
  
  /**
    * read content from a gzipped file
    * @param f file path
    * @return file content as Iterator[String]. Each element of the iterator represents one line in the file
    */
  def gzippedFileLines(f: String) =
    fromInputStream(new GZIPInputStream(new BufferedInputStream(new FileInputStream(f)))).getLines()
  
  /** reads content from a resource
    * @param f resource path
    * @return resource content as String 
    */
  def resourceContent(r: String) = {
   val url = getClass.getResource(r)
   if(url != null) fromURL(url).mkString else "" 
  }
}
