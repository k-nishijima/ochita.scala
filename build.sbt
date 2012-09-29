import AssemblyKeys._

name := "ochita"

version := "1.0"

scalaVersion := "2.9.2"

resolvers += "twitter4j.org" at "http://twitter4j.org/maven2"

libraryDependencies += "org.twitter4j" % "twitter4j-core" % "[2.2,)"

libraryDependencies += "com.h2database" % "h2" % "[1.3,)"

libraryDependencies += "com.github.seratch" %% "scalikejdbc" % "[1.3,)"

libraryDependencies += "org.slf4j" % "slf4j-simple" % "1.6.6"

scalacOptions ++= Seq("-encoding", "UTF-8")

assemblySettings

