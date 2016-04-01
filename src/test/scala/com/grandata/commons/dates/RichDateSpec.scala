package com.grandata.commons.dates

/**
  * Created by marcos on 01/04/16.
  */
class RichDateSpec extends org.specs2.mutable.Specification {

  "RichDate" >> {

    "convert a date using format iso8601" >> {
      import com.grandata.commons.dates.RichDate
      val format = "yyyy-MM-dd'T'HH:mm:ssZ"
      "2010-02-25T00:00:00-05:00".toDaysFromEpochOption(format) must beSome(14665)
    }

    "convert a date using format dd/MM/yyyy" >> {
      import com.grandata.commons.dates.RichDate
      val format = "dd/MM/yyyy"
      "25/02/2010".toDaysFromEpochOption(format) must beSome(14665)
    }

    "convert a date using format yyyyMMdd" >> {
      import com.grandata.commons.dates.RichDate
      val format = "yyyyMMdd"
      "20100225".toDaysFromEpochOption(format) must beSome(14665)
    }

    "convert a date using format yyyyMM" >> {
      import com.grandata.commons.dates.RichDate
      val format = "yyyyMM"
      "201002".toDaysFromEpochOption(format) must beSome(14641)
    }

    "convert a date using format yyyy-MM-dd HH:mm:ss.SSS" >> {
      import com.grandata.commons.dates.RichDate
      val format = "yyyy-MM-dd HH:mm:ss.SSS"
      "2015-09-17 10:17:03.736".toDaysFromEpochOption(format) must beSome(16695)
    }
  }
}
