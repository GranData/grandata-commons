package com.grandata.commons.files

import scala.io.Source._
import java.io._
import java.util.zip.{GZIPOutputStream, GZIPInputStream}
import org.apache.commons.io.{ FileUtils => ApacheFileUtils }
import scala.io.Codec
import java.nio.file.Files
import scala.util.Try
import scala.collection.JavaConversions._

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

  def printToFile(file: String, content: String): Unit = printToFile(new File(file), content)
  
  /** saves content to a text file
    * @param file: file to save the content
    * @param content: content to be saved
    */
  def printToFile(file: File, content: String): Unit = printToFile(file, List(content))
  def printToFile(file: String, content: Iterable[String]): Unit = printToFile(new File(file), content)
  
  /** saves content to a text file
    * @param file: file to save the content
    * @param content: content to be saved. Each element of the Iterable represents a new line in the file
    */
  def printToFile(file: File, content: Iterable[String]): Unit = 
    ApacheFileUtils.writeLines(file, codec.name, content)
  
  /**
    * reads content from a text file
    * @param file file path
    * @return file content as String 
    */
  def fileContent(file: String) = Try(fromFile(file).mkString).getOrElse("").trim
  
  /**
    * reads content from a text file
    * @param file file path
    * @return file content as Iterator[String]. Each element of the iterator represents one line in the file
    */
  def fileLines(file: String) = Try(fromFile(file).getLines()).getOrElse(Iterator.empty)
  
  /**
    * reads content from a gzipped file
    * @param file file path
    * @return file content as String 
    */
  def gzippedFileContent(file: String) =
    Try(fromInputStream(new GZIPInputStream(new BufferedInputStream(new FileInputStream(file)))).mkString).getOrElse("").trim
  
  /**
    * read content from a gzipped file
    * @param file file path
    * @return file content as Iterator[String]. Each element of the iterator represents one line in the file
    */
  def gzippedFileLines(file: String) =
    Try(fromInputStream(new GZIPInputStream(new BufferedInputStream(new FileInputStream(file)))).getLines()).getOrElse(Iterator.empty)
  
  /** reads content from a resource
    * @param resource resource path
    * @return resource content as String 
    */
  def resourceContent(resource: String) = {
   val url = getClass.getResource(resource)
   if(url != null) fromURL(url).mkString else ""
  }
}
