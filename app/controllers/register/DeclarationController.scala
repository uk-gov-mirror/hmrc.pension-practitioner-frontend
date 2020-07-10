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

package controllers.register

import controllers.Retrievals
import controllers.actions._
import javax.inject.Inject
import models.NormalMode
import navigators.CompoundNavigator
import pages.register.DeclarationPage
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.libs.json.{JsObject, Json}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import renderer.Renderer
import uk.gov.hmrc.play.bootstrap.controller.FrontendBaseController
import uk.gov.hmrc.viewmodels.NunjucksSupport

import scala.concurrent.{ExecutionContext, Future}

class DeclarationController @Inject()(override val messagesApi: MessagesApi,
                                      navigator: CompoundNavigator,
                                      identify: IdentifierAction,
                                      getData: DataRetrievalAction,
                                      requireData: DataRequiredAction,
                                      val controllerComponents: MessagesControllerComponents,
                                      renderer: Renderer
                                         )(implicit ec: ExecutionContext) extends FrontendBaseController
  with Retrievals with I18nSupport with NunjucksSupport {

  def onPageLoad: Action[AnyContent] = (identify andThen getData andThen requireData).async {
      implicit request =>
        val json: JsObject = Json.obj("submitUrl" -> routes.DeclarationController.onSubmit().url)
        renderer.render("register/declaration.njk", json).map(Ok(_))
    }

  def onSubmit: Action[AnyContent] = (identify andThen getData andThen requireData).async {
      implicit request =>
         Future.successful(Redirect(navigator.nextPage(DeclarationPage, NormalMode, request.userAnswers)))
    }

}
