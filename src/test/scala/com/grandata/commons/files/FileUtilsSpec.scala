package com.grandata.commons.files

import org.specs2.mutable.Specification
import java.nio.file.Paths
import java.nio.file.Files
import org.apache.commons.io.{ FileUtils => ApacheFileUtils }
import java.util.zip.GZIPOutputStream
import java.nio.file.Path

class FileUtilsSpec extends Specification {
  
  val tmpDir = sys.props("java.io.tmpdir")
  
  "FileUtils" should {
    "return content from a resource file" in {
      FileUtils.resourceContent("/fileUtils.resource") === "resource content"
    }
    
    "return empty from an inexistent resource file" in {
      FileUtils.resourceContent("/notExist.resource") isEmpty
    }
    
    "print content to a text file" in {
      val path = Paths.get(tmpDir, "FileUtilsSpec")
      try {
        FileUtils.printToFile(path.toString, "test content")
        FileUtils.fileContent(path.toString) === "test content"  
      } finally {
        Files.deleteIfExists(path)  
      }
    }
    
    "print list of lines to a text file" in {
      val path = Paths.get(tmpDir, "FileUtilsSpec1")
      try {
        FileUtils.printToFile(path.toString, List("line1", "line2"))
        FileUtils.fileContent(path.toString) === "line1\nline2"  
      } finally {
        Files.deleteIfExists(path)  
      }
    }
    
    "print content to a text file in an inexistent sub-directory" in {
      val path = Paths.get(tmpDir,"newdir", "FileUtilsSpec2")
      try {
        FileUtils.printToFile(path.toString, "new content")
        FileUtils.fileContent(path.toString) === "new content"  
      } finally {
        ApacheFileUtils.deleteDirectory(Paths.get(tmpDir,"newdir").toFile)  
      }
    }
    
    "return content from a text file as list of lines" in {
      val path = Paths.get(tmpDir, "FileUtilsSpec3")
      try {
        FileUtils.printToFile(path.toString, List("line1", "line2"))
        FileUtils.fileLines(path.toString) must contain(exactly("line1", "line2")).inOrder
      } finally {
        Files.deleteIfExists(path)
      }
    }
    
    "return empty from an inexistent file" in {
      FileUtils.fileContent("/notExist.txt") isEmpty
    }
    
     "return empty list from an inexistent file" in {
      FileUtils.fileLines("/notExist.txt") isEmpty
    }
     
    "return content from a gzipped text file" in {
      val path = Paths.get(tmpDir, "FileUtilsSpec4")
      generateZipFile(path, "zipped content")
      try {
        FileUtils.gzippedFileContent(path.toString) === "zipped content"
      } finally {
        Files.deleteIfExists(path)
      }
    }
    
    "return content from a gzipped text file as list of lines" in {
      val path = Paths.get(tmpDir, "FileUtilsSpec5")
      generateZipFile(path, "zipped line1\nzipped line2")
      try {
        FileUtils.gzippedFileLines(path.toString) must contain(exactly("zipped line1", "zipped line2")).inOrder
      } finally {
        Files.deleteIfExists(path)
      }
    }
    
    "return empty from an invalid gzipped file" in {
      val path = Paths.get(tmpDir, "FileUtilsSpec6")
      FileUtils.printToFile(path.toString, List("line1", "line2"))
      try {
        FileUtils.gzippedFileContent(path.toString) isEmpty
      } finally {
        Files.deleteIfExists(path)
      }
    }
    
    "return empty list from an invalid gzipped file" in {
      val path = Paths.get(tmpDir, "FileUtilsSpec7")
      FileUtils.printToFile(path.toString, List("line1", "line2"))
      try {
        FileUtils.gzippedFileLines(path.toString) isEmpty
      } finally {
        Files.deleteIfExists(path)
      }
    }
  }
  
  def generateZipFile(p: Path, c: String) {
    val gzip = new GZIPOutputStream(Files.newOutputStream(p))
    gzip.write(c.getBytes)
    gzip.finish()
    gzip.close()
  }
  
}