package service

import dao.{ LocationDao, EventDao, PasswordDao, ActivityDao }
import model.{ UserPassword, Activity }
import router.dto.ActivityDto
import utils.DatabaseConfig._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * Created by gneotux on 17/07/15.
 */
trait ActivityService {

  def activityDao: ActivityDao

  def add(activity: ActivityDto): Future[Option[Activity]]

  def getAll(): Future[Seq[Activity]]

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

  override def add(activity: ActivityDto): Future[Option[Activity]] = db.run {
    for {
      activityId <- activityDao.add(populateActivity(activity))
      activity <- ActivityDao.get(activityId)
    } yield activity
  }

  override def getAll(): Future[Seq[Activity]] = db.run {
    activityDao.getAll
  }

  override def get(id: Int): Future[Option[Activity]] = db.run {
    activityDao.get(id)
  }

  override def delete(id: Int): Future[Int] = db.run {
    activityDao.delete(id)
  }
}
