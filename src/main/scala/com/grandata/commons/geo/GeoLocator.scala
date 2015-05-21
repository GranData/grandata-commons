/**
 *
 */
package com.grandata.commons.geo

import com.infomatiq.jsi.rtree.RTree
import java.io.ObjectInputStream
import java.io.IOException
import io.jeo.geojson.GeoJSONReader
import com.infomatiq.jsi.Rectangle
import gnu.trove.TIntProcedure
import io.jeo.vector.Feature
import scala.collection.JavaConversions._
import io.jeo.geom.Geom
import com.grandata.commons.files.FileUtils._
import io.jeo.vector.Schema

/** Provides geo-location functionality for several polygon lists. Each polygon list 
  * is loaded from a geojson file. It creates an spatial index for each geojson file with all the polygons
  * stored in that file
  * 
  * @example {{{
  * //create a GeoLocator instance for a state and a city polygon list
  * val locator = new GeoLocator(List("/geo/states.geojson", "/geo/cities.geojson"))
  * //build the states and cities R-Trees
  * locator.generateTrees
  * //try to locate the point in both R-Trees
  * val r = locator.locate(new Point(15.74,103.5)) 
  * if(r(0).isDefined) {
  *   println("could locate point in state " + r(0).get.get("id"))
  * }                                            
  * if(r(1).isDefined) {
  *   println("could locate point in city " + r(1).get.get("id"))
  * }
  * }}}
  * 
  * @constructor creates a GeoLocator instance using a list of geojson files
  *  
  * @see [[http://geojson.org/]] for more information on geojson format
  * @author Esteban Donato
  */
class GeoLocator(geoJsonPaths: List[String]) extends Serializable {
  
  @transient private var featuresInfo: List[(RTree, Array[Feature])] = _
  
  @throws(classOf[IOException])
  private def readObject(in: ObjectInputStream): Unit ={
    in.defaultReadObject()
    generateTrees
  }
  
  /**
   * generates an R-Tree for each geojson file provided to this GeoLocator
   */
  def generateTrees = {
    val jsonFiles = geoJsonPaths
    val featuresList = jsonFiles.map { jsonPath =>
      val reader = new GeoJSONReader
      reader.features(fileContent(jsonPath)).toList.toArray
    }
    val featuresTrees = featuresList.map(generateTree)
    featuresInfo = featuresTrees.zip(featuresList)
  }
  
  private def generateTree(features: Array[Feature]): RTree = {
    val tree = new RTree
    tree.init(null)
    features.zipWithIndex.foreach { case (feature, idx) =>
      val bounds = feature.geometry().getEnvelopeInternal
      val rect = new Rectangle(
        bounds.getMinX.toFloat, 
        bounds.getMinY.toFloat, 
        bounds.getMaxX.toFloat, 
        bounds.getMaxY.toFloat)
      tree.add(rect, idx)
    }
    tree
  }
  
  private def toRectangle(p: GeoPoint) = {
    new Rectangle(p.long.toFloat, p.lat.toFloat, p.long.toFloat, p.lat.toFloat)
  }
  
  /** tries to locate the given point into several polygons lists
    * 
    * @param point the point to be located
    * @return for each polygon list an Option[Feature] indicating whether the point could be located into some polygon
    */
  def locate(point: GeoPoint): List[Option[Feature]] = {
    featuresInfo.map { case (tree, features) =>
      var result: Option[Feature] = None // This is really ugly.
      tree.intersects(toRectangle(point), new TIntProcedure() {
        def execute(i: Int) = {
          val feature = features(i)
          //TODO geometry.buffer(0.0) is a workaround to avoid a "Self-intersection at or near point" exception. 
          //The exception is avoided now but performance got worse
          if (feature.geometry.buffer(0.0).contains(Geom.point(point.long, point.lat))) {
            result = Some(feature)
            false
          } else {
            true
          }
        }
      })
      result
    }
  }
}
/**
  * Geo point defined as a latitude/longitude pair
  */
class GeoPoint(val lat: Double, val long: Double)