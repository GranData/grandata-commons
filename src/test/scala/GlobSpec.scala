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
    Files.createDirectory(fs.getPath("/foo"))
  }

  val glob = new Glob(fs)

  "Glob" should {

    "show foo in /*" in {

      glob.glob("/*").toList mustEqual List("/foo")

    }

  }


}

