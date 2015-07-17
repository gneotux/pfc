package dao

import com.github.tototoshi.slick.JdbcJodaSupport._
import com.wordnik.swagger.annotations.ApiModelProperty
import model.Event
import org.joda.time.DateTime
import utils.DatabaseConfig.profile.api._

import scala.annotation.meta.field

/**
 * Created by gneotux on 17/07/15.
 */
trait EventDao {

  def create

  def getAll: DBIO[Seq[Event]]

  def get(id: Int): DBIO[Option[Event]]

  def add(event: Event): DBIO[Int]

  def delete(id: Int): DBIO[Int]
}

trait EventDaoSlickImpl extends EventDao {

  class Activities(tag: Tag) extends Table[Event](tag, "events") {

    def id: Rep[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def name: Rep[String] =  column[String]("name")

    def description: Rep[Option[String]] = column[Option[String]]("description")

    def website: Rep[Option[String]] = column[Option[String]]("website")

    def twitterHashtag: Rep[Option[String]] = column[Option[String]]("twitter_hashtag")

    def logoUrl: Rep[Option[String]] = column[Option[String]]("logo_url")

    def * = (id, name, description, website, twitterHashtag, logoUrl) <>((Event.apply _).tupled, Event.unapply)
  }

  val events = TableQuery[Activities]

  override def create = events.schema.create

  override def getAll: DBIO[Seq[Event]] = events.result

  override def get(id: Int): DBIO[Option[Event]] = events.filter(_.id === id).result.headOption

  override def add(event: Event): DBIO[Int] = {
    (events returning events.map(_.id)) += event
  }

  override def delete(id: Int): DBIO[Int] = events.filter(_.id === id).delete
}

object EventDao extends EventDaoSlickImpl
