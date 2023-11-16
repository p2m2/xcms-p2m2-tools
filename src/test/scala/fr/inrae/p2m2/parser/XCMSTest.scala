package fr.inrae.p2m2.parser
import fr.inrae.p2m2.ReadFile.rsc
import fr.inrae.p2m2.format.XCMSFeaturesIon
import utest.{TestSuite, Tests, test}

object XCMSTest extends TestSuite {

  def tests: Tests = Tests {
    test("ex1Parse") {
      val resource = rsc("src/test/resources/job1564298_bo_fes_vs_fbm.tsv")
      val lines: Seq[String] = resource.split("\n")
      val l : Seq[XCMSFeaturesIon] = XCMS.parse(lines)
      assert(l.nonEmpty)
    }

    test("ex2Parse") {
      val resource = rsc("src/test/resources/job1564299_bo_fes_vs_falt.tsv")
      val lines: Seq[String] = resource.split("\n")
      val l : Seq[XCMSFeaturesIon] = XCMS.parse(lines)
      assert(l.nonEmpty)
    }
  }
}