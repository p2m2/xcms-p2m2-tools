package fr.inrae.p2m2.format

import scala.util.Try

case class XCMSFeaturesIon(
                            featureidx : Int,
                            mzmed : Double  ,
                            mzmin : Double  ,
                            mzmax : Double  ,
                            rtmed : Double  ,
                            rtmin  :Double  ,
                            rtmax : Double  ,
                            name : String
                          )

case object XCMSFeaturesIon {
  def apply(m: Map[String,String]) = {
    new XCMSFeaturesIon(
      Try(m("featureidx").toInt).toOption.getOrElse(-1),
      Try(m("mzmed").toDouble).toOption.getOrElse(-1.0),
      Try(m("mzmin").toDouble).toOption.getOrElse(-1.0),
      Try(m("mzmax").toDouble).toOption.getOrElse(-1.0),
      Try(m("rtmed").toDouble).toOption.getOrElse(-1.0),
      Try(m("rtmin").toDouble).toOption.getOrElse(-1.0),
      Try(m("rtmax").toDouble).toOption.getOrElse(-1.0),
      m.getOrElse("name","<empty>")
    )
  }
}
