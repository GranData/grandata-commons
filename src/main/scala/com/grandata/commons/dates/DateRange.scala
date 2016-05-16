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
  def monthsCount = monthIterator.toList.size
  
  override def hashCode = 41 * (41 + start.hashCode) + end.hashCode
  
  override def equals(other: Any) = other match {
    case that: DateRange => this.start == that.start && this.end == that.end
    case _ => false
  }
}
