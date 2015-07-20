package com.grandata.commons.geo

import org.specs2.mutable.Specification
import org.specs2.specification.BeforeAfterAll
import com.grandata.commons.files.FileUtils
import java.io.File

class GeoLocatorSpec extends Specification with BeforeAfterAll {
  
  var locator: GeoLocator = _
  var sFile: File = _
  var cFile: File = _
  
  "GeoLocator" should {
    "return the geo located points" in {
      val result = locator.locate(GeoPoint(20,-101))
      result must be size(2)
      result(0) must beSome
      result(0).get.get("id") === 4
      result(1) must beSome
      result(1).get.get("id") === 40
    }
  
    "return none for the points not located" in {
      val result = locator.locate(GeoPoint(15.74,103.5))
      result must be size(2)
      result(0) must beSome
      result(0).get.get("id") === 5
      result(1) must beNone
    }
  }
  
  override def afterAll() {
    sFile.delete()
    cFile.delete()
  }
  
  override def beforeAll() {
    sFile = File.createTempFile("states", ".geojson")
    cFile = File.createTempFile("cities", ".geojson")
    FileUtils.printToFile(sFile, FileUtils.resourceContent("/states.geojson"))
    FileUtils.printToFile(cFile, FileUtils.resourceContent("/cities.geojson"))
    
    locator = new GeoLocator(List(sFile.getAbsolutePath, cFile.getAbsolutePath))
    locator.generateTrees
  }

}