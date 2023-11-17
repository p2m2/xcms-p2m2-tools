package fr.inrae.p2m2.parser
import fr.inrae.p2m2.ReadFile.rsc
import fr.inrae.p2m2.format.XCMSFeaturesIon
import utest.{TestSuite, Tests, test}

object XCMSTest extends TestSuite {

  def tests: Tests = Tests {

    test("getSepFromHeader") {
      val resource = rsc("src/test/resources/test.tsv")
      val header: Option[String] = resource.split("\n").headOption
      assert(header.nonEmpty)
      assert(XCMS.getSepAndHeader(header).getOrElse(("\t",Seq()))._1 == "\t")
    }

    test("ex1Parse tsv") {
      val resource = rsc("src/test/resources/test.tsv")
      val l : Seq[Option[XCMSFeaturesIon]] = XCMS.parse(resource)
      assert(l.nonEmpty)
      assert(!l.contains(None))
    }

    test("ex1Parse csv") {
      val resource = rsc("src/test/resources/test.csv")
      val l: Seq[Option[XCMSFeaturesIon]] = XCMS.parse(resource)
      assert(l.nonEmpty)
      assert(!l.contains(None))
    }
  }
}