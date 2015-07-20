package service

import dao.ActivityTypeDao
import model.ActivityType
import router.dto.ActivityTypeDto
import utils.DatabaseConfig._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * Created by gneotux on 20/07/15.
 */
trait ActivityTypeService {

  def activityTypeDao: ActivityTypeDao

  def add(activityType: ActivityTypeDto): Future[Option[ActivityType]]

  def getAll(): Future[Seq[ActivityType]]

  def get(id: Int): Future[Option[ActivityType]]

  def delete(id: Int): Future[Int]

  def populateActivityType: ActivityTypeDto => ActivityType = (activityTypeDto: ActivityTypeDto) =>
    ActivityType(
      0,
      activityTypeDto.description
    )
}

object ActivityTypeService extends ActivityTypeService {

  override val activityTypeDao = ActivityTypeDao

  override def add(activityType: ActivityTypeDto): Future[Option[ActivityType]] = db.run {
    for {
      activityTypeId <- activityTypeDao.add(populateActivityType(activityType))
      activityType <- ActivityTypeDao.get(activityTypeId)
    } yield activityType
  }

  override def getAll(): Future[Seq[ActivityType]] = db.run {
    activityTypeDao.getAll
  }

  override def get(id: Int): Future[Option[ActivityType]] = db.run {
    activityTypeDao.get(id)
  }

  override def delete(id: Int): Future[Int] = db.run {
    activityTypeDao.delete(id)
  }
}
