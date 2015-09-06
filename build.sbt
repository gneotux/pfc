import NativePackagerKeys._

packageArchetype.java_application

version       := "0.1"

scalaVersion  := "2.11.6"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

scalacOptions in Test ++= Seq("-Yrangepos")

libraryDependencies ++= {
  val akkaVersion = "2.3.6"
  val sprayVersion = "1.3.2"
  Seq(
    "io.spray"             %%   "spray-can"         % sprayVersion,
    "io.spray"             %%   "spray-routing"     % sprayVersion,
    "io.spray"             %%   "spray-json"        % sprayVersion,
    "io.spray"             %%   "spray-testkit"     % sprayVersion  % Test excludeAll(ExclusionRule("org.specs2")),
    "com.typesafe.akka"    %%   "akka-testkit"      % akkaVersion   % Test excludeAll(ExclusionRule("org.specs2")),
    "org.scalatest"        %%   "scalatest"         % "2.2.1"       % Test excludeAll(ExclusionRule("org.specs2")),
    "org.specs2"           %%   "specs2-core"       % "2.3.13-scalaz-7.1.0-RC1" % Test,
    "org.specs2"           %%   "specs2-junit"      % "2.3.13-scalaz-7.1.0-RC1" % Test,
    "org.specs2"           %%   "specs2-mock"       % "2.3.13-scalaz-7.1.0-RC1" % Test,
    "org.scalaz"           %%   "scalaz-core"       % "7.1.0",
    "com.typesafe.akka"    %%   "akka-actor"        % akkaVersion,
    "com.typesafe.slick"   %%   "slick"             % "3.0.0",
    "com.github.tototoshi" %%   "slick-joda-mapper" % "2.0.0",
    "org.slf4j"             %   "slf4j-nop"         % "1.7.7",
    "org.slf4j"             %   "slf4j-api"         % "1.7.7",
    "postgresql"            %   "postgresql"        % "9.1-901.jdbc4",
    "com.h2database"        %   "h2"                % "1.4.182",
    "org.joda"              %   "joda-convert"      % "1.7",
    "com.gettyimages"      %%   "spray-swagger"     % "0.5.0",
    "org.webjars"           %   "swagger-ui"        % "2.0.12",
    "com.github.t3hnar"    %%   "scala-bcrypt"      % "2.4",
    "org.mindrot"           %   "jbcrypt"           % "0.3m",
    "joda-time"             %   "joda-time"         % "2.7",
    "com.typesafe"          %   "config"            % "1.2.1"
  )
}


fork in Test := false

parallelExecution in Test := false

Revolver.settings

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"