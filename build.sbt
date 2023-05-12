import Dependencies.*
import CustomSbt.*

Global / onChangedBuildSource := ReloadOnSourceChanges

ThisBuild / organization := "com.47deg"

ThisBuild / scmInfo      := Some(
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
    "-Ykind-projector"
  ) ++ Seq("-rewrite", "-indent") ++ Seq("-source", "future-migration")

lazy val `poc-scala-data-streaming`: Project =
  project
    .in(file("."))
    .aggregate(
      // layer 1
      // team red
      `core-headers`,
      // team yellow (utils/common)

      // layer 2
      // team blue
      configuration,
      `data-generator`,
      `kafka-consumer`,
      `job-processor-flink`,
      //`job-processor-kafka`,
      //`job-processor-spark`,
      //`job-processor-storm`,
      // team green
      core,

      // layer 3
      // team red
      `entry-point`
    )

// layer 1

// team red
lazy val `core-headers`: Project =
  project
    .in(file("01-core-headers"))
    .settings(commonSettings)
    .settings(
      name := "core-headers",
      libraryDependencies ++= Seq()
    )

// layer 2

// team yellow (c=common)
lazy val configuration: Project = (project in file("02-c-config-ciris"))
  .dependsOn(`core-headers`)
  .settings(commonSettings)
  .settings(
    name := "configuration",
    libraryDependencies ++= Seq(
      Libraries.config.cirisRefined
    )
  )

lazy val `data-generator`: Project = (project in file("02-c-data-generator"))
  .dependsOn(`core-headers` % Cctt)
  .dependsOn(core % Cctt) // This should be avoided
  .settings(commonSettings)
  .settings(
    name := "data-generator",
    libraryDependencies ++= Seq(
      Libraries.test.munitCatsEffect,
      Libraries.logging.log4catsSlf4j % Test
    )
  )

//lazy val `kafka-util`: Project = (project in file("02-c-kafka-util"))
//  .settings(
//    name := "kafka-util"
//  )

// team blue (i=input) from here
lazy val `kafka-consumer`: Project =
  project
    .in(file("02-i-kafka-consumer"))
    //.dependsOn(`kafka-util` % Cctt)
    .dependsOn(`core-headers` % Cctt)
    .settings(commonSettings)
    .settings(
      name := "kafka-consumer",
      libraryDependencies ++= Seq(
        Libraries.kafka.fs2KafkaVulcan
      )
    )

lazy val `job-processor-flink`: Project =
  project
    .in(file("02-i-job-processor-flink"))
    .dependsOn(`core-headers` % Cctt)
    .settings(commonSettings)
    .settings(
        name := "flink",
        libraryDependencies ++= Seq(
          Libraries.cats.catsEffect,
          Libraries.flink.clients,
          Libraries.flink.kafka
        )
    )

lazy val `job-processor-flink-integration`: Project =
  project.in(file("02-i-job-processor-flink/integration"))
    .dependsOn(`job-processor-flink`)
    .settings(commonSettings)
    .settings(
      publish / skip := true,
      libraryDependencies ++= Seq(
        Libraries.integrationTest.kafka,
        Libraries.integrationTest.munit,
        Libraries.test.munitCatsEffect,
        Libraries.logging.log4catsSlf4j % Test,
        Libraries.cats.catsEffect % Test
      )
    )

//lazy val `job-processor-kafka`: Project =
//  project
//    .in(file("02-i-job-processor-kafka"))
//    .dependsOn(`core-headers` % Cctt)
//    .settings(commonSettings)
//    .settings(commonDependencies)
//    .settings(
//      name := "kafka-streams",
//      libraryDependencies ++= Seq()
//    )
//
//lazy val `job-processor-spark`: Project =
//project
//    .in(file("02-i-job-processor-spark"))
//    .dependsOn(`core-headers` % Cctt)
//    .settings(commonSettings)
//    .settings(commonDependencies)
//    .settings(
//      name := "spark-streaming",
//      libraryDependencies ++= Seq()
//    )
//
//lazy val `job-processor-storm`: Project =
//  project
//    .in(file("02-i-job-processor-storm"))
//    .dependsOn(`core-headers` % Cctt)
//    .settings(commonSettings)
//    .settings(commonDependencies)
//    .settings(
//      name := "storm",
//      libraryDependencies ++= Seq()
//    )

// team green (o=output) from here
lazy val core: Project =
  project
    .in(file("02-o-core"))
    .dependsOn(`core-headers` % Cctt)
    .settings(commonSettings)
    .settings(
      name := "core",
      libraryDependencies ++= Seq(
        Libraries.kafka.fs2KafkaVulcan,
        Libraries.test.munitScalacheck,
        Libraries.test.scalatest
      )
    )

// layer 3
// team red
lazy val `entry-point`: Project =
  project
    .in(file("03-entry-point"))
    // the dependency on `core-headers` is added transitively
    // the dependencies on team yellow are added transitively
    // team blue
    .dependsOn(`kafka-consumer` % Cctt)
    .dependsOn(`data-generator` % Cctt)
    //.dependsOn(`job-processor-flink` % Cctt)
    //.dependsOn(`job-processor-kafka` % Cctt)
    //.dependsOn(`job-processor-spark` % Cctt)
    //.dependsOn(`job-processor-storm` % Cctt)
    // team green
    .dependsOn(configuration % Cctt)
    .dependsOn(core % Cctt)
    .settings(commonSettings)
    .settings(
      name := "entry-point",
      libraryDependencies ++= Seq(
        Libraries.logging.logback,
        Libraries.logging.log4catsSlf4j
      )
    )

lazy val commonSettings = commonScalacOptions ++ Seq(
  resolvers += "confluent" at "https://packages.confluent.io/maven/",
  update / evictionWarningOptions := EvictionWarningOptions.empty
)

lazy val commonScalacOptions = Seq(
  Compile / console / scalacOptions --= Seq(
    "-Wunused:_",
    "-Xfatal-warnings"
  ),
  Test / console / scalacOptions := (Compile / console / scalacOptions).value
)

