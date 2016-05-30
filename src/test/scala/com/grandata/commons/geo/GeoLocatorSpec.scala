package com.grandata.commons.geo

import org.specs2.mutable.Specification

import com.grandata.commons.files.FileUtils

class GeoLocatorSpec extends Specification {
  
  val statesContent = FileUtils.resourceContent("/states.geojson")
  val citiesContent = FileUtils.resourceContent("/cities.geojson")
  
  "GeoLocator" should {
    "return the geo located points" in {
      val result = new GeoLocator(Seq(statesContent, citiesContent)).generateTrees.locate(GeoPoint(20,-101))
      result must be size(2)
      result(0) must beSome
      result(0).get.get("id") === 4
      result(1) must beSome
      result(1).get.get("id") === 40
    }
  
    "return none for the points not located" in {
      val result = new GeoLocator(Seq(statesContent, citiesContent)).generateTrees.locate(GeoPoint(15.74,103.5))
      result must be size(2)
      result(0) must beSome
      result(0).get.get("id") === 5
      result(1) must beNone
    }
    
    "fix invalid geometries before building the R-tree" in {
      val result = new GeoLocator(Seq(statesContent, citiesContent)).generateTrees.locate(GeoPoint(26.1, -109.1))
      result must be size(2)
      result(1).get.get("id") === 60

    }
  }
}