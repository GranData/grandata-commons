package com.grandata.commons

/**
 * Created by gustavo on 16/04/15.
 */
package object strings {
  implicit class RichSplitableString[T](s: String) {
    def splitFields(char: Char) = s.split(s"\\$char", -1)
  }
  
  def zerosWhenEmpty(columns: List[String])={
    columns.map(zeroIfEmpty(_))
  }

  def zeroIfEmpty(value:String)={
    if (!value.trim.isEmpty) value else 0.toString
  }

}
