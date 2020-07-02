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

package forms.mappings

import java.time.LocalDate

import play.api.data.validation.Constraint
import play.api.data.validation.Invalid
import play.api.data.validation.Valid
import uk.gov.hmrc.domain.Nino

trait Constraints {
  private val regexPostcode = """^[A-Z]{1,2}[0-9][0-9A-Z]?\s?[0-9][A-Z]{2}$"""
  lazy val nameRegex: String = """^[a-zA-Z &`\-\'\.^]*$"""
  private val regexCrn = "^[A-Za-z0-9 -]{8}$"
  val addressLineRegex = """^[A-Za-z0-9 \-,.&'\/]{1,35}$"""

  protected def firstError[A](constraints: Constraint[A]*): Constraint[A] =
    Constraint {
      input =>
        constraints
          .map(_.apply(input))
          .find(_ != Valid)
          .getOrElse(Valid)
    }

  protected def postCode(errorKey: String): Constraint[String] = regexp(regexPostcode, errorKey)

  protected def minimumValue[A](minimum: A, errorKey: String)(implicit ev: Ordering[A]): Constraint[A] =
    Constraint {
      input =>

        import ev._

        if (input >= minimum) {
          Valid
        } else {
          Invalid(errorKey, minimum)
        }
    }

  protected def minimumValueOption[A](minimum: A, errorKey: String)(implicit ev: Ordering[A]): Constraint[Option[A]] =
    Constraint {
      case Some(input) =>
        import ev._

        if (input >= minimum) {
          Valid
        } else {
          Invalid(errorKey, minimum)
        }
      case None =>
        Valid
    }

  protected def maximumValue[A](maximum: A, errorKey: String)(implicit ev: Ordering[A]): Constraint[A] =
    Constraint {
      input =>

        import ev._

        if (input <= maximum) {
          Valid
        } else {
          Invalid(errorKey, maximum)
        }
    }

  protected def maximumValueOption[A](minimum: A, errorKey: String)(implicit ev: Ordering[A]): Constraint[Option[A]] =
    Constraint {
      case Some(input) =>
        import ev._

        if (input <= minimum) {
          Valid
        } else {
          Invalid(errorKey, minimum)
        }
      case None =>
        Valid
    }

  protected def inRange[A](minimum: A, maximum: A, errorKey: String)(implicit ev: Ordering[A]): Constraint[A] =
    Constraint {
      input =>

        import ev._

        if (input >= minimum && input <= maximum) {
          Valid
        } else {
          Invalid(errorKey, minimum, maximum)
        }
    }

  protected def regexp(regex: String, errorKey: String): Constraint[String] =
    Constraint {
      case str if str.matches(regex) =>
        Valid
      case _ =>
        Invalid(errorKey, regex)
    }

  protected def maxLength(maximum: Int, errorKey: String): Constraint[String] =
    Constraint {
      case str if str.length <= maximum =>
        Valid
      case _ =>
        Invalid(errorKey, maximum)
    }

  protected def optionalMaxLength(maximum: Int, errorKey: String): Constraint[Option[String]] =
    Constraint {
      case Some(str) if str.length <= maximum =>
        Valid
      case None => Valid
      case _ =>
        Invalid(errorKey, maximum)
    }

  protected def exactLength(length: Int, errorKey: String): Constraint[String] =
    Constraint {
      case str if str.length == length =>
        Valid
      case _ =>
        Invalid(errorKey, length)
    }

  protected def maxDate(maximum: LocalDate, errorKey: String, args: Any*): Constraint[LocalDate] =
    Constraint {
      case date if date.isAfter(maximum) =>
        Invalid(errorKey, args: _*)
      case _ =>
        Valid
    }

  protected def minDate(minimum: LocalDate, errorKey: String, args: Any*): Constraint[LocalDate] =
    Constraint {
      case date if date.isBefore(minimum) =>
        Invalid(errorKey, args: _*)
      case _ =>
        Valid
    }

  //protected def futureDate(invalidKey: String): Constraint[LocalDate] =
  //  Constraint {
  //    case date if date.isAfter(DateHelper.today) => {
  //      Invalid(invalidKey)
  //    }
  //    case _ => Valid
  //  }

  protected def yearHas4Digits(errorKey: String): Constraint[LocalDate] =
    Constraint {
      case date if date.getYear >= 1000 => Valid
      case _ => Invalid(errorKey)
    }

  protected def nonEmptySet(errorKey: String): Constraint[Set[_]] =
    Constraint {
      case set if set.nonEmpty =>
        Valid
      case _ =>
        Invalid(errorKey)
    }

  protected def validNino(invalidKey: String): Constraint[String] =
    Constraint {
      case nino if Nino.isValid(nino) => Valid
      case _ => Invalid(invalidKey)
    }

  protected def validCrn(invalidKey: String): Constraint[String] =
    Constraint {
      case crn if crn.matches(regexCrn) => Valid
      case _ => Invalid(invalidKey)
    }

  protected def validAddressLine(invalidKey: String): Constraint[String] = regexp(addressLineRegex, invalidKey)
  protected def optionalValidAddressLine(invalidKey: String): Constraint[Option[String]] = Constraint {
    case Some(str) if str.matches(addressLineRegex) =>
      Valid
    case None => Valid
    case _ =>
      Invalid(invalidKey, addressLineRegex)
  }
}