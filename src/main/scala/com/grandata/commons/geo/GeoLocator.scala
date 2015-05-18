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

/**
 * @author esteban
 *
 */
class GeoLocator(geoJsonPaths: List[String]) extends Serializable {
  
  @transient private var featuresInfo: List[(RTree, Array[Feature])] = _
  
  @throws(classOf[IOException])
  private def readObject(in: ObjectInputStream): Unit ={
    in.defaultReadObject()
    generateTrees
  }
  
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
  
  private def toRectangle(p: Point) = {
    new Rectangle(p.long.toFloat, p.lat.toFloat, p.long.toFloat, p.lat.toFloat)
  }
  
  def locate(point: Point): List[Option[Feature]] = {
    if (point.lat == 0 && point.long == 0) {
      List.fill(featuresInfo.size)(None)
    } else {
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
}
class Point(val lat: Double, val long: Double)