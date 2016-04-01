package com.grandata.commons

import java.text.SimpleDateFormat
import java.time.{LocalDate, LocalDateTime}
import java.time.format.DateTimeFormatter
import java.util.Date

import org.joda.time.format.DateTimeFormat

import scala.concurrent.duration.Duration
import scala.util.Try

/**
  * Created by marcos on 01/04/16.
  */
package object dates {
  implicit class RichDate[T](str: String) {
    def toDate(format: String): Date = new SimpleDateFormat(format).parse(str)

    def toDaysFromEpochOption(format: String): Option[Long] = try {
      val date = DateTimeFormat.forPattern(format).parseDateTime(str)
      Some(Duration(date.getMillis, "millis").toDays)
    } catch {
      case e: Throwable => None
    }

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
