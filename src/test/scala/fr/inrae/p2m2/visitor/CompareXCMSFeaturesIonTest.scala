package fr.inrae.p2m2.visitor

import fr.inrae.p2m2.ReadFile.rsc
import fr.inrae.p2m2.format.XCMSFeaturesIon
import utest.{TestSuite, Tests, test}

object CompareXCMSFeaturesIonTest extends TestSuite {

  def tests: Tests = Tests {
    test("compareWithMed true") {
      assert(
        CompareXCMSFeaturesIon.compareWithMed(
        XCMSFeaturesIon(Map("mzmed"->"179.0564","rtmed"-> "18.5")),
        XCMSFeaturesIon(Map("mzmed"->"179.0561","rtmed"-> "18.2")), 5.0, 1.0))
    }
    test("compareWithMed false with mzmed") {
      assert(!
        CompareXCMSFeaturesIon.compareWithMed(
        XCMSFeaturesIon(Map("mzmed" -> "179.0564","rtmed"-> "18.5")),
        XCMSFeaturesIon(Map("mzmed" -> "170.0561","rtmed"-> "18.2")), 5.0,1.0))
    }

    test("compareWithMed false with rtmed") {
      assert(!CompareXCMSFeaturesIon.compareWithMed(
        XCMSFeaturesIon(Map("mzmed" -> "179.0564", "rtmed" -> "12.5")),
        XCMSFeaturesIon(Map("mzmed" -> "179.0561", "rtmed" -> "18.2")), 5.0, 1.0))
    }

    test("compareWithListXCMSFeaturesIon") {
      val f1 = XCMSFeaturesIon(Map("mzmed"->"179.0564","rtmed"-> "18.5"))
      val listIons = Seq(
        Some(XCMSFeaturesIon(Map("mzmed"->"179.0561","rtmed"-> "18.2"))),
        Some(XCMSFeaturesIon(Map("mzmed" -> "170.0561","rtmed"-> "18.2")))
      )

      assert(CompareXCMSFeaturesIon.compareWithListXCMSFeaturesIon(Some(f1),listIons, 5.0, 1.0).nonEmpty)
    }

    test("getColumnCompare") {
      val resource1 = rsc("src/test/resources/test.tsv")
      val resource2 = rsc("src/test/resources/test.csv")

      val l = CompareXCMSFeaturesIon.getColumnCompare(resource1, Seq(("ressource2",resource2)),
        5.0, 0.2)
      assert(l.nonEmpty)
    }


  }
}
