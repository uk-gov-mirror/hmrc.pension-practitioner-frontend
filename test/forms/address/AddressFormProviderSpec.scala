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

package forms.address

import forms.FormSpec
import forms.behaviours.{AddressBehaviours, FormBehaviours}
import forms.mappings.AddressMappings
import models.Address
import org.mockito.Matchers.any
import org.mockito.Mockito.when
import org.scalatestplus.mockito.MockitoSugar
import play.twirl.api.Html
import utils.InputOption
import utils.countryOptions.CountryOptions

import scala.concurrent.Future
import scala.util.Random

class AddressFormProviderSpec extends FormBehaviours with MockitoSugar with FormSpec with AddressBehaviours {

  private def alphaString(max: Int = AddressMappings.maxAddressLineLength) =
    Random.alphanumeric take Random.shuffle(Range(1, max).toList).head mkString ""

  private val addressLine1 = alphaString()
  private val addressLine2 = alphaString()
  private val addressLine3 = alphaString()
  private val addressLine4 = alphaString()
  private val postCode = "ZZ1 1ZZ"

  private val countryOptions = mock[CountryOptions]

  val validData: Map[String, String] = Map(
    "addressLine1" -> addressLine1,
    "addressLine2" -> addressLine2,
    "addressLine3" -> addressLine3,
    "addressLine4" -> addressLine4,
    "postCode" -> postCode,
    "country" -> "GB"
  )

  val form = new AddressFormProvider(countryOptions)()

  "Address form" must {
    behave like questionForm(Address(
      addressLine1,
      addressLine2,
      Some(addressLine3),
      Some(addressLine4),
      Some(postCode),
      "GB"
    ))

    when(countryOptions.options).thenReturn(Seq(InputOption("United Arab Emirates", "country:AE")))

    behave like formWithCountry(
      form,
      "country",
      "error.country.invalid",
      "error.country.invalid",
      countryOptions,
      Map(
        "addressLine1" -> addressLine1,
        "addressLine2" -> addressLine2
      )
    )

    behave like formWithCountryAndPostCode(
      form,
      "error.postcode.required",
      "error.postcode.invalid",
      "error.postcode.nonUK.length",
      Map(
        "addressLine1" -> addressLine1,
        "addressLine2" -> addressLine2
      ),
      (address: Address) => address.postcode.getOrElse("")
    )

    "behave like a form with address lines" when {

      behave like formWithAddressField(
        form,
        "addressLine1",
        "error.address_line_1.required",
        "error.address_line_1.length",
        "error.address_line_1.invalid"
      )

      behave like formWithAddressField(
        form,
        "addressLine2",
        "error.address_line_2.required",
        "error.address_line_2.length",
        "error.address_line_2.invalid"
      )

      behave like formWithOptionalAddressField(
        form,
        "addressLine3",
        "error.address_line_3.length",
        "error.address_line_3.invalid",
        validData
      )

      behave like formWithOptionalAddressField(
        form,
        "addressLine4",
        "error.address_line_4.length",
        "error.address_line_4.invalid",
        validData
      )

    }

  }

}
