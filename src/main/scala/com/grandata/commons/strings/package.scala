package com.grandata.commons

/**
 * Created by gustavo on 16/04/15.
 */

import java.text._
import java.util.Locale

import scala.util.Try

package object strings {

  implicit class RichString[T](str: String) {
    def splitFields(char: Char) = str.split(s"\\$char", -1).toVector

    def occurrences(substr: String) = substr.r.findAllMatchIn(str).length

    def toDoubleOption: Option[Double] = Try(str.toDouble).toOption

    def toFloatOption: Option[Float] = Try(str.toFloat).toOption

    def toFloatOption(locale: Locale): Option[Float] = {
      if(str.isEmpty) {
        None
      } else {
      val pos = new ParsePosition(0)
      val txt = str.replaceFirst("E\\+", "E")
      val number = NumberFormat.getInstance(locale).parse(txt, pos)
      if(pos.getIndex() == txt.length) Some(number.floatValue) else None  
      }
    }

    def toByteOption: Option[Byte] = Try(str.toByte).toOption

    def toBooleanOption: Option[Boolean] = Try(str.toBoolean).toOption match {
      case None => str.toIntOption match {
        case Some(0) => Some(false)
        case Some(1) => Some(true)
        case _ => None
      }
      case v => v
    }

    def toIntOption: Option[Int] = Try(str.toInt).toOption

    def toStringOption: Option[String] = Try(str.toString).toOption match {
      case Some(s: String) => if (s.nonEmpty) Some(s) else None
      case _ => None
    }
  }
}
