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

package audit

import config.FrontendAppConfig
import models.requests.UserType
import org.mockito.ArgumentCaptor
import org.mockito.Matchers.any
import org.mockito.Mockito.{times, when, verify}
import org.scalatest._
import org.scalatestplus.mockito.MockitoSugar
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.mvc.AnyContentAsEmpty
import play.api.test.FakeRequest
import uk.gov.hmrc.play.audit.http.connector.AuditConnector
import uk.gov.hmrc.play.audit.http.connector.AuditResult.Success
import uk.gov.hmrc.play.audit.model.DataEvent

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class AuditServiceSpec extends WordSpec with MustMatchers with MockitoSugar with Inside {

  private val mockAuditConnector = mock[AuditConnector]
  private val app = new GuiceApplicationBuilder()
    .overrides(
      bind[AuditConnector].toInstance(mockAuditConnector)
    )
    .build()

  private val auditService = app.injector.instanceOf[AuditService]

  private def config: FrontendAppConfig = app.injector.instanceOf[FrontendAppConfig]

  "AuditService" must {
    "sent audit event" in {
      val aftAuditEvent = PSPStartEvent(UserType.Organisation, existingUser = false)
      val templateCaptor = ArgumentCaptor.forClass(classOf[DataEvent])

      implicit val request: FakeRequest[AnyContentAsEmpty.type] = FakeRequest("", "")

      when(mockAuditConnector.sendEvent(any())(any(), any())).thenReturn(Future(Success))

      auditService.sendEvent(aftAuditEvent)

      verify(mockAuditConnector, times(1)).sendEvent(templateCaptor.capture())
      inside(templateCaptor.getValue) {
        case DataEvent(auditSource, auditType, _, _, detail, _) =>
          auditSource mustBe config.appName
          auditType mustBe "PSPStartNew"
          detail mustBe Map("userType" -> "Organisation", "existingUser" -> "false")
      }
      app.stop()
    }
  }
}
