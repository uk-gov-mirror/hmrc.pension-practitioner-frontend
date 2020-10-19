/*
 * Copyright 2020 HM Revenue & Customs
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

package models

import utils.{Enumerable, WithName}

sealed trait SubscriptionType

object SubscriptionType extends Enumerable.Implicits {

  case object Creation extends WithName("Creation") with SubscriptionType

  case object Variation extends WithName("Variation") with SubscriptionType

  val values = Seq(Creation, Variation)

  implicit val enumerable: Enumerable[SubscriptionType] =
    Enumerable(values.map(v => v.toString -> v): _*)

}
