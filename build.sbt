// Project name (artifact name in Maven)
name := "corenlpserver"

// orgnization name (e.g., the package name of the project)
organization := "com.redcatlabs"

version := "0.1-SNAPSHOT"

// project description
description := "Stanford CoreNLP Java Server"

// Enables publishing to maven repo
publishMavenStyle := true

// Do not append Scala versions to the generated artifacts
crossPaths := false

// This forbids including Scala related libraries into the dependency
autoScalaLibrary := false

// library dependencies. (orginization name) % (project name) % (version)

// Old, irrelevant entries from another build.sbt -- delete soon
libraryDependencies ++= Seq(
//   "org.apache.commons" % "commons-math3" % "3.1.1",
//   "org.fluentd" % "fluent-logger" % "0.2.10",
//   "org.mockito" % "mockito-core" % "1.9.5" % "test"  // Test-only dependency
)

// These are for the SparkJava REST API piece
libraryDependencies ++= Seq(
   "com.sparkjava" % "spark-core" % "2.2"
)

// This is for when we need more explicit control over json serialization 
libraryDependencies ++= Seq(
   "com.google.code.gson" % "gson" % "2.3.1"
)

// These are for the CoreNLP piece
//   See : https:/ /github.com/sistanlp/processors/blob/master/build.sbt 
libraryDependencies ++= Seq(
   "edu.stanford.nlp" % "stanford-corenlp" % "3.5.2",
   "edu.stanford.nlp" % "stanford-corenlp" % "3.5.2" classifier "models"
//   "edu.stanford.nlp" % "stanford-corenlp" % "3.5.2" classifier "models-chinese"
//   "edu.stanford.nlp" % "stanford-corenlp" % "3.5.2" classifier "models-german"
//   "edu.stanford.nlp" % "stanford-corenlp" % "3.5.2" classifier "models-spanish"
)

