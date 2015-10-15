package com.grandata.commons.dates

import org.specs2.mutable.Specification
import org.joda.time.LocalDate

class DateRangeSpec extends Specification {
  
  "DateRange" should {
    "create a day iterator" in {
      val iter = new DateRange(new LocalDate(2015, 10, 30), new LocalDate(2015, 11, 1)).dayIterator
      iter.hasNext must beTrue
      iter.next() === new LocalDate(2015, 10, 30)
      iter.hasNext must beTrue
      iter.next() === new LocalDate(2015, 10, 31)
      iter.hasNext must beTrue
      iter.next() === new LocalDate(2015, 11, 1)
      iter.hasNext must beFalse
    }
    
    "create an empty day iterator from an invalid date range" in {
      val iter = new DateRange(new LocalDate(2015, 11, 20), new LocalDate(2015, 11, 1)).dayIterator
      iter.isEmpty must beTrue
    }
    
    "create a month iterator" in {
      val iter = new DateRange(new LocalDate(2015, 10, 1), new LocalDate(2015, 11, 1)).monthIterator
      iter.hasNext must beTrue
      iter.next() === new LocalDate(2015, 10, 1)
      iter.hasNext must beTrue
      iter.next() === new LocalDate(2015, 11, 1)
      iter.hasNext must beFalse
    }
    
    "create an empty month iterator from an invalid date range" in {
      val iter = new DateRange(new LocalDate(2015, 12, 1), new LocalDate(2015, 11, 1)).monthIterator
      iter.isEmpty must beTrue
    }
    
    "be equal to another DateRange instance with the same start and end dates" in {
      val range1 = new DateRange(new LocalDate(2015, 10, 1), new LocalDate(2015, 11, 1))
      val range2 = new DateRange(new LocalDate(2015, 10, 1), new LocalDate(2015, 11, 1))
      range1 === range2
    }
    
    "not be equal to another DateRange instance with different start or end dates" in {
      val range1 = new DateRange(new LocalDate(2015, 10, 1), new LocalDate(2015, 11, 1))
      val range2 = new DateRange(new LocalDate(2015, 10, 1), new LocalDate(2015, 11, 2))
      range1 == range2 must beFalse
    }
  }

}