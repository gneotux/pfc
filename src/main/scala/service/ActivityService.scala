package service

import java.util.NoSuchElementException

import dao._
import model._
import router.dto.ActivityDto
import utils.DatabaseConfig._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.control.Exception

/**
 * Created by gneotux on 17/07/15.
 */
trait ActivityService {

  def activityDao: ActivityDao
  def atendeeDao: AtendeeDao
  def speakerDao: SpeakerDao
  def userDao: UserDao


  def add(activity: ActivityDto): Future[Option[Activity]]

  def addAtendee(activityId: Int, userId: Int): Future[Option[Atendee]]

  def addSpeaker(activityId: Int, userId: Int): Future[Option[Speaker]]

  def getAll(): Future[Seq[Activity]]

  def getAllAtendees(activityId: Int): Future[Seq[User]]

  def getAllSpeakers(activityId: Int): Future[Seq[User]]

  def deleteAtendee(activityId: Int, userId: Int): Future[Int]

  def deleteSpeaker(activityId: Int, userId: Int): Future[Int]

  def get(id: Int): Future[Option[Activity]]

  def delete(id: Int): Future[Int]

  def populateActivity: ActivityDto => Activity = (activityDto: ActivityDto) =>
    Activity(
      0,
      activityDto.eventId,
      activityDto.locationId,
      activityDto.activityTypeId,
      activityDto.title,
      activityDto.description,
      activityDto.objective,
      activityDto.startTime,
      activityDto.endTime,
      activityDto.resources
    )
}

object ActivityService extends ActivityService {

  override val activityDao = ActivityDao
  override val atendeeDao = AtendeeDao
  override val speakerDao = SpeakerDao
  override val userDao = UserDao


  override def add(activity: ActivityDto): Future[Option[Activity]] = db.run {
    for {
      activityId <- activityDao.add(populateActivity(activity))
      activity <- ActivityDao.get(activityId)
    } yield activity
  }

  override def addAtendee(activityId: Int, userId: Int): Future[Option[Atendee]] = db.run {
    for {
      activity <- activityDao.get(activityId)
//      _ = if (activity.isEmpty) throw new NoSuchElementException(s"Activity not found with activityId: ${activityId}")
      user <- userDao.get(userId)
//      _ = if (user.isEmpty) throw new NoSuchElementException(s"Atendee with userId: ${userId} not found")
      attendeeId <- atendeeDao.add(Atendee(0,userId,activityId))
      atendee <- atendeeDao.get(attendeeId)
    } yield atendee
  }

  override def addSpeaker(activityId: Int, userId: Int): Future[Option[Speaker]] = db.run {
    for {
      activity <- activityDao.get(activityId)
//      _ = if (activity.isEmpty) throw new NoSuchElementException(s"Activity not found with activityId: ${activityId}")
      user <- userDao.get(userId)
//      _ = if (user.isEmpty) throw new NoSuchElementException(s"Speaker with userId: ${userId} not found")
      attendeeId <- speakerDao.add(Speaker(0,userId,activityId))
      speaker <- speakerDao.get(attendeeId)
    } yield speaker
  }

  override def deleteAtendee(activityId: Int, userId: Int): Future[Int] = db.run {
    atendeeDao.deleteByUserAndActivityId(activityId, userId)
  }

  override def deleteSpeaker(activityId: Int, userId: Int): Future[Int] = db.run {
    speakerDao.deleteByUserAndActivityId(activityId, userId)
  }

  override def getAll(): Future[Seq[Activity]] = db.run {
    activityDao.getAll
  }

  override def getAllAtendees(activityId: Int): Future[Seq[User]] = db.run{
    atendeeDao.getUsersByActivityId(activityId)
  }

  override def getAllSpeakers(activityId: Int): Future[Seq[User]] = db.run{
    speakerDao.getUsersByActivityId(activityId)
  }

  override def get(id: Int): Future[Option[Activity]] = db.run {
    activityDao.get(id)
  }

  override def delete(id: Int): Future[Int] = db.run {
    activityDao.delete(id)
  }

}
