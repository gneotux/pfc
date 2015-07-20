package service

import dao.EventDayDao
import model.EventDay
import router.dto.EventDayDto
import utils.DatabaseConfig._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * Created by gneotux on 20/07/15.
 */
trait EventDayService {

  def eventDayDao: EventDayDao

  def add(eventDay: EventDayDto): Future[Option[EventDay]]

  def getAll(): Future[Seq[EventDay]]

  def get(id: Int): Future[Option[EventDay]]

  def delete(id: Int): Future[Int]

  def populateEventDay: EventDayDto => EventDay = (eventDayDto: EventDayDto) =>
    EventDay(
      0,
      eventDayDto.eventId,
      eventDayDto.startTime,
      eventDayDto.endTime
    )
}

object EventDayService extends EventDayService {

  override val eventDayDao = EventDayDao

  override def add(eventDay: EventDayDto): Future[Option[EventDay]] = db.run {
    for {
      eventDayId <- eventDayDao.add(populateEventDay(eventDay))
      eventDay <- EventDayDao.get(eventDayId)
    } yield eventDay
  }

  override def getAll(): Future[Seq[EventDay]] = db.run {
    eventDayDao.getAll
  }

  override def get(id: Int): Future[Option[EventDay]] = db.run {
    eventDayDao.get(id)
  }

  override def delete(id: Int): Future[Int] = db.run {
    eventDayDao.delete(id)
  }
}
