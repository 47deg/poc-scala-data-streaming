/*
 * Copyright 2023 Xebia Functional
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fortyseven.coreheaders.model.types

import munit.ScalaCheckSuite
import org.scalacheck.Gen
import org.scalacheck.Prop.forAll
import com.fortyseven.coreheaders.model.types.types.*
import org.scalacheck

class typesTest extends ScalaCheckSuite:

  property("Latitudes grater than 90 are not allowed"):
    forAll(Gen.chooseNum(90.0, Double.MaxValue).suchThat(_ > 90.0)): latitude =>
      Latitude(latitude).isLeft

  property("Latitudes smaller than -90 are not allowed"):
    forAll(Gen.chooseNum(Double.MinValue, -90.0).suchThat(_ < -90.0)): latitude =>
      Latitude(latitude).isLeft

  property("Latitude should build from values that conform with Earth latitude limits"):
    forAll(Gen.chooseNum(-90.0, 90.0)): latitude =>
      Latitude(latitude).isRight

  property("Longitude grater than 180 are not allowed"):
    forAll(Gen.chooseNum(180.0, Double.MaxValue).suchThat(_ > 180.0)): longitude =>
      Longitude(longitude).isLeft

  property("Longitude smaller than -180 are not allowed"):
    forAll(Gen.chooseNum(Double.MinValue, -180.0).suchThat(_ < -180.0)): longitude =>
      Longitude(longitude).isLeft

  property("Longitude should build from values that conform with Earth longitude limits"):
    forAll(Gen.chooseNum(-180.0, 180.0)): longitude =>
      Longitude(longitude).isRight

  property("An invalid Percentage should have values bellow 0"):
    forAll(Gen.negNum[Double]): percentage =>
      Percentage(percentage).isLeft

  property("An invalid Percentage should have values above 100"):
    forAll(Gen.chooseNum(100.0, Double.MaxValue).suchThat(_ > 100.0)): percentage =>
      Percentage(percentage).isLeft

  property("A valid Percentage should have values between 0 and 100"):
    forAll(Gen.chooseNum(0.0, 100.0)): percentage =>
      Percentage(percentage).isRight

  property("An invalid Speed should have a value lower than 0"):
    forAll(Gen.negNum[Double]): speed =>
      Speed(speed).isLeft

  property("A valid Speed should have a value equal to 0 or higher"):
    forAll(Gen.posNum[Double]): speed =>
      Speed(speed).isRight

  property("An invalid Hz should have a value lower than 0"):
    forAll(Gen.negNum[Double]): hz =>
      Hz(hz).isLeft

  property("A valid Hz should have a value equal to 0 or higher"):
    forAll(Gen.posNum[Double]): hz =>
      Hz(hz).isRight

  property("An invalid Bar should have a value lower than 0"):
    forAll(Gen.negNum[Double]): bar =>
      Bar(bar).isLeft

  property("A valid Bar should have a value equal to 0 or higher"):
    forAll(Gen.posNum[Double]): bar =>
      Bar(bar).isRight

  property("An invalid Meters should have a value lower than 0"):
    forAll(Gen.negNum[Int]): meters =>
      Meters(meters).isLeft

  property("A valid Meters should have a value equal to 0 or higher"):
    forAll(Gen.posNum[Int]): meters =>
      Meters(meters).isRight
