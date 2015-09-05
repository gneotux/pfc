package dao

import model.{ Attendee, Attendee$, User }
import utils.DatabaseConfig.profile.api._


/**
 * Created by gneotux on 16/07/15.
 */
trait AttendeeDao {

  def create

  def getAll: DBIO[Seq[Attendee]]

  def get(id: Int): DBIO[Option[Attendee]]

  def getUsersByActivityId(activityId: Int): DBIO[Seq[User]]

  def add(user: Attendee): DBIO[Int]

  def delete(id: Int): DBIO[Int]

  def deleteByUserAndActivityId(activityId: Int, userId: Int): DBIO[Int]


}

trait AttendeeDaoSlickImpl extends AttendeeDao {

  class Activities(tag: Tag) extends Table[Attendee](tag, "atendees") {

    def id: Rep[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def userId: Rep[Int] = column[Int]("user_id")

    def activityId: Rep[Int] = column[Int]("activity_id")

    def * = (id, userId, activityId) <>((Attendee.apply _).tupled, Attendee.unapply)
  }

  val attendees = TableQuery[Activities]

  override def create = attendees.schema.create

  override def getAll: DBIO[Seq[Attendee]] = attendees.result

  override def get(id: Int): DBIO[Option[Attendee]] = attendees.filter(_.id === id).result.headOption

  override def getUsersByActivityId(activityId: Int): DBIO[Seq[User]] =
    (for {
      atendee <- attendees if atendee.activityId === activityId
      users <- UserDao.users if users.id === atendee.userId
    } yield users).result


  override def add(attendee: Attendee): DBIO[Int] = {
    (attendees returning attendees.map(_.id)) += attendee
  }

  override def delete(id: Int): DBIO[Int] = attendees.filter(_.id === id).delete

  override def deleteByUserAndActivityId(userId: Int, activityId: Int): DBIO[Int] = {
    attendees.filter(attendee => attendee.userId === userId && attendee.activityId === activityId).delete
  }

}

object AttendeeDao extends AttendeeDaoSlickImpl