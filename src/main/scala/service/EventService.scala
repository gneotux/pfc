package service

import dao.{ CompanyDao, SponsorDao, EventDao }
import model.{ Company, Sponsor, Event }
import router.dto.EventDto
import utils.DatabaseConfig._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * Created by gneotux on 17/07/15.
 */
trait EventService {

  def eventDao: EventDao

  def sponsorDao: SponsorDao

  def companyDao: CompanyDao

  def add(event: EventDto): Future[Option[Event]]

  def getAll(): Future[Seq[Event]]

  def get(id: Int): Future[Option[Event]]

  def delete(id: Int): Future[Int]

  def getAllSponsorsByEventId(eventId : Int): Future[Seq[Company]]

  def addSponsor(eventId: Int, companyId: Int): Future[Option[Sponsor]]

  def deleteSponsor(eventId: Int, companyId: Int): Future[Int]

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
  override val sponsorDao = SponsorDao
  override val companyDao = CompanyDao


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

  override def getAllSponsorsByEventId(eventId : Int): Future[Seq[Company]] = db.run {
    sponsorDao.getCompaniesByEventId(eventId)
  }


  override def addSponsor(eventId: Int, companyId: Int): Future[Option[Sponsor]] = db.run {
    for {
      activity <- eventDao.get(eventId)
      //      _ = if (activity.isEmpty) throw new NoSuchElementException(s"Activity not found with activityId: ${activityId}")
      user <- companyDao.get(companyId)
      //      _ = if (user.isEmpty) throw new NoSuchElementException(s"Atendee with userId: ${userId} not found")
      sponsorId <- sponsorDao.add(Sponsor(0,companyId,eventId))
      sponsor <- sponsorDao.get(sponsorId)
    } yield sponsor
  }

  override def deleteSponsor(eventId: Int, companyId: Int): Future[Int] = db.run {
    sponsorDao.deleteByEventIdAndCompanyId(eventId, companyId)
  }
}
