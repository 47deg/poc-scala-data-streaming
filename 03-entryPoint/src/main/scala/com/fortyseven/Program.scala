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

package com.fortyseven

import cats.effect.unsafe.implicits.global
import cats.effect.{IO, IOApp}
import com.fortyseven.configuration.dataGenerator.DataGeneratorConfigurationEffect
import com.fortyseven.configuration.kafka.{KafkaConfiguration, KafkaConfigurationEffect}
import com.fortyseven.datagenerator.DataGenerator
import com.fortyseven.kafkaconsumer.KafkaConsumer
import org.typelevel.log4cats.slf4j.Slf4jLogger

object Program:

  val run: IO[Unit] = for
    logger   <- Slf4jLogger.create[IO]
    config   <- new KafkaConfigurationEffect[IO].configuration
    _        <- logger.info(config.toString)
    kafkaCon <- new KafkaConfigurationEffect[IO].configuration
    _        <- logger.info(kafkaCon.toString)
    _        <- new DataGenerator[IO].run.background.use { f => f.start }
    _        <- new KafkaConsumer[IO].consume()
  yield ()
