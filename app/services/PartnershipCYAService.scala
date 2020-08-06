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

package services

import models.{Address, CheckMode, UserAnswers}
import pages.partnership._
import play.api.i18n.Messages
import uk.gov.hmrc.viewmodels.SummaryList.{Action, Key, Row, Value}
import uk.gov.hmrc.viewmodels.Text.Literal
import uk.gov.hmrc.viewmodels._

class PartnershipCYAService extends CYAService {

  def partnershipCya(ua: UserAnswers)(implicit messages: Messages): Seq[Row] =
    (
      ua.get(BusinessNamePage),
      ua.get(BusinessUTRPage),
      ua.get(PartnershipAddressPage),
      ua.get(PartnershipEmailPage),
      ua.get(PartnershipPhonePage)
    ) match {
      case (Some(name), Some(utr), Some(address), Some(email), Some(phone)) =>
          Seq(
            partnershipName(name),
            partnershipUtr(utr),
            partnershipAddress(address),
            partnershipEmail(name, email),
            partnershipPhone(name, phone)
          )
      case _ => Seq.empty

    }

  private def partnershipName(name: String): Row =
      Row(
        key = Key(msg"cya.partnershipName", classes = Seq("govuk-!-width-one-half")),
        value = Value(Literal(name), classes = Seq("govuk-!-width-one-third"))
      )

  private def partnershipUtr(utr: String): Row =
      Row(
        key = Key(msg"cya.utr", classes = Seq("govuk-!-width-one-half")),
        value = Value(Literal(utr), classes = Seq("govuk-!-width-one-third"))
      )

  private def partnershipAddress(address: Address)(implicit messages: Messages): Row =
      Row(
        key = Key(msg"cya.address", classes = Seq("govuk-!-width-one-half")),
        value = Value(addressAnswer(address), classes = Seq("govuk-!-width-one-third")),
        actions = List(
          Action(
            content = msg"site.edit",
            href = controllers.partnership.routes.PartnershipPostcodeController.onPageLoad(CheckMode).url,
            visuallyHiddenText = Some(msg"cya.change.address")
          )
        )
      )

  private def partnershipEmail(partnershipName: String, email: String): Row =
    Row(
      key = Key(msg"cya.email".withArgs(partnershipName), classes = Seq("govuk-!-width-one-half")),
      value = Value(Literal(email), classes = Seq("govuk-!-width-one-third")),
      actions = List(
        Action(
          content = msg"site.edit",
          href = controllers.partnership.routes.PartnershipEmailController.onPageLoad(CheckMode).url,
          visuallyHiddenText = Some(msg"cya.change.email".withArgs(partnershipName))
        )
      )
    )

  private def partnershipPhone(partnershipName: String, phone: String): Row =
    Row(
      key = Key(msg"cya.phone".withArgs(partnershipName), classes = Seq("govuk-!-width-one-half")),
      value = Value(Literal(phone), classes = Seq("govuk-!-width-one-third")),
      actions = List(
        Action(
          content = msg"site.edit",
          href = controllers.partnership.routes.PartnershipPhoneController.onPageLoad(CheckMode).url,
          visuallyHiddenText = Some(msg"cya.change.phone".withArgs(partnershipName))
        )
      )
    )
}