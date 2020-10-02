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

package controllers.actions

import javax.inject.Inject
import models.requests.AuthenticatedRequest
import models.requests.PSPUser
import models.requests.UserType
import models.requests.UserType.UserType
import play.api.mvc.AnyContent
import play.api.mvc.BodyParser
import play.api.mvc.PlayBodyParsers
import play.api.mvc.Request
import play.api.mvc.Result
import uk.gov.hmrc.domain.Nino

import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.Future

case class FakeAuthAction @Inject()(bodyParsers: PlayBodyParsers) extends AuthAction {
  private val defaultUserType: UserType = UserType.Organisation
  private val defaultPspId: String = "test psp id"
  implicit val executionContext: ExecutionContextExecutor =
    scala.concurrent.ExecutionContext.Implicits.global
  override def invokeBlock[A](
    request: Request[A],
    block: AuthenticatedRequest[A] => Future[Result]
  ): Future[Result] =
    block(
      AuthenticatedRequest(
        request,
        PSPUser(defaultUserType, Some(Nino("AB100100A")), isExistingPSP = false, None, Some(defaultPspId))
      )
    )
  override def parser: BodyParser[AnyContent] =
    bodyParsers.default
}