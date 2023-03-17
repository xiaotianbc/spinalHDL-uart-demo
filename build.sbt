name := "hello_world"
version := "0.1"
scalaVersion := "2.11.12"
fork := true
libraryDependencies ++= Seq(
  "com.github.spinalhdl" % "spinalhdl-core_2.11" % "1.8.0b",
  "com.github.spinalhdl" % "spinalhdl-lib_2.11" % "1.8.0b",
  compilerPlugin("com.github.spinalhdl" % "spinalhdl-idsl-plugin_2.11" % "1.8.0b")
)