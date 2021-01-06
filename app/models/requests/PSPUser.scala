/*
 * Copyright 2021 HM Revenue & Customs
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

package models.requests

import models.requests.UserType.UserType
import uk.gov.hmrc.domain.Nino

case class PSPUser(userType: UserType,
                   nino: Option[Nino],
                   isExistingPSP: Boolean,
                   existingPSPId: Option[String],
                   alreadyEnrolledPspId: Option[String] = None,
                   userId: String = ""
                  ){
  def pspIdOrException: String = alreadyEnrolledPspId.getOrElse(throw PspIdNotFoundException)
}

object UserType extends Enumeration {
  type UserType = Value
  val Individual, Organisation = Value
}

case object PspIdNotFoundException extends Exception