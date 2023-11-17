package fr.inrae.p2m2.parser

import fr.inrae.p2m2.format.XCMSFeaturesIon

case object XCMS {

  def getSepFromHeader(header : Option[String]) : Option[String] = header match {
    case Some(line) if line.nonEmpty =>
      Some(Seq(("\t", line.split("\t")), (";", line.split(";"))).maxBy(_._2.length)._1)
    case _ => None
  }
  /**
   *
   * @param lines : XCMS lines to parse
   * @return
   */
  def parse(lines: Seq[String],sep : String): Seq[Option[XCMSFeaturesIon]] = {
    // buffer key to store properties of the current features during parsing
    val header : Seq[String]= lines.head.split(sep)
    //println(header)
    lines.drop(1).zipWithIndex.map {
      case (line,i) =>
        val values : Seq[String]= line.split(sep)
        if (values.length > header.length) {
          System.err.println(s"bad line def at [$i] size:${values.length} != ${header.length} ")
          System.err.println(s"${values.mkString(",")}")
          None
        } else {
          val m : Map[String,String]= header.zipWithIndex.map {
            case (field, idx) => field -> values.lift(idx).getOrElse("")
          }.toMap
          Some(XCMSFeaturesIon(m))
        }
    }
  }
}
