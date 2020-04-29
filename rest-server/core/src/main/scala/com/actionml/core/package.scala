package com.actionml

import com.actionml.core.validate.ValidateError
import com.typesafe.scalalogging.LazyLogging
import zio.{Has, ZIO, ZLayer}
import zio.clock.Clock
import zio.logging.{LogAnnotation, Logging}
import zio.logging.slf4j.Slf4jLogger
import zio.stream.ZStream

package object core  extends LazyLogging {

  def drawActionML(): Unit = {
    val actionML =
      """
        |
        |               _   _             __  __ _
        |     /\       | | (_)           |  \/  | |
        |    /  \   ___| |_ _  ___  _ __ | \  / | |
        |   / /\ \ / __| __| |/ _ \| '_ \| |\/| | |
        |  / ____ \ (__| |_| | (_) | | | | |  | | |____
        | /_/    \_\___|\__|_|\___/|_| |_|_|  |_|______|
        |
        |    _    _
        |   | |  | |
        |   | |__| | __ _ _ __ _ __   ___  ___ ___
        |   |  __  |/ _` | '__| '_ \ / _ \/ __/ __|
        |   | |  | | (_| | |  | | | |  __/\__ \__ \
        |   |_|  |_|\__,_|_|  |_| |_|\___||___/___/
        |
        |
      """.stripMargin

    logger.info(actionML)
  }

  def drawInfo(title: String, dataMap: Seq[(String, Any)]): Unit = {
    val leftAlignFormat = "║ %-40s%-38s ║"

    val line = "═" * 80

    val preparedTitle = "║ %-78s ║".format(title)
    val data = dataMap.map {
      case (key, value) =>
        leftAlignFormat.format(key, value)
    } mkString "\n"

    logger.info(
      s"""
         |╔$line╗
         |$preparedTitle
         |$data
         |╚$line╝
         |""".stripMargin)

  }

  case class BadParamsException(message: String) extends Exception(message)

  type HEnv = Clock with Logging
  type HIO[A] = ZIO[HEnv, ValidateError, A]
  type HStream[A] = ZStream[HEnv, ValidateError, A]

  val harnessRuntime = zio.Runtime.unsafeFromLayer {
    Slf4jLogger.make((c, s) => s) ++
    Clock.live
  }
}
