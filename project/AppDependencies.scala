import sbt._
import uk.gov.hmrc.SbtAutoBuildPlugin
import uk.gov.hmrc.sbtdistributables.SbtDistributablesPlugin
import uk.gov.hmrc.versioning.SbtGitVersioning

object AppDependencies {
  import play.core.PlayVersion

  val compile = Seq(
    play.sbt.PlayImport.ws,
    "org.reactivemongo"             %%  "play2-reactivemongo"            % "0.20.3-play26",
    "uk.gov.hmrc"                   %%  "logback-json-logger"            % "4.8.0",
    "uk.gov.hmrc"                   %%  "play-health"                    % "3.14.0-play-26",
    "uk.gov.hmrc"                   %%  "play-conditional-form-mapping"  % "1.3.0-play-26",
    "uk.gov.hmrc"                   %%  "bootstrap-play-26"              % "1.14.0",
    "uk.gov.hmrc"                   %%  "play-whitelist-filter"          % "3.4.0-play-26",
    "uk.gov.hmrc"                   %%  "play-nunjucks"                  % "0.23.0-play-26",
    "uk.gov.hmrc"                   %%  "play-nunjucks-viewmodel"        % "0.9.0-play-26",
    "org.webjars.npm"               %   "govuk-frontend"                 % "3.7.0",
    "org.webjars.npm"               %   "hmrc-frontend"                  % "1.15.1",
    "com.google.inject.extensions"  %   "guice-multibindings"            % "4.2.2",
    "uk.gov.hmrc"                   %%  "domain"                         % "5.9.0-play-26"
  )

  val test = Seq(
    "org.scalatest"               %% "scalatest"          % "3.0.7",
    "org.scalatestplus.play"      %% "scalatestplus-play" % "3.1.2",
    "org.pegdown"                 %  "pegdown"            % "1.6.0",
    "org.jsoup"                   %  "jsoup"              % "1.10.3",
    "com.typesafe.play"           %% "play-test"          % PlayVersion.current,
    "org.mockito"                 %  "mockito-all"        % "1.10.19",
    "org.scalacheck"              %% "scalacheck"         % "1.14.0",
    "com.github.tomakehurst"      % "wiremock-jre8"       % "2.21.0"
  ).map(_ % Test)

  def apply(): Seq[ModuleID] = compile ++ test
}
