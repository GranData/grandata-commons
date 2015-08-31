package com.grandata.commons.config

import java.io.File

import com.typesafe.config.ConfigFactory

trait Config {
  val applicationName: String
  
  lazy val applicationConf = ConfigFactory.load.withFallback(ConfigFactory.load("application"))
}