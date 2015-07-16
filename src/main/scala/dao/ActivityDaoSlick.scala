package dao

import model.Activity
import org.joda.time.DateTime
import utils.DatabaseConfig.profile.api._
import com.github.tototoshi.slick.JdbcJodaSupport._


/**
 * Created by gneotux on 16/07/15.
 */
trait ActivityDao {

  def create

  def getAll: DBIO[Seq[Activity]]

  def get(id: Int): DBIO[Option[Activity]]

  def add(user: Activity): DBIO[Int]

  def delete(id: Int): DBIO[Int]
}

trait ActivityDaoSlickImpl extends ActivityDao {

  class Activities(tag: Tag) extends Table[Activity](tag, "activities") {

    def id: Rep[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def eventId: Rep[Int] = column[Int]("event_id")

    def locationId: Rep[Int] = column[Int]("location_id")

    def activityTypeId: Rep[Int] = column[Int]("activity_type_id")

    def title: Rep[Option[String]] = column[Option[String]]("title")

    def description: Rep[Option[String]] = column[Option[String]]("description")

    def objective: Rep[Option[String]] = column[Option[String]]("objective")

    def startTime: Rep[DateTime] = column[DateTime]("start_time")

    def endTime: Rep[DateTime] = column[DateTime]("end_time")

    def resources: Rep[Option[String]] = column[Option[String]]("resources")
    
    def * = (id, eventId, locationId, activityTypeId, title, description, objective, startTime, endTime, resources) <>((Activity.apply _).tupled, Activity.unapply)
  }

  val activities = TableQuery[Activities]

  override def create = activities.schema.create

  override def getAll: DBIO[Seq[Activity]] = activities.result

  override def get(id: Int): DBIO[Option[Activity]] = activities.filter(_.id === id).result.headOption

  override def add(activity: Activity): DBIO[Int] = {
    (activities returning activities.map(_.id)) += activity
  }

  override def delete(id: Int): DBIO[Int] = activities.filter(_.id === id).delete
}

object ActivityDao extends ActivityDaoSlickImpl
