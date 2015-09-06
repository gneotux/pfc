package dao

import com.github.tototoshi.slick.JdbcJodaSupport._
import com.wordnik.swagger.annotations.ApiModelProperty
import model.ActivityType
import org.joda.time.DateTime
import utils.DatabaseConfig.profile.api._

import scala.annotation.meta.field

/**
 * Created by gneotux on 19/07/15.
 */
trait ActivityTypeDao {

  def create

  def getAll: DBIO[Seq[ActivityType]]

  def get(id: Int): DBIO[Option[ActivityType]]

  def add(activityType: ActivityType): DBIO[Option[Int]]

  def delete(id: Int): DBIO[Int]
}

trait ActivityTypeDaoSlickImpl extends ActivityTypeDao {

  class ActivityTypes(tag: Tag) extends Table[ActivityType](tag, "activityTypes") {

    def id: Rep[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def name: Rep[String] =  column[String]("name")

    def * = (id, name) <>((ActivityType.apply _).tupled, ActivityType.unapply)
  }

  val activityTypes = TableQuery[ActivityTypes]

  override def create = activityTypes.schema.create

  override def getAll: DBIO[Seq[ActivityType]] = activityTypes.result

  override def get(id: Int): DBIO[Option[ActivityType]] = activityTypes.filter(_.id === id).result.headOption

  override def add(activityType: ActivityType): DBIO[Option[Int]] = {
    (activityTypes returning activityTypes.map(_.id)) insertOrUpdate activityType
  }

  override def delete(id: Int): DBIO[Int] = activityTypes.filter(_.id === id).delete
}

object ActivityTypeDao extends ActivityTypeDaoSlickImpl
