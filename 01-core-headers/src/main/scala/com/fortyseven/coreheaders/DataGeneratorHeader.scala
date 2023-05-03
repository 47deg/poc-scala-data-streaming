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

package com.fortyseven.coreheaders

import com.fortyseven.coreheaders.model.app.model.*
import com.fortyseven.coreheaders.model.iot.model.*

trait DataGeneratorHeader[F[_]]:

  def generateBatteryCharge: F[BateryCharge]

  def generateBatteryHealth: F[BatteryHealth]

  def generateBreaksHealth: F[BreaksHealth]

  def generateBreaksUsage: F[BreaksUsage]

  def generateGPSPosition: F[GPSPosition]

  def generatePneumaticPressure: F[PneumaticPressure]

  def generateWheelRotation: F[WheelRotation]

  def generateCurrentSpeed: F[CurrentSpeed]

  def generateTotalDistanceByTrip: F[TotalDistanceByTrip]

  def generateTotalDistancePerUser: F[TotalDistanceByUser]

  def generateTotalRange: F[TotalRange]