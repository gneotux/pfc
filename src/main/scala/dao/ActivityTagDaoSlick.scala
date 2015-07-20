package dao

import model.{ ActivityTag, User }
import utils.DatabaseConfig.profile.api._


/**
 * Created by gneotux on 19/07/15.
 */
trait ActivityTagDao {

  def create

  def getAll: DBIO[Seq[ActivityTag]]

  def get(id: Int): DBIO[Option[ActivityTag]]

  def getUsersByActivityId(activityId: Int): DBIO[Seq[User]]

  def add(user: ActivityTag): DBIO[Int]

  def delete(id: Int): DBIO[Int]
}

trait ActivityTagDaoSlickImpl extends ActivityTagDao {

  class Activities(tag: Tag) extends Table[ActivityTag](tag, "activityTags") {

    def id: Rep[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def tagId: Rep[Int] = column[Int]("tag_id")

    def activityId: Rep[Int] = column[Int]("activity_id")

    def * = (id, tagId, activityId) <>((ActivityTag.apply _).tupled, ActivityTag.unapply)
  }

  val activityTags = TableQuery[Activities]

  override def create = activityTags.schema.create

  override def getAll: DBIO[Seq[ActivityTag]] = activityTags.result

  override def get(id: Int): DBIO[Option[ActivityTag]] = activityTags.filter(_.id === id).result.headOption

  override def getUsersByActivityId(activityId: Int): DBIO[Seq[User]] =
    (for {
      activityTag <- activityTags if activityTag.activityId === activityId
      user <- UserDao.users
    } yield user).result


  override def add(activityTag: ActivityTag): DBIO[Int] = {
    (activityTags returning activityTags.map(_.id)) += activityTag
  }

  override def delete(id: Int): DBIO[Int] = activityTags.filter(_.id === id).delete
}

object ActivityTagDao extends ActivityTagDaoSlickImpl