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

package generators

import org.scalacheck.Arbitrary
import pages.WhatTypeBusinessPage
import pages.company.{BusinessUTRPage, CompanyNamePage, ConfirmNamePage}
import pages.register.AreYouUKCompanyPage
import pages.register.BusinessTypePage
import pages.company.ConfirmAddressPage

trait PageGenerators {

  implicit lazy val arbitraryConfirmAddressPage: Arbitrary[ConfirmAddressPage.type] =
    Arbitrary(ConfirmAddressPage)

  implicit lazy val arbitraryConfirmNamePage: Arbitrary[ConfirmNamePage.type] =
    Arbitrary(ConfirmNamePage)

  implicit lazy val arbitraryCompanyNamePage: Arbitrary[CompanyNamePage.type] =
    Arbitrary(CompanyNamePage)

  implicit lazy val arbitraryBusinessUTRPage: Arbitrary[BusinessUTRPage.type] =
    Arbitrary(BusinessUTRPage)

  implicit lazy val arbitraryBusinessTypePage: Arbitrary[BusinessTypePage.type] =
    Arbitrary(BusinessTypePage)

  implicit lazy val arbitraryAreYouUKCompanyPage: Arbitrary[AreYouUKCompanyPage.type] =
    Arbitrary(AreYouUKCompanyPage)

  implicit lazy val arbitraryChargeTypePage: Arbitrary[WhatTypeBusinessPage.type] =
    Arbitrary(WhatTypeBusinessPage)
}
