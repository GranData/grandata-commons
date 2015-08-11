/**
 * Created by gustavo on 26/03/15.
 */
package com.grandata.commons.files

import java.nio.file.{FileSystems, FileSystem, Files}
import org.specs2.mutable._
import org.specs2.specification.{BeforeAll, BeforeAfterAll}

import com.google.common.jimfs.{PathType, Configuration, Jimfs}
import com.google.common.jimfs.Feature._

class GlobTest(fs: FileSystem) extends GlobImpl with FileSystemComponent {
  def fileSystem: FileSystem = fs
}
class GlobSpec extends Specification with BeforeAll {
  import collection.JavaConversions._

  val fs = Jimfs.newFileSystem(Configuration.builder(
    PathType.unix())
      .setRoots("/")
      .setWorkingDirectory("/")
      .setAttributeViews("basic")
      .setSupportedFeatures(LINKS, SYMBOLIC_LINKS, SECURE_DIRECTORY_STREAM, FILE_CHANNEL)
      .build())

//  Files.newDirectoryStream(fs.getPath("/")).iterator.toIterator.foreach(println)

  def beforeAll: Unit = {
    Files.createDirectory(fs.getPath("/one"))
    Files.createDirectory(fs.getPath("/two"))
    Files.createDirectory(fs.getPath("/one/dir_one"))
    Files.createDirectory(fs.getPath("/one/dir_two"))

    Files.createFile(fs.getPath("/one/one"))
    Files.createFile(fs.getPath("/one/two"))
    Files.createFile(fs.getPath("/one/three.gz"))
    Files.createFile(fs.getPath("/one/dir_one/one.gz"))
    Files.createFile(fs.getPath("/one/dir_one/one1.gz"))
    Files.createFile(fs.getPath("/one/dir_two/one2.gz"))
  }

  val glob = new GlobTest(fs)

  "Glob" should {

    "show /*" in {
      glob.glob("/*").toList mustEqual List("/one", "/two")
    }

    "show /*/*" in {
      glob.glob("/*/*").toSet mustEqual Set("/one/one", "/one/two", "/one/three.gz", "/one/dir_one", "/one/dir_two")
    }

    "show /*/*.gz" in {
      glob.glob("/*/*.gz").toSet mustEqual Set("/one/three.gz")
    }

    "show /*/dir_{one,two}/*" in {
      glob.glob("/*/dir_{one,two}/*").toSet mustEqual Set("/one/dir_one/one.gz", "/one/dir_one/one1.gz", "/one/dir_two/one2.gz")
    }

    "show /*/*/*" in {
      glob.glob("/*/*/*").toSet mustEqual Set("/one/dir_one/one.gz", "/one/dir_one/one1.gz", "/one/dir_two/one2.gz")
    }

    "show /*/dir_one/*.gz" in {
      glob.glob("/*/dir_one/*.gz").toSet mustEqual Set("/one/dir_one/one.gz", "/one/dir_one/one1.gz")
    }

    "show /*/dir_*/one[1-9].gz" in {
      glob.glob("/*/dir_*/one[1-9].gz").toSet mustEqual Set("/one/dir_two/one2.gz", "/one/dir_one/one1.gz")
    }

    "show /*/dir_*/one1.gz" in {
      glob.glob("/*/dir_*/one1.gz").toSet mustEqual Set("/one/dir_one/one1.gz", "/one/dir_two/one1.gz")
    }

  }


}

