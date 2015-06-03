package com.grandata.commons.config

import java.util.Date

import com.typesafe.config.ConfigFactory
import java.io.File


trait Config {
  val applicationName: String

  def customer: String


  private val homeDir = System.getProperty("user.home")

  private lazy val devApplicationConf = ConfigFactory.load("application")
  private lazy val appConfigFile =
    devApplicationConf.getString(s"${applicationName}.application.app-config-path")

  lazy val userApp = ConfigFactory.parseFile(new File(homeDir + "/" + appConfigFile))
  lazy val application = userApp.withFallback(devApplicationConf)


  def buildInputPath(date: Date, `type`: String, pattern: String = "*"): String

  def buildInputPath(`type`: String, pattern: String): String

  def buildOutputPath(date: Date, `type`: String, dir: String = ""): String

  def buildSingleOutputPath(date: Date, `type`: String, dir: String = ""): String
}
