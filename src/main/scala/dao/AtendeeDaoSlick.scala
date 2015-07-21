package dao

import model.{ Atendee, User }
import utils.DatabaseConfig.profile.api._


/**
 * Created by gneotux on 16/07/15.
 */
trait AtendeeDao {

  def create

  def getAll: DBIO[Seq[Atendee]]

  def get(id: Int): DBIO[Option[Atendee]]

  def getUsersByActivityId(activityId: Int): DBIO[Seq[User]]

  def add(user: Atendee): DBIO[Int]

  def delete(id: Int): DBIO[Int]

  def deleteByUserAndActivityId(activityId: Int, userId: Int): DBIO[Int]


}

trait AtendeeDaoSlickImpl extends AtendeeDao {

  class Activities(tag: Tag) extends Table[Atendee](tag, "atendees") {

    def id: Rep[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def userId: Rep[Int] = column[Int]("user_id")

    def activityId: Rep[Int] = column[Int]("activity_id")

    def * = (id, userId, activityId) <>((Atendee.apply _).tupled, Atendee.unapply)
  }

  val atendees = TableQuery[Activities]

  override def create = atendees.schema.create

  override def getAll: DBIO[Seq[Atendee]] = atendees.result

  override def get(id: Int): DBIO[Option[Atendee]] = atendees.filter(_.id === id).result.headOption

  override def getUsersByActivityId(activityId: Int): DBIO[Seq[User]] =
    (for {
      atendee <- atendees if atendee.activityId === activityId
      users <- UserDao.users if users.id === atendee.userId
    } yield users).result


  override def add(atendee: Atendee): DBIO[Int] = {
    (atendees returning atendees.map(_.id)) += atendee
  }

  override def delete(id: Int): DBIO[Int] = atendees.filter(_.id === id).delete

  override def deleteByUserAndActivityId(userId: Int, activityId: Int): DBIO[Int] = {
    atendees.filter(atendee => atendee.userId === userId && atendee.activityId === activityId).delete
  }

}

object AtendeeDao extends AtendeeDaoSlickImpl