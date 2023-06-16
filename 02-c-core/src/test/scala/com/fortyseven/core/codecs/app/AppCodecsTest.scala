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

package com.fortyseven.core.codecs.app

import com.fortyseven.core.TestUtils.getOutput
import com.fortyseven.core.TestUtils.given
import com.fortyseven.coreheaders.model.app.model.*
import munit.ScalaCheckSuite
import org.scalacheck.Prop.forAll
import com.fortyseven.core.codecs.app.AppCodecs.given

class AppCodecsTest extends ScalaCheckSuite:

  property("Total distance by trip should return the same value after encoding and decoding"):
    forAll: (totalDistanceByTrip: TotalDistanceByTrip) =>
      assert(getOutput(totalDistanceByTrip).isRight)

  property("Total distance by user should return the same value after encoding and decoding"):
    forAll: (totalDistanceByUser: TotalDistanceByUser) =>
      assert(getOutput(totalDistanceByUser).isRight)

  property("Current speed should return the same value after encoding and decoding"):
    forAll: (currentSpeed: CurrentSpeed) =>
      assert(getOutput(currentSpeed).isRight)

  property("Total range should return the same value after encoding and decoding"):
    forAll: (totalRange: TotalRange) =>
      assert(getOutput(totalRange).isRight)
