package com.grandata.commons.config

import java.io.File

import com.typesafe.config.ConfigFactory

trait Config {
  val applicationName: String

  private val homeDir = System.getProperty("user.home")

  private lazy val devApplicationConf = ConfigFactory.load("application")
  private lazy val appConfigFile =
    devApplicationConf.getString(s"${applicationName}.application.app-config-path")

  lazy val userApp = ConfigFactory.parseFile(new File(homeDir + "/" + appConfigFile))
  lazy val application = userApp.withFallback(devApplicationConf)
}