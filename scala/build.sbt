val commonSettings =
  Seq(name := "scala", version := "0.1", scalaVersion := "2.13.1")

val Versions = new {
  val cats = "2.1.0"
}

lazy val models = project
  .in(file("models"))
  .settings(commonSettings)

lazy val free = project
  .in(file("free"))
  .settings(
    commonSettings,
    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-core" % Versions.cats,
      "org.typelevel" %% "cats-free" % Versions.cats
    ),
    addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.11.0" cross CrossVersion.full)
  )
  .dependsOn(models)
