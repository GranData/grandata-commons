package com.grandata.commons.files

import java.nio.file.{FileSystems, FileSystem}

/**
 * Created by gustavo on 26/03/15.
 */
class Glob extends GlobImpl with FileSystemComponent {
  def fileSystem: FileSystem = FileSystems.getDefault
}