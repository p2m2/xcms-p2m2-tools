package fr.inrae.p2m2.visitor

import fr.inrae.p2m2.format.XCMSFeaturesIon
import fr.inrae.p2m2.parser.XCMS

case object CompareXCMSFeaturesIon {

  def compareWithMed(f1 : XCMSFeaturesIon, f2 : XCMSFeaturesIon, ppm_error : Double, rt_threshold : Double): Boolean =
    (Math.abs((f1.mzmed - f2.mzmed)/f2.mzmed)*Math.pow(10.0,6)<=ppm_error) &&
      (Math.abs(f1.rtmed-f2.rtmed)<=rt_threshold)


  def compareWithListXCMSFeaturesIon(f1 : Option[XCMSFeaturesIon],
                                     listIons : Seq[Option[XCMSFeaturesIon]],
                                     ppm_error : Double,
                                     rt_threshold : Double) : Option[XCMSFeaturesIon] = f1 match {
    case Some(f) => listIons.find(ion => ion match {
      case Some(f2) => compareWithMed(f, f2, ppm_error, rt_threshold)
      case _ => false
    }).flatten
    case _ => None
  }


  def getColumnCompare( reportXCMS1 : Seq[String], listReportXCMSCompare : Seq[(String,Seq[String])],
                        ppm_error : Double, rt_threshold : Double): Seq[String] = {

    val sep1 = XCMS.getSepFromHeader(reportXCMS1.headOption).getOrElse("\t")
    val lIons1 = XCMS.parse(reportXCMS1,sep1)
    val listOfList : Seq[Seq[String]]= listReportXCMSCompare.map {
      case (_, reportXCMS2) =>
        val lIons2 = XCMS.parse(reportXCMS2,XCMS.getSepFromHeader(reportXCMS2.headOption).getOrElse("\t"))
        lIons1.map(
          ion => compareWithListXCMSFeaturesIon(ion, lIons2, ppm_error, rt_threshold)
            .map(x => s"${x.name}(${x.featureIdx})").getOrElse("")
        )
    }

    val header : String = reportXCMS1.head+sep1+listReportXCMSCompare.map(_._1).mkString(sep1)

    Seq(header)++
      reportXCMS1.drop(1).zipWithIndex.map {
        case (line, i) => line + sep1 + listOfList.map(_(i)).mkString(sep1)
      }
  }
}
