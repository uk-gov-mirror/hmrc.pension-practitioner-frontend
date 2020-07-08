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

package controllers.register.company

import config.FrontendAppConfig
import connectors.cache.UserAnswersCacheConnector
import controllers.DataRetrievals
import controllers.actions._
import forms.register.company.ConfirmNameFormProvider
import javax.inject.Inject
import models.NormalMode
import navigators.CompoundNavigator
import pages.register.company.ConfirmNamePage
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import renderer.Renderer
import uk.gov.hmrc.play.bootstrap.controller.FrontendBaseController
import uk.gov.hmrc.viewmodels.{NunjucksSupport, Radios}

import scala.concurrent.{ExecutionContext, Future}

class ConfirmNameController @Inject()(override val messagesApi: MessagesApi,
                                      userAnswersCacheConnector: UserAnswersCacheConnector,
                                      navigator: CompoundNavigator,
                                      identify: IdentifierAction,
                                      getData: DataRetrievalAction,
                                      requireData: DataRequiredAction,
                                      formProvider: ConfirmNameFormProvider,
                                      val controllerComponents: MessagesControllerComponents,
                                      config: FrontendAppConfig,
                                      renderer: Renderer
                                      )(implicit ec: ExecutionContext) extends FrontendBaseController with I18nSupport with NunjucksSupport {

  private val form = formProvider()

  def onPageLoad(): Action[AnyContent] = (identify andThen getData andThen requireData).async {
    implicit request =>
      DataRetrievals.retrieveCompanyName { pspName =>
        val preparedForm = request.userAnswers.get (ConfirmNamePage) match {
          case None => form
          case Some (value) => form.fill (value)
        }

        val json = Json.obj(
          "form" -> preparedForm,
          "pspName" -> pspName,
          "submitUrl" -> routes.ConfirmNameController.onSubmit().url,
          "radios" -> Radios.yesNo (preparedForm("value"))
        )

      renderer.render ("register/company/confirmName.njk", json).map(Ok (_))
    }
  }

  def onSubmit(): Action[AnyContent] = (identify andThen getData andThen requireData).async {
    implicit request =>
      DataRetrievals.retrieveCompanyName { pspName =>
        form.bindFromRequest().fold(
          formWithErrors => {

            val json = Json.obj(
              "form"   -> formWithErrors,
              "pspName" -> pspName,
              "submitUrl"   -> routes.ConfirmNameController.onSubmit().url,
              "radios" -> Radios.yesNo(formWithErrors("value"))
            )

            renderer.render("register/company/confirmName.njk", json).map(BadRequest(_))
          },
          value =>
            for {
              updatedAnswers <- Future.fromTry(request.userAnswers.set(ConfirmNamePage, value))
              _ <- userAnswersCacheConnector.save( updatedAnswers.data)
            } yield Redirect(navigator.nextPage(ConfirmNamePage, NormalMode, updatedAnswers))
        )
      }
  }
}
