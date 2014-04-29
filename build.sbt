import play.Project._
import Grunt._
import SimplePlayRunHook._

name := "uprint"

version := "2.0.1"

scalaVersion := "2.10.3"

lazy val uprintlib = project

lazy val uprintclient = project.dependsOn(uprintlib)

lazy val uprintadmin = project.settings(playScalaSettings: _*).dependsOn(uprintlib)

lazy val root = project.in(file(".")).aggregate(uprintlib,uprintclient,uprintadmin)
