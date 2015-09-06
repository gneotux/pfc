package service

import dao._
import model._
import org.joda.time.DateTime
import router.dto.ActivityDto
import utils.DatabaseConfig._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scalaz.OptionT
import scalaz.OptionT._
import scalaz.std.scalaFuture._

/**
 * Created by gneotux on 17/07/15.
 */
trait ActivityService {

  def activityDao: ActivityDao
  def attendeeDao: AttendeeDao
  def speakerDao: SpeakerDao
  def userDao: UserDao
  def activityTagDao: ActivityTagDao
  def tagDao: TagDao


  def add(activity: ActivityDto): Future[Option[Activity]]

  def addAttendee(activityId: Int, userId: Int): Future[Option[Attendee]]

  def addSpeaker(activityId: Int, userId: Int): Future[Option[Speaker]]

  def getAll(): Future[Seq[Activity]]

  def getAllAttendees(activityId: Int): Future[Seq[User]]

  def getAllSpeakers(activityId: Int): Future[Seq[User]]

  def deleteAttendee(activityId: Int, userId: Int): Future[Int]

  def deleteSpeaker(activityId: Int, userId: Int): Future[Int]

  def get(id: Int): Future[Option[Activity]]

  def delete(id: Int): Future[Int]

  def update(id: Int, activityDto: ActivityDto): Future[Option[Activity]]

  def getAllTags(activityId: Int): Future[Seq[Tag]]

  def addTag(activityId: Int, tagId: Int): Future[Option[ActivityTag]]

  def deleteTag(activityId: Int, tagId: Int): Future[Int]

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
  override val attendeeDao = AttendeeDao
  override val speakerDao = SpeakerDao
  override val userDao = UserDao
  override val activityTagDao = ActivityTagDao
  override val tagDao = TagDao


  override def add(activity: ActivityDto): Future[Option[Activity]] = db.run {
    for {
      activityId <- activityDao.add(populateActivity(activity))
      activity <- ActivityDao.get(activityId.getOrElse(-1))
    } yield activity
  }

  override def addAttendee(activityId: Int, userId: Int): Future[Option[Attendee]] = {
    (for {
      activity <- optionT(db.run(activityDao.get(activityId)))
//      _ = if (activity.isEmpty) throw new NoSuchElementException(s"Activity not found with activityId: ${activityId}")
      user <- optionT(db.run(userDao.get(userId)))
//      _ = if (user.isEmpty) throw new NoSuchElementException(s"Atendee with userId: ${userId} not found")
      attendeeId <- optionT(db.run(attendeeDao.add(Attendee(0,userId,activityId))).map(Option.apply))
      attendee <- optionT(db.run(attendeeDao.get(attendeeId)))
    } yield attendee).run
  }

  override def addSpeaker(activityId: Int, userId: Int): Future[Option[Speaker]] = {
    (for {
      activity <- optionT(db.run(activityDao.get(activityId)))
//      _ = if (activity.isEmpty) throw new NoSuchElementException(s"Activity not found with activityId: ${activityId}")
      user <- optionT(db.run(userDao.get(userId)))
//      _ = if (user.isEmpty) throw new NoSuchElementException(s"Speaker with userId: ${userId} not found")
      attendeeId <- optionT(db.run(speakerDao.add(Speaker(0,userId,activityId)).map(Option.apply)))
      speaker <- optionT(db.run(speakerDao.get(attendeeId)))
    } yield speaker).run
  }

  override def deleteAttendee(activityId: Int, userId: Int): Future[Int] = db.run {
    attendeeDao.deleteByUserAndActivityId(userId, activityId)
  }

  override def deleteSpeaker(activityId: Int, userId: Int): Future[Int] = db.run {
    speakerDao.deleteByUserAndActivityId(userId, activityId)
  }

  override def getAll(): Future[Seq[Activity]] = db.run {
    activityDao.getAll
  }

  override def getAllAttendees(activityId: Int): Future[Seq[User]] = db.run{
    attendeeDao.getUsersByActivityId(activityId)
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

  override def update(id: Int, activityDto: ActivityDto): Future[Option[Activity]] = {

    val toUpdate = populateActivity(activityDto).copy(id = id)

    val result = for {
      p <- optionT(db.run(activityDao.get(id)))
      resultUpdate <- optionT(db.run(activityDao.add(toUpdate)))
      updated <- optionT(db.run(activityDao.get(id)))
    } yield updated

    result.run
  }

  override def getAllTags(activityId: Int): Future[Seq[Tag]] = db.run{
    activityTagDao.getTagsByActivityId(activityId)
  }


  override def addTag(activityId: Int, tagId: Int): Future[Option[ActivityTag]] = {
    (for {
      activity <- optionT(db.run(activityDao.get(activityId)))
      //      _ = if (activity.isEmpty) throw new NoSuchElementException(s"Activity not found with activityId: ${activityId}")
      tag <- optionT(db.run(tagDao.get(tagId)))
      //      _ = if (user.isEmpty) throw new NoSuchElementException(s"Speaker with userId: ${userId} not found")
      activityTagId <- optionT(db.run(activityTagDao.add(ActivityTag(0,tagId,activityId)).map(Option.apply)))
      activityTag <- optionT(db.run(activityTagDao.get(activityTagId)))
    } yield activityTag).run
  }

  override def deleteTag(activityId: Int, tagId: Int): Future[Int] = db.run {
    activityTagDao.deleteByActivityIdAndTagId(activityId, tagId)
  }

}
