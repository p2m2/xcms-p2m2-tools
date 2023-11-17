package fr.inrae.p2m2.format

import scala.util.Try

case class XCMSFeaturesIon(
                            featureIdx : Int,
                            mzmed : Double,
                            rtmed : Double,
                            name : String
                          )

case object XCMSFeaturesIon {
  def apply(m: Map[String,String]) = {
    new XCMSFeaturesIon(
      Try(m("featureidx").toInt).toOption.getOrElse(-1),
      Try(m("mzmed").toDouble).toOption.getOrElse(-1.0),
      Try(m("rtmed").toDouble).toOption.getOrElse(-1.0),
      m.getOrElse("name","<no_name>")
    )
  }
}
