import sbt.*

object Dependencies {

  object Versions {

    val scala = "3.2.2"

    val organizeImports = "0.6.0"

    val catsEffect = "3.4.10"
    val ciris = "3.1.0"
    val fs2Kafka = "3.0.0"
    val log4cats = "2.6.0"
    val logback = "1.4.7"
    val scalatest = "3.2.15"
    val munitScalacheck = "1.0.4"
    val munitCatsEffect = "1.0.7"
    val testContainers  = "0.40.15"
  }

  object SbtPlugins {
    val organizeImports = "com.github.liancheng" %% "organize-imports" % Versions.organizeImports
  }

  object Libraries {

    object cats {
      val catsEffect = "org.typelevel" %% "cats-effect" % Versions.catsEffect
    }

    object codec {
      val fs2KafkaVulcan = "com.github.fd4s" %% "fs2-kafka-vulcan" % Versions.fs2Kafka
    }

    object config {
      val ciris = "is.cir" %% "ciris" % Versions.ciris
    }

    object integrationTest {
      val munit = "com.dimafeng" %% "testcontainers-scala-munit" % Versions.testContainers
      val kafka = "com.dimafeng" %% "testcontainers-scala-kafka" % Versions.testContainers
    }

    object kafka {
      val fs2Kafka = "com.github.fd4s" %% "fs2-kafka" % Versions.fs2Kafka
    }

    object logging {
      val log4catsSlf4j = "org.typelevel" %% "log4cats-slf4j" % Versions.log4cats
      val logback = "ch.qos.logback" % "logback-classic" % Versions.logback
    }

    object test {
      val scalatest = "org.scalatest" %% "scalatest" % Versions.scalatest
      val munitScalacheck = "org.typelevel" %% "scalacheck-effect-munit" % Versions.munitScalacheck
      val munitCatsEffect = "org.typelevel" %% "munit-cats-effect-3" % Versions.munitCatsEffect
    }
  }
}