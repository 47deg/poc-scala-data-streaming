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

package com.fortyseven.coreheaders.config.internal

import scala.concurrent.duration.FiniteDuration

object KafkaConfig:

  final case class KafkaConf(broker: BrokerConf, consumer: Option[ConsumerConf], producer: Option[ProducerConf])

  final case class BrokerConf(brokerAddress: String)

  final case class ConsumerConf(topicName: String, autoOffsetReset: String, groupId: String, maxConcurrent: Int)

  final case class ProducerConf(
      topicName: String,
      valueSerializerClass: String,
      maxConcurrent: Int,
      compressionType: String,
      commitBatchWithinSize: Int,
      commitBatchWithinTime: FiniteDuration
    )