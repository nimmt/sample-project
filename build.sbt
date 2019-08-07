lazy val akkaHttpVersion = "10.1.8"
lazy val akkaVersion     = "2.5.23"
lazy val slickVersion    = "3.3.2"

// Slick document: https://github.com/slick/slick/tree/master/doc/src
// https://github.com/slick/slick/blob/master/doc/src/database.md
lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization    := "info.nimmt",
      scalaVersion    := "2.13.0"
    )),
    name := "My Akka HTTP Project",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http"            % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-xml"        % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-actor"           % akkaVersion,
      "com.typesafe.akka" %% "akka-stream"          % akkaVersion,
      "com.typesafe.slick" %% "slick"               % slickVersion,
      "org.slf4j"          % "slf4j-nop"            % "1.7.26",
      "com.typesafe.slick" %% "slick-hikaricp"      % slickVersion,
      "mysql"              % "mysql-connector-java" % "6.0.6",

      "com.typesafe.akka" %% "akka-http-testkit"    % akkaHttpVersion % Test,
      "com.typesafe.akka" %% "akka-testkit"         % akkaVersion     % Test,
      "com.typesafe.akka" %% "akka-stream-testkit"  % akkaVersion     % Test,
      "org.scalatest"     %% "scalatest"            % "3.0.8"         % Test,
      "org.scalacheck"    %% "scalacheck"           % "1.14.0"        % Test
    )
  )
