import Dependencies._
import Util._

Global / onChangedBuildSource := ReloadOnSourceChanges
ThisBuild / organization := "com.47deg"
ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/47deg/poc-scala-data-streaming"),
    "scm:git@github.com:47deg/poc-scala-data-streaming.git"
  )
)
ThisBuild / scalaVersion := Versions.scala
ThisBuild / semanticdbEnabled := true
ThisBuild / scalafixDependencies += SbtPlugins.organizeImports

ThisBuild / scalacOptions ++=
  Seq(
    "-deprecation",
    "-explain",
    "-feature",
    "-language:implicitConversions",
    "-unchecked",
    "-Xfatal-warnings",
    "-Yexplicit-nulls", // experimental (I've seen it cause issues with circe)
    "-Ykind-projector",
    "-Ysafe-init", // experimental (I've seen it cause issues with circe)
  ) ++ Seq("-rewrite", "-indent") ++ Seq("-source", "future-migration")

lazy val `poc-scala-data-streaming`: Project =
  project.in(file("."))
    .aggregate(
      // layer 1
      // team red
      `core-headers`,
      // team yellow (utils/common)

      // layer 2
      // team blue
      `kafka-consumer`,
      `job-processor-flink`,
      `job-processor-spark`,
      `job-processor-storm`,
      // team green
      core,

      // layer 3
      // team red
      main,
    )

// layer 1

// team red

lazy val `core-headers`: Project =
  project
    .in(file("01-core-headers"))
    .settings(commonSettings)
    .settings(commonDependencies)
    .settings(
      name := "core-headers",
      libraryDependencies ++= Seq(
        // the less the better
      )
    )

// layer 2

// team yellow (c=common)
lazy val `data-generator`: Project = (project in file("02-c-data-generator"))
  .settings(
    name := "data-generator"
  )
  .dependsOn(`core-headers`)

lazy val `kafka-util`: Project = (project in file("02-c-kafka-util"))
  .settings(
    name := "kafka-util"
  )
  .dependsOn(`core-headers`)

// team blue (i=input) from here
lazy val `kafka-consumer`: Project =
  project
    .in(file("02-i-kafka-consumer"))
    .dependsOn(`kafka-util` % Cctt) // does not depend in core-headers because it depends on kafka-utils (transitive)
    .settings(commonSettings)

lazy val `job-processor-spark`: Project =
  project
    .in(file("02-i-job-processor-spark"))
    .dependsOn(`core-headers` % Cctt)
    .settings(commonSettings)
    .settings(
      libraryDependencies ++= Seq()
    )

lazy val `job-processor-flink`: Project =
  project
    .in(file("02-i-job-processor-flink"))
    .dependsOn(`core-headers` % Cctt)
    .settings(commonSettings)
    .settings(
      libraryDependencies ++= Seq()
    )

lazy val `job-processor-storm`: Project =
  project
    .in(file("02-i-job-processor-storm"))
    .dependsOn(`core-headers` % Cctt)
    .settings(commonSettings)
    .settings(
      libraryDependencies ++= Seq()
    )

// team green (o=output) from here
lazy val core: Project =
  project
    .in(file("02-o-core"))
    .dependsOn(`core-headers` % Cctt)
    .settings(commonSettings)
    .settings(
      name := "core",
      libraryDependencies ++= Seq()
    )


// layer 3
// team red
lazy val main: Project =
  project
    .in(file("03-main"))
    // the dependency on `core-headers` is added transitively
    // the dependencies on team yellow are added transitively
    // team blue
    .dependsOn(`kafka-consumer` % Cctt)
    .dependsOn(`data-generator` % Cctt)
    .dependsOn(`job-processor-flink` % Cctt)
    .dependsOn(`job-processor-spark` % Cctt)
    .dependsOn(`job-processor-storm` % Cctt)
    // team green
    .dependsOn(core % Cctt)
    .settings(commonSettings)
    .settings(
      name := "main",
      libraryDependencies ++= Seq(
        // the less the better (usually zero)
      )
    )

lazy val commonSettings = commonScalacOptions ++ Seq(
  update / evictionWarningOptions := EvictionWarningOptions.empty
)

lazy val commonScalacOptions = Seq(
  Compile / console / scalacOptions --= Seq(
    "-Wunused:_",
    "-Xfatal-warnings",
  ),
  Test / console / scalacOptions :=
    (Compile / console / scalacOptions).value,
)

lazy val commonDependencies = Seq(
  libraryDependencies ++= Seq(
    // main dependencies
  ),
  libraryDependencies ++= Seq(
    org.scalatest.scalatest,
    org.scalatestplus.`scalacheck-1-15`,
  ).map(_ % Test)
)

