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

package com.fortyseven.kafkaconsumer

import scala.concurrent.duration.*
import cats.*
import cats.effect.kernel.{Async, Sync}
import cats.effect.{IO, IOApp}
import cats.implicits.*
import ciris.{ConfigValue, Effect}
import com.fortyseven.configuration.kafka.KafkaConfiguration
import com.fortyseven.coreheaders.KafkaConsumerHeader
import com.fortyseven.coreheaders.config.kafka.KafkaConfigurationHeader
import fs2.kafka.*
import cats.effect.unsafe.implicits.global

class KafkaConsumer[F[_]: Async] extends KafkaConsumerHeader[F]:

  override def consume(): F[Unit] = for {
    conf <- KafkaConfiguration.config.load[F]
    runnned <- run(conf)
  } yield runnned

  def run(kc: KafkaConfiguration): F[Unit] =
    def processRecord(record: ConsumerRecord[String, String]): F[(String, String)] =
      Applicative[F].pure(record.key -> record.value)

    val consumerSettings =
      ConsumerSettings[F, String, String]
        .withAutoOffsetReset(AutoOffsetReset.Earliest)
        .withBootstrapServers(kc.consumerConfiguration.bootstrapServers.toString)
        .withGroupId(kc.consumerConfiguration.groupId.toString)

    val producerSettings =
      ProducerSettings[F, String, String].withBootstrapServers(kc.producerConfiguration.bootstrapServers.toString)

    val stream =
      fs2.kafka.KafkaConsumer
        .stream(consumerSettings)
        .subscribeTo(kc.streamConfiguration.inputTopic.toString)
        .records
        .mapAsync(25) { committable =>
          processRecord(committable.record).map { case (key, value) =>
            val record = ProducerRecord(kc.streamConfiguration.outputTopic.toString, key, value)
            committable.offset -> ProducerRecords.one(record)
          }
        }.through { offsetsAndProducerRecords =>
          KafkaProducer.stream(producerSettings).flatMap { producer =>
            offsetsAndProducerRecords
              .evalMap { case (offset, producerRecord) =>
                producer.produce(producerRecord).map(_.as(offset))
              }.parEvalMap(Int.MaxValue)(identity)
          }
        }.through(commitBatchWithin(kc.streamConfiguration.commitBatchWithinSize.toString.toInt, kc.streamConfiguration.commitBatchWithinTime))

    stream.compile.drain
