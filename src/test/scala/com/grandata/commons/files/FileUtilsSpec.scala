package com.grandata.commons.files

import org.specs2.mutable.Specification

class FileUtilsSpec extends Specification {
  
  "FileUtils" should {
    "return content from a resource file" in {
      FileUtils.resourceContent("/fileUtils.resource") === "resource content"
    }
    "return empty from an inexistent resource file" in {
      FileUtils.resourceContent("/notExist.resource") isEmpty
    }
  }
}