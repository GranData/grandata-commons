package com.grandata.commons.dates

import org.joda.time.LocalDate

/**
 * Created by gustavo on 16/04/15.
 */
class DateRange(val start: LocalDate, val end: LocalDate) {
  def dayIterator = DateUtils.dayIterator(start, end)
  def monthIterator = DateUtils.monthIterator(start, end)
}
