/**
 *
 */
package com.grandata.commons.geo

import java.io.{IOException, ObjectInputStream}
import com.grandata.commons.files.FileUtils._
import com.infomatiq.jsi.Rectangle
import com.infomatiq.jsi.rtree.RTree
import gnu.trove.TIntProcedure
import io.jeo.geojson.GeoJSONReader
import io.jeo.geom.Geom
import io.jeo.vector.Feature
import scala.collection.JavaConversions._

/** Provides geo-location functionality for several polygon lists. Each polygon list 
  * is provided in geojson format. It creates an spatial index for each geojson provided
  *
  * @example {{{
  *           //create a GeoLocator instance for a state and a city polygon list
  *           val locator = new GeoLocator(List("/geo/states.geojson", "/geo/cities.geojson"))
  *           //build the states and cities R-Trees
  *           locator.generateTrees
  *           //try to locate the point in both R-Trees
  *           val r = locator.locate(new Point(15.74,103.5))
  *           if(r(0).isDefined) {
  *             println("could locate point in state " + r(0).get.get("id"))
  *           }
  *           if(r(1).isDefined) {
  *             println("could locate point in city " + r(1).get.get("id"))
  *           }
  *          }}}
  *
  * @constructor creates a GeoLocator instance using a list of polygons in geojson format
  *
  * @see [[http://geojson.org/]] for more information on geojson format
  * @author Esteban Donato
  */
class GeoLocator(geoJsonContent: Seq[String], fixGeometries : Boolean = true) extends Serializable {
  
  /**
   * creates a GeoLocator instance using a list of geojson files
   */
  def this(geoJsonPaths: List[String]) = this(geoJsonPaths.map(fileContent).toSeq)

  @transient private var featuresInfo: List[(RTree, Array[Feature])] = _

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
          if (feature.geometry.contains(Geom.point(point.long, point.lat))) {
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

  private def toRectangle(p: GeoPoint) = {
    new Rectangle(p.long.toFloat, p.lat.toFloat, p.long.toFloat, p.lat.toFloat)
  }

  @throws(classOf[IOException])
  private def readObject(in: ObjectInputStream): Unit = {
    in.defaultReadObject()
    generateTrees
  }

  /**
   * generates an R-Tree for each geojson file provided to this GeoLocator
   */
  def generateTrees = {
    val featuresList = geoJsonContent.map { content =>
      val reader = new GeoJSONReader
      fixGeom(reader.features(content).toList.toArray)
    }.toList
    val featuresTrees = featuresList.map(generateTree)
    featuresInfo = featuresTrees.zip(featuresList)
    this
  }

  private def fixGeom(features: Array[Feature]) = 
    if(fixGeometries) features.map(f => if(f.geometry().isValid()) f else f.put(f.geometry().buffer(0.0))) else features

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
}

/**
 * Geo point defined as a latitude/longitude pair
 */
class GeoPoint(val lat: Double, val long: Double) extends Serializable{

  private def canEqual(other: Any): Boolean = other.isInstanceOf[GeoPoint]

  override def equals(other: Any): Boolean = other match {
    case that: GeoPoint =>
      (that canEqual this) &&
        lat == that.lat &&
        long == that.long
    case _ => false
  }

  override def hashCode(): Int = {
    val state = Seq(lat, long)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }


  override def toString = s"($lat, $long)"

  def toTuple = (this.lat, this.long)
}

object GeoPoint {
  def apply(lat: Double, long: Double) = new GeoPoint(lat, long)
  def apply(tuple: (Double, Double)) = new GeoPoint(tuple._1, tuple._2)
  def unapply(loc: GeoPoint) = Some(loc.lat, loc.long)
}