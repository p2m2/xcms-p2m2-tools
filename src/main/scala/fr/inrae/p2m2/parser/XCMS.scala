package fr.inrae.p2m2.parser

import fr.inrae.p2m2.format.XCMSFeaturesIon

case object XCMS {
  /**
   *
   * @param lines : XCMS lines to parse
   * @return
   */
  def parse(lines: Seq[String]): Seq[XCMSFeaturesIon] = {
    // buffer key to store properties of the current features during parsing
    val header : Seq[String]= lines.head.split("[;\t]")
    //println(header)
    lines.drop(1).zipWithIndex.flatMap {
      case (line,i) =>
        val values : Seq[String]= line.split("[;\t]")
        if (values.length > header.length) {
          System.err.println(s"bad line def at [$i] size:${values.length} != ${header.length} ")
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
