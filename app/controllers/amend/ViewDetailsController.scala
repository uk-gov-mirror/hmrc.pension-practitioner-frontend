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

package controllers.amend

import com.google.inject.Inject
import controllers.actions.{AuthAction, DataRetrievalAction}
import play.api.i18n.I18nSupport
import play.api.mvc.{AnyContent, MessagesControllerComponents, Action}
import renderer.Renderer
import services.PspDetailsService
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import utils.annotations.AuthMustHaveEnrolment

import scala.concurrent.{Future, ExecutionContext}

class ViewDetailsController @Inject()(@AuthMustHaveEnrolment authenticate: AuthAction,
                                     getData: DataRetrievalAction,
                                     pspDetailsService: PspDetailsService,
                                     renderer: Renderer,
                                     val controllerComponents: MessagesControllerComponents
                                    )(implicit val executionContext: ExecutionContext) extends FrontendBaseController with I18nSupport {

  def onPageLoad: Action[AnyContent] = (authenticate andThen getData).async {
    implicit request =>
      request.user.alreadyEnrolledPspId.map { pspId =>
        pspDetailsService.getJson(request.userAnswers, pspId).flatMap { json =>
          renderer.render("amend/viewDetails.njk", json).map(Ok(_))
        }
      }.getOrElse(
        Future.successful(Redirect(controllers.routes.SessionExpiredController.onPageLoad()))
      )
  }

}
