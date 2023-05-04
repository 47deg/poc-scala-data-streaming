import sbt._

object Dependencies {

  object Versions {

    val scala = "3.2.2"

    val organizeImports = "0.6.0"

    val catsEffect = "3.4.10"
    val ciris = "3.1.0"
    val `fs2-kafka` = "3.0.0"
    val scalatest = "3.2.15"
    val munitScalacheck = "0.7.29"
    val munitCatsEffect = "1.0.7"
  }

  object SbtPlugins {
    val organizeImports = "com.github.liancheng" %% "organize-imports" % Versions.organizeImports
  }

  object Libraries {

    object cats {
      val catsEffect = "org.typelevel" %% "cats-effect" % Versions.catsEffect
    }

    object config {
      private val ciris = "is.cir" %% "ciris" % Versions.ciris
      private val cirisRefined = "is.cir" %% "ciris-refined" % Versions.ciris
      val all: Seq[ModuleID] = Seq(ciris, cirisRefined)
    }

    object kafka {
      val fs2Kafka = "com.github.fd4s" %% "fs2-kafka" % Versions.`fs2-kafka`
    }

    object test {
      val scalatest = "org.scalatest" %% "scalatest" % Versions.scalatest
      val munitScalacheck = "org.scalameta" %% "munit-scalacheck" % Versions.munitScalacheck
      val munitCatsEffect = "org.typelevel" %% "munit-cats-effect-3" % Versions.munitCatsEffect
    }
  }
}