/**
 * Created by gustavo on 26/03/15.
 */

import java.nio.file.{FileSystems, FileSystem, Files}

import com.grandata.commons.files.{GlobImpl, FileSystemComponent}
import org.specs2.mutable._
import org.specs2.specification.{BeforeAll, BeforeAfterAll}

import com.google.common.jimfs.{PathType, Configuration, Jimfs}
import com.google.common.jimfs.Feature._

class Glob(fs: FileSystem) extends GlobImpl with FileSystemComponent {
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

  Files.newDirectoryStream(fs.getPath("/")).iterator.toIterator.foreach(println)

  def beforeAll: Unit = {
    Files.createDirectory(fs.getPath("/one"))
    Files.createFile(fs.getPath("/one/one"))
    Files.createFile(fs.getPath("/one/two"))
    Files.createFile(fs.getPath("/one/three.gz"))
    Files.createDirectory(fs.getPath("/one/dir_one"))
    Files.createFile(fs.getPath("/one/dir_one/one.gz"))
  }

  val glob = new Glob(fs)

  "Glob" should {

    "show foo in /*" in {

      glob.glob("/*").toList mustEqual List("/one")

    }
    "show foo in /*/*" in {

      glob.glob("/*/*").toSet mustEqual Set("/one/one", "/one/two", "/one/three.gz", "/one/dir_one")

    }
    "show foo in /*/*.gz" in {

      glob.glob("/*/*.gz").toSet mustEqual Set("/one/three.gz")

    }
    "show foo in /*/*/*" in {

      glob.glob("/*/*/*").toSet mustEqual Set("/one/dir_one/one.gz")

    }
    "show foo in /*/dir_one/*.gz" in {

      glob.glob("/*/dir_one/*.gz").toSet mustEqual Set("/one/dir_one/one.gz")

    }

  }


}

