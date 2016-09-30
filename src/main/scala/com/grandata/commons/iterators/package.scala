package com.grandata.commons

package object iterators {

  implicit class RichIterator[T](iterator: Iterator[T]) {

    /**
      * Gets the first element which satisfies the specified condition within
      * the [0;maxFallbacks) range.
      *
      * @param condition the matching condition.
      * @param maxFallbacks maximum fallbacks performed when condition fails.
      * @return an Option for the element
      */
    def getOnCondition(condition: T => Boolean, maxFallbacks: Int): Option[T] = iterator.getOnCondition(condition, maxFallbacks, 0)

    private def getOnCondition(condition: T => Boolean, maxFallbacks: Int, currentIndex: Int): Option[T] = {
      if (!(iterator.hasNext) || currentIndex > maxFallbacks) {
        return Option.empty
      }
      val current = iterator.next
      if (!(condition(current))) {
        return iterator.getOnCondition(condition, maxFallbacks, currentIndex + 1)
      }
      Option(current)
    }

  }

}