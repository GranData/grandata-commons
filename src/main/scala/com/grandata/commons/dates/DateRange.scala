package com.grandata.commons.dates

import org.joda.time.{DateTime, LocalDate}

/**
 * Created by gustavo on 16/04/15.
 */

object DateRange {
  def plusMonths(start: LocalDate, months: Int) = new DateRange(start, start.plusMonths(months))
}

class DateRange(val start: LocalDate, val end: LocalDate) {
  def dayIterator = DateUtils.dayIterator(start, end)
  def monthIterator = DateUtils.monthIterator(start, end)
}
