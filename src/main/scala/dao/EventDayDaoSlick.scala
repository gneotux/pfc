package dao

import model.EventDay
import org.joda.time.DateTime
import utils.DatabaseConfig.profile.api._


/**
 * Created by gneotux on 19/07/15.
 */
trait EventDayDao {

  def create

  def getAll: DBIO[Seq[EventDay]]

  def get(id: Int): DBIO[Option[EventDay]]

  def add(user: EventDay): DBIO[Int]

  def delete(id: Int): DBIO[Int]
}

trait EventDayDaoSlickImpl extends EventDayDao {

  class EventDays(tag: Tag) extends Table[EventDay](tag, "eventDays") {

    def id: Rep[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def eventId: Rep[Int] = column[Int]("event_id")

    def startTime: Rep[DateTime] = column[DateTime]("start_time")

    def endTime: Rep[DateTime] = column[DateTime]("end_time")

    def * = (id, eventId, startTime, endTime) <>((EventDay.apply _).tupled, EventDay.unapply)
  }

  val eventDays = TableQuery[EventDays]

  override def create = eventDays.schema.create

  override def getAll: DBIO[Seq[EventDay]] = eventDays.result

  override def get(id: Int): DBIO[Option[EventDay]] = eventDays.filter(_.id === id).result.headOption

  override def add(eventDay: EventDay): DBIO[Int] = {
    (eventDays returning eventDays.map(_.id)) += eventDay
  }

  override def delete(id: Int): DBIO[Int] = eventDays.filter(_.id === id).delete
}

object EventDayDao extends EventDayDaoSlickImpl