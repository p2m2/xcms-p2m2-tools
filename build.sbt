
scalaVersion := "2.13.12"
name := "xcms-p2m2-tools"
organization := "com.github.p2m2"
version := "1.0"
organizationName := "p2m2"
organizationHomepage := Some(url("https://www6.inrae.fr/p2m2"))
licenses := Seq("MIT License" -> url("http://www.opensource.org/licenses/mit-license.php"))

scmInfo := Some(
  ScmInfo(
    url("https://github.com/p2m2/xcms-p2m2-tools"),
    "scm:git@github.com:p2m2/xcms-p2m2-tools.git"
  )
)

developers := List(
  Developer("ofilangi", "Olivier Filangi", "olivier.filangi@inrae.fr",url("https://github.com/ofilangi"))
)

lazy val root = (project in file(".")).
  enablePlugins(ScalaJSPlugin).
  enablePlugins(ScalaJSBundlerPlugin).
  settings(
    name := "xcms-p2m2-tools",
    version := "1.0",
    credentials += {
      val realm = scala.util.Properties.envOrElse("REALM_CREDENTIAL", "")
      val host = scala.util.Properties.envOrElse("HOST_CREDENTIAL", "")
      val login = scala.util.Properties.envOrElse("LOGIN_CREDENTIAL", "")
      val pass = scala.util.Properties.envOrElse("PASSWORD_CREDENTIAL", "")

      val file_credential = Path.userHome / ".sbt" / ".credentials"

      if (reflect.io.File(file_credential).exists) {
        Credentials(file_credential)
      } else {
        Credentials(realm, host, login, pass)
      }
    },
    publishTo := {
      if (isSnapshot.value)
        Some("Sonatype Snapshots Nexus" at "https://oss.sonatype.org/content/repositories/snapshots")
      else
        Some("Sonatype Snapshots Nexus" at "https://oss.sonatype.org/service/local/staging/deploy/maven2")
    },
    publishConfiguration := publishConfiguration.value.withOverwrite(true),
    publishLocalConfiguration := publishLocalConfiguration.value.withOverwrite(true),
    pomIncludeRepository := { _ => false },
    publishMavenStyle := true,
    coverageMinimumStmtTotal := 20,
    coverageFailOnMinimum := false,
    coverageHighlighting := true,
    libraryDependencies ++= Seq(
      "com.lihaoyi" %%% "utest" % "0.8.2" % Test,
      "com.lihaoyi" %%% "scalatags" % "0.12.0",
      "org.scala-js" %%% "scala-js-macrotask-executor" % "1.1.1"
    ),
    testFrameworks += new TestFramework("utest.runner.Framework"),
    Compile / fastOptJS / scalaJSLinkerConfig ~= {
      _.withOptimizer(false)
        .withPrettyPrint(true)
        .withSourceMap(true)
    },
    Compile / fullOptJS / scalaJSLinkerConfig ~= {
      _.withSourceMap(false)
        .withModuleKind(ModuleKind.CommonJSModule)
    },
    Compile / scalaJSUseMainModuleInitializer := true,
    Compile / mainClass := Some("fr.inrae.p2m2.webapp.WebApp"),
    webpackBundlingMode := BundlingMode.LibraryAndApplication(),
    assembly / assemblyMergeStrategy := {
      case PathList("META-INF", _*) => MergeStrategy.discard
      case _ => MergeStrategy.first
    }
  )

Global / onChangedBuildSource := ReloadOnSourceChanges

