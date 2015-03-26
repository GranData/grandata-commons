package com.grandata.commons.files

import java.nio.file.{Files}

import java.nio.file.FileSystem

import scala.util.Try

/**
 * Created by gustavo on 26/03/15.
 */

trait FileSystemComponent {
  def fileSystem: FileSystem
}

trait GlobImpl {
  this: FileSystemComponent =>

    import collection.JavaConversions._

    private lazy val magicRegex = """[*?\[]""".r
    private def withoutMagic(pattern: String): Boolean = magicRegex.findFirstIn(pattern).isEmpty

    private def iglob(basedir: String, patterns: Array[String]): Iterator[String] = {

      if (patterns.isEmpty) Iterator.empty
      else {
        val (noMagic, magic) = patterns.span(withoutMagic)
        val newBase = fileSystem.getPath(basedir, noMagic.mkString("/"))

//        println(s"basedir: $basedir, newBase: $newBase, magic: ${magic.mkString(" / ")}")

        Try(Files.newDirectoryStream(newBase, magic.head))
          .map(stream =>
          try {
            stream.map { foundPath =>
              if (magic.tail.isEmpty)
                Iterator(foundPath.toString)
              else if (Files.isDirectory(foundPath))
                iglob(foundPath.toString, magic.tail)
              else
                Iterator.empty
            }.fold(Iterator.empty)(_ ++ _)
          } finally {
            stream.close()
          }
          ).getOrElse(Iterator.empty)
      }
    }

    def glob(pattern: String): Iterator[String] = {
      //    println(s"Exists: ${Files.exists(FileSystems.getDefault.getPath(pattern))} // ${withoutMagic(pattern)}")
      if (withoutMagic(pattern)) {
        if (Files.exists(fileSystem.getPath(pattern)))
          Iterator(pattern)
        else
          Iterator.empty
      } else {
        iglob(if (pattern.startsWith("/")) "/" else "", pattern.split("/"))
      }
    }
}
