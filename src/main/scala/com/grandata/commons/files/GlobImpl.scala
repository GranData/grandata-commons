package com.grandata.commons.files

import java.nio.file.{Files}

import java.nio.file.FileSystem

import scala.util.Try

import java.nio.file.Path
/**
 * Created by gustavo on 26/03/15.
 */

trait FileSystemComponent {
  def fileSystem: FileSystem
}

trait GlobImpl {
  this: FileSystemComponent =>

    import collection.JavaConversions._

    private lazy val magicRegex = """[*?\[\{]""".r
    private def withoutMagic(pattern: String): Boolean = magicRegex.findFirstIn(pattern).isEmpty

    private def processPath(magic: Array[String], foundPath: Path): Iterator[String] = {
      if (magic.size <= 1)
        Iterator(foundPath.toString)
      else if (Files.isDirectory(foundPath))
        iglob(foundPath.toString, magic.tail)
      else
        Iterator.empty

    }
    private def iglob(basedir: String, patterns: Array[String]): Iterator[String] = {

      if (patterns.isEmpty) Iterator.empty
      else {
        val (noMagic, magic) = patterns.span(withoutMagic)
        val newBase = fileSystem.getPath(basedir, noMagic.mkString("/"))

        magic.headOption match {
          case Some(magicHead) =>
            Try(Files.newDirectoryStream(newBase, magicHead))
              .map(stream =>
                try {
                  stream.map { foundPath =>
                    processPath(magic, foundPath)
                  }.fold(Iterator.empty)(_ ++ _)
                } finally {
                  stream.close()
                }
              ).getOrElse(Iterator.empty)
          case None =>
            Iterator(newBase.toString)
        }

      }
    }

    def glob(pattern: String): Iterator[String] = {
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
