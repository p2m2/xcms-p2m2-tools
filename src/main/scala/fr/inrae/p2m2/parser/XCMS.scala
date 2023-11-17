package fr.inrae.p2m2.parser

import fr.inrae.p2m2.format.XCMSFeaturesIon
import kantan.csv.CsvConfiguration.{Header, QuotePolicy}
import kantan.csv._
import kantan.csv.ops._
import kantan.csv.generic._

import scala.util.{Success, Try}

case object XCMS {
  def getSepAndHeader(header: Option[String]): Option[(String,Seq[String])] = header match {
    case Some(line) if line.nonEmpty =>
      val sep : String = Seq(("\t", line.split("\t")), (";", line.split(";"))).maxBy(_._2.length)._1
      Some(sep,header.get.split(sep))
    case _ => None
  }


  /**
   *
   * @param contentFile : XCMS lines to parse
   * @return
   */
  def parse(contentFile: String): Seq[Option[XCMSFeaturesIon]] = {

    getSepAndHeader(contentFile.split("\n").headOption) match {
      case Some((sep : String, header : Seq[String])) => contentFile.readCsv[List, List[String]](rfc.withHeader.withCellSeparator(sep.charAt(0))).collect {
        case Right(fields) =>
          Some(XCMSFeaturesIon(
              fields.zipWithIndex.flatMap {
                case (f, i) if i<header.length => Some(header(i) -> f.trim)
                case (f, i) =>
                 // System.err.println(s"fields :${fields.mkString(",")} - (field,column) :($f,$i).")
                  None
              }.toMap))

        case Left(error) =>
          System.err.println(error.getMessage)
          None
      }
      case _ => Seq()
    }
  }

}
