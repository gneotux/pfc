package utils

import utils.Config.dbConfig._
import slick.driver.{ H2Driver, JdbcProfile, PostgresDriver }
import slick.jdbc.JdbcBackend._
import java.net.URI


//Based on play-slick driver loader
object DatabaseConfig {

  val systemUrl: Option[String] = if (test) None else Option(System.getenv("DATABASE_URL"))

  lazy val db: Database = systemUrl.fold[Database](Database.forURL(url, user, password, driver = driver)) {
    uri =>
      
      val systemUrl: Option[(String, String, String)] = for {
        dbUri <- Option(new URI(uri))
        userInfo = dbUri.getUserInfo().split(":").lift
        user <- userInfo(0)
        password <- userInfo(1)
        host <- Option(dbUri.getHost)
        port <- Option(dbUri.getPort)
        databaseName <- Option(dbUri.getPath)
      } yield (s"jdbc:postgresql://$host:$port$databaseName", user, password)

      val tup = systemUrl.getOrElse(throw new Exception("FAILING GETTING DATABASE_URL"))

      Database.forURL(tup._1, tup._2, tup._3, driver = driver)
  }
  lazy val profile: JdbcProfile = driver match {
    case "org.postgresql.Driver" => PostgresDriver
    case "org.h2.Driver" => H2Driver
  }
}
