package fr.inrae.p2m2.visitor

import fr.inrae.p2m2.format.XCMSFeaturesIon
import fr.inrae.p2m2.parser.XCMS

case object CompareXCMSFeaturesIon {

  def compareWithMed(f1 : XCMSFeaturesIon, f2 : XCMSFeaturesIon, ppm_error : Double, rt_threshold : Double): Boolean =
    (Math.abs((f1.mzmed - f2.mzmed)/f2.mzmed)*Math.pow(10.0,6)<ppm_error) &&
      (Math.abs(f1.rtmed-f2.rtmed)<rt_threshold)


  def compareWithListXCMSFeaturesIon(f1 : XCMSFeaturesIon,
                                     listIons : Seq[XCMSFeaturesIon],
                                     ppm_error : Double,
                                     rt_threshold : Double) : Option[XCMSFeaturesIon] =
    listIons.find(ion => compareWithMed(f1, ion, ppm_error, rt_threshold))

  def getColumnCompare( reportXCMS1 : Seq[String], listReportXCMSCompare : Seq[(String,Seq[String])],
                        ppm_error : Double, rt_threshold : Double, sep : String = ";" ): Seq[String] = {

    val lIons1 = XCMS.parse(reportXCMS1)
    val listOfList : Seq[Seq[String]]= listReportXCMSCompare.map {
      case (_, reportXCMS2) =>
        val lIons2 = XCMS.parse(reportXCMS2)
        lIons1.map(
          ion => compareWithListXCMSFeaturesIon(ion, lIons2, ppm_error, rt_threshold).map(_.name).getOrElse("")
        )
    }

    val header : String = reportXCMS1.head+sep+listReportXCMSCompare.map(_._1).mkString(sep)

    Seq(header)++
      reportXCMS1.drop(1).zipWithIndex.map {
        case (line, i) => line + sep + listOfList.map(_(i)).mkString(sep)
      }
  }
}
