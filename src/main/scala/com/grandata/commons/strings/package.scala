package com.grandata.commons

/**
 * Created by gustavo on 16/04/15.
 */

import java.text.SimpleDateFormat
import java.util.Date

import org.joda.time.format.DateTimeFormat

import scala.concurrent.duration.{Duration, SECONDS}
import scala.util.Try
import java.util.Locale
import java.text._
import java.time._
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoField

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

    def toDate(format: String): Date = new SimpleDateFormat(format).parse(str)

    def toDaysFromEpochOption(format: String): Option[Long] = try {
      val date = DateTimeFormat.forPattern(format).parseDateTime(str)
      Some(Duration(date.getMillis, "millis").toDays)
    } catch {
      case e: Throwable => None
    }

    def toDaysEpochOption(formatter: DateTimeFormatter): Option[Long] =
      Try({
        val formattedDate = formatter.parse(str)
        val seconds = formattedDate.getLong(ChronoField.INSTANT_SECONDS)
        Duration(seconds, SECONDS).toDays
      }).toOption

    def toYearMonthOption(format: String): Option[YearMonth] =
      toYearMonthOption(DateTimeFormatter.ofPattern(format))
    def toYearMonthOption(formatter: DateTimeFormatter): Option[YearMonth] =
      Try(YearMonth.parse(str, formatter)).toOption

    def toLocalDateOption(format: String): Option[LocalDate] = 
      toLocalDateOption(DateTimeFormatter.ofPattern(format))
    def toLocalDateOption(formatter: DateTimeFormatter): Option[LocalDate] = 
      Try(LocalDate.parse(str, formatter)).toOption
  
    def toLocalDateTimeOption(format: String): Option[LocalDateTime] = 
      toLocalDateTimeOption(DateTimeFormatter.ofPattern(format))
    def toLocalDateTimeOption(formatter: DateTimeFormatter): Option[LocalDateTime] = 
      Try(LocalDateTime.parse(str, formatter)).toOption

  }
}
