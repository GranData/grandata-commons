package com.grandata.commons

/**
 * Created by gustavo on 16/04/15.
 */
package object strings {
  implicit class RichSplitableString[T](s: String) {
    def splitFields(char: Char) = s.split(s"\\$char", -1)
  }

  implicit class RichString[T](str: String) {
    def occurrences(substr: String) = substr.r.findAllMatchIn(str).length
  }
}
