package dao

import model.{ Tag => EventTag }
import utils.DatabaseConfig.profile.api._

/**
 * Created by gneotux on 19/07/15.
 */
trait TagDao {

  def create

  def getAll: DBIO[Seq[EventTag]]

  def get(id: Int): DBIO[Option[EventTag]]

  def add(tag: EventTag): DBIO[Int]

  def delete(id: Int): DBIO[Int]
}

trait TagDaoSlickImpl extends TagDao {

  class EventTags(tag: Tag) extends Table[EventTag](tag, "tags") {

    def id: Rep[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def name: Rep[String] =  column[String]("name")

    def color: Rep[Option[String]] = column[Option[String]]("color")

    def shortName: Rep[Option[String]] = column[Option[String]]("short_name")

    def * = (id, name, color, shortName) <>((EventTag.apply _).tupled, EventTag.unapply)
  }

  val tags = TableQuery[EventTags]

  override def create = tags.schema.create

  override def getAll: DBIO[Seq[EventTag]] = tags.result

  override def get(id: Int): DBIO[Option[EventTag]] = tags.filter(_.id === id).result.headOption

  override def add(tag: EventTag): DBIO[Int] = {
    (tags returning tags.map(_.id)) += tag
  }

  override def delete(id: Int): DBIO[Int] = tags.filter(_.id === id).delete
}

object TagDao extends TagDaoSlickImpl
