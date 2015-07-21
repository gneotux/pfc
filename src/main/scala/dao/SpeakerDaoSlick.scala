package dao

import model.{ User, Speaker }
import utils.DatabaseConfig.profile.api._


/**
 * Created by gneotux on 16/07/15.
 */
trait SpeakerDao {

  def create

  def getAll: DBIO[Seq[Speaker]]

  def get(id: Int): DBIO[Option[Speaker]]

  def getUsersByActivityId(activityId: Int): DBIO[Seq[User]]

  def add(user: Speaker): DBIO[Int]

  def delete(id: Int): DBIO[Int]

  def deleteByUserAndActivityId(activityId: Int, userId: Int): DBIO[Int]
}

trait SpeakerDaoSlickImpl extends SpeakerDao {

  class Activities(tag: Tag) extends Table[Speaker](tag, "speakers") {

    def id: Rep[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def userId: Rep[Int] = column[Int]("user_id")

    def activityId: Rep[Int] = column[Int]("activity_id")

    def * = (id, userId, activityId) <>((Speaker.apply _).tupled, Speaker.unapply)
  }

  val speakers = TableQuery[Activities]

  override def create = speakers.schema.create

  override def getAll: DBIO[Seq[Speaker]] = speakers.result

  override def get(id: Int): DBIO[Option[Speaker]] = speakers.filter(_.id === id).result.headOption

  override def getUsersByActivityId(activityId: Int): DBIO[Seq[User]] =
    (for {
      speaker <- speakers if speaker.activityId === activityId
      users <- UserDao.users if users.id === speaker.userId
    } yield users).result

  override def add(speaker: Speaker): DBIO[Int] = {
    (speakers returning speakers.map(_.id)) += speaker
  }

  override def delete(id: Int): DBIO[Int] = speakers.filter(_.id === id).delete

  override def deleteByUserAndActivityId(userId: Int, activityId: Int): DBIO[Int] = {
    speakers.filter(speaker => speaker.userId === userId && speaker.activityId === activityId).delete
  }

}

object SpeakerDao extends SpeakerDaoSlickImpl