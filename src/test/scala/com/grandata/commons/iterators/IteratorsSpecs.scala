package com.grandata.commons.iterators

import org.specs2.mutable.Specification

class IteratorsSpecs extends Specification {

  "Iterators" should {

    "get on condition with max fallbacks" in {
      val data = Array("hello", "world", "how", "are", "you")
      data.iterator.getOnCondition(_.equals("hello"), 3) must beEqualTo(Option("hello")) // first try
      data.iterator.getOnCondition(_.equals("world"), 3) must beEqualTo(Option("world")) // first fallback
      data.iterator.getOnCondition(_.equals("how"), 3) must beEqualTo(Option("how")) // second fallback
      data.iterator.getOnCondition(_.equals("are"), 3) must beEqualTo(Option("are")) // third and last fallback
      data.iterator.getOnCondition(_.equals("you"), 3) must beEqualTo(None)
    }

  }
}
