package fr.inrae.p2m2.webapp

import fr.inrae.p2m2.visitor.CompareXCMSFeaturesIon
import org.scalajs.dom
import org.scalajs.dom.html.{Input, Table}
import org.scalajs.dom.{File, FileReader, HTMLInputElement, window}
import org.scalajs.macrotaskexecutor.MacrotaskExecutor.Implicits._
import scalatags.JsDom
import scalatags.JsDom.all._

import scala.annotation.unused
import scala.concurrent.{ExecutionContext, Future, Promise}
import scala.scalajs.js.URIUtils.encodeURI
import scala.util.{Failure, Success, Try}

object WebApp {
  private def readFileAsText(file : dom.File)(implicit @unused ec: ExecutionContext) : Future[String] = {
    val p = Promise[String]()
    val fr = new FileReader()

    fr.onload = _ => {
      p.success(fr.result.toString)
    }

    fr.onerror = _ => {
      System.err.println(s"** Can not parse file ** ")
      p.failure(new Exception())
    }

    fr.readAsText(file,"ISO-8859-1")
    p.future
  }

  private var allStaticFiles : Seq[File] = Seq()

  private def getPpmUser : Double =
    Try(dom
    .document
    .getElementById("ppm").asInstanceOf[Input].value.toDouble).toOption.getOrElse(5.0)

  private def getRtWindowsUser: Double =
    Try(dom
      .document
      .getElementById("rt").asInstanceOf[Input].value.toDouble).toOption.getOrElse(0.1)

  private def drawTableFile(allFiles : Seq[File]) : Table = {

    val lFutures = Future.sequence(allFiles.zipWithIndex.map{
      case (f,indexInAllFiles) => readFileAsText(f).map(x => (indexInAllFiles,x))
    })

    table(
      `class` := "results",
      thead(tr(colspan := 2, "XCMS report files")),
      tbody(
        allFiles.zipWithIndex.map {
          case (f, indexInTable) => tr(
            td(
              div(
                onclick := {
                  () => {
                    window.open("", "Error").document.body.innerHTML =
                      tag("csv-viewer")(
                        attr("initial-content") := "h1;h2;h3\nb1;b2;b3",
                        attr("style") := "height: 500px; border: 1px solid green"
                      ).render.innerHTML
                  }
                },"CSV")
            ),
            td(
            div(
              onclick := { () => {
                if (allStaticFiles.length>1) {
                  dom.document.body.style.cursor = "progress"
                  lFutures.onComplete {
                    case Success(listData: Seq[(Int, String)]) =>
                      println(s"PPM=$getPpmUser, RT:$getRtWindowsUser")
                      val reportXCMS1 = listData.find(_._1 == indexInTable).map(_._2.split("\n").toSeq).getOrElse(Seq())
                      val listOtherReport = listData.filter(_._1 != indexInTable).map(x => (f.name, x._2.split("\n").toSeq))
                      val r = CompareXCMSFeaturesIon.getColumnCompare(reportXCMS1, listOtherReport, getPpmUser, getRtWindowsUser, "\t")
                      val name_build = f.name.split("\\.") match {
                        case s if s.nonEmpty => s.toSeq
                        case _ => Seq("csv")
                      }
                      dom.document.body.style.cursor = "default"
                      a(
                        href := "data:text/csv;charset=utf-8," + encodeURI(r.mkString("\n")),
                        target := "_blank",
                        download := (name_build.dropRight(1) :+ "new" :+ name_build.last).mkString(".")
                      ).render.click()

                    //r.mkString("\n")

                    case Failure(e) =>
                      window.open("", "Error").document.body.innerHTML = e.getMessage
                    }
                  }
                }
              }, `class` := "xcms_file", f.name)))
        }
      )
    ).render

  }

  def main(args: Array[String]): Unit = {

    val inputTag: JsDom.TypedTag[Input] = input(
      id := "inputFiles",
      `type` := "file",
      multiple := "multiple",
      onchange := {
        (ev : dom.InputEvent) =>
          val files : Seq[File] = ev.currentTarget.asInstanceOf[HTMLInputElement].files.toSeq
          allStaticFiles = allStaticFiles ++ files

          val div = dom
            .document
            .getElementById("exportCsvTable")

          div.innerHTML = ""
          div.append(drawTableFile(allStaticFiles.distinct))


      }
    )
    dom
      .document
      .getElementById("inputFilesDiv")
      .append(inputTag.render)
  }
}
