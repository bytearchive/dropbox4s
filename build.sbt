name := "dropbox4s"

organization := "com.github.Shinsuke-Abe"

scalaVersion := "2.11.2"

crossScalaVersions := Seq("2.10.1", "2.11.2")

libraryDependencies ++= Seq(
  "org.specs2" %% "specs2" % "2.4.2" % "test",
  "org.mockito" % "mockito-core" % "1.9.5" % "test",
  "net.databinder.dispatch" %% "dispatch-core" % "0.11.0",
  "org.json4s" %% "json4s-native" % "3.2.10",
  "com.dropbox.core" % "dropbox-core-sdk" % "[1.7,1.8)",
  "commons-codec" % "commons-codec" % "1.9"
)

scalacOptions in Test ++= Seq("-Yrangepos")

// Read here for optional dependencies:
// http://etorreborre.github.io/specs2/guide/org.specs2.guide.Runners.html#Dependencies

resolvers ++= Seq("snapshots", "releases").map(Resolver.sonatypeRepo)

licenses += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0"))

initialCommands := "import dropbox4s._"
