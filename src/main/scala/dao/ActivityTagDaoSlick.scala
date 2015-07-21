package dao

import model.{ Tag => ActTag, ActivityTag, User }
import utils.DatabaseConfig.profile.api._


/**
 * Created by gneotux on 19/07/15.
 */
trait ActivityTagDao {

  def create

  def getAll: DBIO[Seq[ActivityTag]]

  def get(id: Int): DBIO[Option[ActivityTag]]

  def getTagsByActivityId(activityId: Int): DBIO[Seq[ActTag]]

  def add(user: ActivityTag): DBIO[Int]

  def delete(id: Int): DBIO[Int]

  def deleteByActivityIdAndTagId(activityId: Int, tagId: Int): DBIO[Int]
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

  override def getTagsByActivityId(activityId: Int): DBIO[Seq[ActTag]] =
    (for {
      activityTag <- activityTags if activityTag.activityId === activityId
      tag <- TagDao.tags if activityTag.tagId === tag.id
    } yield tag).result


  override def add(activityTag: ActivityTag): DBIO[Int] = {
    (activityTags returning activityTags.map(_.id)) += activityTag
  }

  override def delete(id: Int): DBIO[Int] = activityTags.filter(_.id === id).delete

  override def deleteByActivityIdAndTagId(activityId: Int, tagId: Int): DBIO[Int] =
    activityTags.filter(activityTag => activityTag.activityId === activityId && activityTag.tagId === tagId).delete

}

object ActivityTagDao extends ActivityTagDaoSlickImpl