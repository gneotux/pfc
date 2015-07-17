package service

import dao.EventDao
import model.Event
import router.dto.EventDto
import utils.DatabaseConfig._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * Created by gneotux on 17/07/15.
 */
trait EventService {

  def eventDao: EventDao

  def add(event: EventDto): Future[Option[Event]]

  def getAll(): Future[Seq[Event]]

  def get(id: Int): Future[Option[Event]]

  def delete(id: Int): Future[Int]

  def populateEvent: EventDto => Event = (eventDto: EventDto) =>
    Event(
      0,
      eventDto.name,
      eventDto.description,
      eventDto.website,
      eventDto.twitterHashtag,
      eventDto.logoUrl
    )
}

object EventService extends EventService {

  override val eventDao = EventDao

  override def add(event: EventDto): Future[Option[Event]] = db.run {
    for {
      eventId <- eventDao.add(populateEvent(event))
      event <- EventDao.get(eventId)
    } yield event
  }

  override def getAll(): Future[Seq[Event]] = db.run {
    eventDao.getAll
  }

  override def get(id: Int): Future[Option[Event]] = db.run {
    eventDao.get(id)
  }

  override def delete(id: Int): Future[Int] = db.run {
    eventDao.delete(id)
  }
}
