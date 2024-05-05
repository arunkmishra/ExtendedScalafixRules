lazy val V = _root_.scalafix.sbt.BuildInfo

inThisBuild(
  List(
    organization := "com.github.arun",
    homepage := Some(
      url("https://github.com/arunkmishra/ExtendedScalafixRules")
    ),
    licenses := List(
      "Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0")
    ),
    scalaVersion := V.scala213,
    crossScalaVersions := List(V.scala212, V.scala213),
    addCompilerPlugin(scalafixSemanticdb),
    scalacOptions ++= List("-Yrangepos", "-P:semanticdb:synthetics:on")
  )
)

lazy val skipPublishSettings = {
  (publish / skip) := true
}

lazy val rules = project.settings(
  moduleName := "scalafix-rules",
  libraryDependencies += "ch.epfl.scala" %% "scalafix-core" % V.scalafixVersion,
)

lazy val input = project.settings(skipPublishSettings)

lazy val output = project.settings(skipPublishSettings)

lazy val tests = project
  .settings(
    libraryDependencies += "ch.epfl.scala" % "scalafix-testkit" % V.scalafixVersion % Test cross CrossVersion.full,
    scalafixTestkitOutputSourceDirectories :=
      (output / Compile / unmanagedSourceDirectories).value,
    scalafixTestkitInputSourceDirectories :=
      (input / Compile / unmanagedSourceDirectories).value,
    scalafixTestkitInputClasspath :=
      (input / Compile / fullClasspath).value
  )
  .settings(skipPublishSettings)
  .dependsOn(input, rules)
  .enablePlugins(ScalafixTestkitPlugin)
