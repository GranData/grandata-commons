package com.grandata.commons.config

import com.typesafe.config.ConfigFactory
import java.io.File


trait Config {
  val applicationName: String

  private val homeDir = System.getProperty("user.home")

  private val devApplicationConf = ConfigFactory.load("application")
  private val appConfigFile =
    devApplicationConf.getString(s"${applicationName}.application.app-config-path")

  val userApp = ConfigFactory.parseFile(new File(homeDir + "/" + appConfigFile))
  val application = userApp.withFallback(devApplicationConf)
}
