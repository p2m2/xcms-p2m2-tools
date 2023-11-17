package fr.inrae.p2m2.parser
import fr.inrae.p2m2.ReadFile.rsc
import fr.inrae.p2m2.format.XCMSFeaturesIon
import utest.{TestSuite, Tests, test}

object XCMSTest extends TestSuite {

  def tests: Tests = Tests {

    test("getSepFromHeader") {
      val resource = rsc("src/test/resources/job1564298_bo_fes_vs_fbm.tsv")
      val header: Option[String] = resource.split("\n").headOption
      assert(header.nonEmpty)
      assert(XCMS.getSepFromHeader(header).contains("\t"))
    }

    test("ex1Parse") {
      val resource = rsc("src/test/resources/job1564298_bo_fes_vs_fbm.tsv")
      val lines: Seq[String] = resource.split("\n")
      val l : Seq[Option[XCMSFeaturesIon]] = XCMS.parse(lines,"\t")
      assert(l.nonEmpty)
      assert(!l.contains(None))
    }

    test("ex2Parse") {
      val resource = rsc("src/test/resources/job1564299_bo_fes_vs_falt.tsv")
      val lines: Seq[String] = resource.split("\n")
      val l : Seq[Option[XCMSFeaturesIon]] = XCMS.parse(lines,"\t")
      assert(l.nonEmpty)
      assert(!l.contains(None))
    }
  }
}