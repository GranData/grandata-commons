package com.grandata.commons.dates

import org.joda.time.LocalDate

/**
 * Created by gustavo on 16/04/15.
 */
object DateUtils {
  def dayIterator(start: LocalDate, end: LocalDate) = Iterator.iterate(start)(_ plusDays 1) takeWhile (_ isBefore end)
}
