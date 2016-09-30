package com.grandata.commons.strings

import org.specs2.mutable.Specification
import org.specs2.specification.BeforeAll
import org.joda.time.DateTime
import scala.concurrent.duration.Duration
import java.util.GregorianCalendar
import java.util.Locale
import java.time._
import java.time.format.DateTimeFormatter

/**
 * Created by gustavo on 16/04/15.
 */
class StringsSpecs extends Specification {
  
  "Strings" should {

    import com.grandata.commons.strings._
    """split "\t\t\t"  """ in {
      "\t\t\t".splitFields('\t') mustEqual Vector("", "", "", "")
    }

    "Ocurrences is OK" in {
      "hola nada nada x y z hola manhola".occurrences("hola") must equalTo(3)
      "ola nada nada x y z ola".occurrences("hola") must equalTo(0)
    }
    
    "Convert a String to Int Option" in {
      "1345".toIntOption must beEqualTo(Some(1345))
      "ABC".toIntOption must beEqualTo(None)
      "134.5".toIntOption must beEqualTo(None)
      "134,5".toIntOption must beEqualTo(None)
    }

    "Convert a String to Double Option" in {
      "1345.48".toDoubleOption must beEqualTo(Some(1345.48D))
      "ABC".toDoubleOption must beEqualTo(None)
      "134,5".toDoubleOption must beEqualTo(None)
    }

    "Convert a String to Float Option" in {
      "134.5".toFloatOption must beEqualTo(Some(134.5F))
      "1345".toFloatOption must beEqualTo(Some(1345.0F))
      ".1345".toFloatOption must beEqualTo(Some(0.1345F))
      "ABC".toFloatOption must beEqualTo(None)
      "134,5".toFloatOption must beEqualTo(None)
    }
    
    "Convert a String in scientific notation to Float Option" in {
      "10.567E2".toFloatOption must beEqualTo(Some(1056.7F))
      "10.567E+2".toFloatOption must beEqualTo(Some(1056.7F))
      "10.567E002".toFloatOption must beEqualTo(Some(1056.7F))
      "10.567E+002".toFloatOption must beEqualTo(Some(1056.7F))
      
      "10.567E-2".toFloatOption must beEqualTo(Some(0.10567F))
      "10.567E-002".toFloatOption must beEqualTo(Some(0.10567F))
      
      ".567E2".toFloatOption must beEqualTo(Some(56.7F))
      
      "10,567E-002".toFloatOption must beEqualTo(None)
    }

    "Convert a String with comma as decimal separator to Float Option" in {
      "134,5".toFloatOption(Locale.GERMAN) must beEqualTo(Some(134.5F))
      ",1345".toFloatOption(Locale.GERMAN) must beEqualTo(Some(0.1345F))
      "134,5bla".toFloatOption(Locale.GERMAN) must beNone
      "134.5".toFloatOption(Locale.GERMAN) must beEqualTo(Some(1345F))
    }
    
    "Convert a String with comma as decimal separator and scientific notation to Float Option" in {
      "10,567E2".toFloatOption(Locale.GERMAN) must beEqualTo(Some(1056.7F))
      "10,567E+2".toFloatOption(Locale.GERMAN) must beEqualTo(Some(1056.7F))
      "10,567E002".toFloatOption(Locale.GERMAN) must beEqualTo(Some(1056.7F))
      "10,567E+002".toFloatOption(Locale.GERMAN) must beEqualTo(Some(1056.7F))
      
      "10,567E-2".toFloatOption(Locale.GERMAN) must beEqualTo(Some(0.10567F))
      "10,567E-002".toFloatOption(Locale.GERMAN) must beEqualTo(Some(0.10567F))
      
      ",567E2".toFloatOption(Locale.GERMAN) must beEqualTo(Some(56.7F))
      
      "10.567E-002".toFloatOption(Locale.GERMAN) must beEqualTo(Some(105.67F))
      "10,567E-002bla".toFloatOption(Locale.GERMAN) must beEqualTo(None)
    }
    
    "Convert an empty String to a Float Option" in {
      "".toFloatOption must beEqualTo(None)
      "".toFloatOption(Locale.GERMAN) must beEqualTo(None)
    }


    "Convert a String to Boolean Option" in {
      "true".toBooleanOption must beEqualTo(Some(true))
      "false".toBooleanOption must beEqualTo(Some(false))
      "1".toBooleanOption must beEqualTo(Some(true))
      "0".toBooleanOption must beEqualTo(Some(false))
      "ABC".toBooleanOption must beEqualTo(None)
      "134.5".toBooleanOption must beEqualTo(None)
      "134,5".toBooleanOption must beEqualTo(None)
    }

    "Convert a String to String Option" in {
      "true".toStringOption must beEqualTo(Some("true"))
      "1".toStringOption must beEqualTo(Some("1"))
      "".toStringOption must beEqualTo(None)
    }
    
    "Convert a String to Byte Option" in {
      "1".toByteOption must beEqualTo(Some(1.toByte))
      "-1".toByteOption must beEqualTo(Some(-1.toByte))
      "300".toByteOption must beEqualTo(None)
      "".toByteOption must beEqualTo(None)
    }

    "Convert a String to Date" in {
      val date = new GregorianCalendar(2015, 0, 24).getTime
      "24/01/2015".toDate("dd/MM/yyyy") must beEqualTo(date)
      "24/01/15".toDate("dd/MM/yy") must beEqualTo(date)
      "ABC".toDate("dd/MM/yy") must throwA[Exception].like { case _ => ok }
      "1345".toDate("dd/MM/yy") must throwA[Exception].like { case _ => ok }
    }

    "Convert a String to DaysFromEpochOption" in {
      val days = Some(Duration(new DateTime(2015, 1, 10, 0, 0).getMillis, "millis").toDays)
      "10/01/2015".toDaysFromEpochOption("dd/MM/yyyy") must beEqualTo(days)
      "10/01/15".toDaysFromEpochOption("dd/MM/yy") must beEqualTo(days)
      "2015-01-10T10:23:15-05:00".toDaysFromEpochOption("yyyy-MM-dd'T'HH:mm:ssZ") must beEqualTo(days)
      "2015-01-09T20:23:15-05:00".toDaysFromEpochOption("yyyy-MM-dd'T'HH:mm:ssZ") must beEqualTo(days)
      "ABC".toDaysFromEpochOption("dd/MM/yyyy") must beEqualTo(None)
      "1345".toDaysFromEpochOption("dd/MM/yyyy") must beEqualTo(None)
    }

    "convert a String to year month" in {
      val yearMonthA = Option(YearMonth.of(2016, 8))
      val yearMonthB = Option(YearMonth.of(2016, 10))
      "201608".toYearMonthOption("yyyyMM") must beEqualTo(yearMonthA)
      "201610".toYearMonthOption(DateTimeFormatter.ofPattern("yyyyMM")) must beEqualTo(yearMonthB)
      "ABC".toYearMonthOption(DateTimeFormatter.ofPattern("yyyyMM")) must beEqualTo(None)
      "1345".toYearMonthOption(DateTimeFormatter.ofPattern("yyyyMM")) must beEqualTo(None)
    }
    
    "Split line with custom char" in {
      "1,,,34,,,".splitFields(',') must beEqualTo(Vector("1", "", "", "34", "", "", ""))
      "1|||34|||".splitFields('|') must beEqualTo(Vector("1", "", "", "34", "", "", ""))
    }
    
    "Convert a String to a LocalDate" in {
      "01/12/2015".toLocalDateOption("dd/MM/yyyy") === Some(LocalDate.of(2015, Month.DECEMBER, 1))
      "01/12/2015".toLocalDateOption(DateTimeFormatter.ofPattern("dd/MM/yyyy")) === Some(LocalDate.of(2015, Month.DECEMBER, 1))
      "01/12/2015".toLocalDateOption("dd-MM-yyyy") must beNone
    }
    
    "Convert a String to a LocalDateTime" in {
      "01/12/2015 10:09".toLocalDateTimeOption("dd/MM/yyyy HH:mm") === Some(LocalDateTime.of(2015, Month.DECEMBER, 1, 10, 9))
      "01/12/2015 10:09".toLocalDateTimeOption(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) === Some(LocalDateTime.of(2015, Month.DECEMBER, 1, 10, 9))
      "01/12/2015".toLocalDateTimeOption("dd/MM/yyyy HH:mm") must beNone
    }
  }
}
