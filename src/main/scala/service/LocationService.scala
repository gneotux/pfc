package service

import dao.LocationDao
import model.Location
import router.dto.LocationDto
import utils.DatabaseConfig._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * Created by gneotux on 18/07/15.
 */
trait LocationService {

  def locationDao: LocationDao

  def add(location: LocationDto): Future[Option[Location]]

  def getAll(): Future[Seq[Location]]

  def get(id: Int): Future[Option[Location]]

  def delete(id: Int): Future[Int]

  def populateLocation: LocationDto => Location = (locationDto: LocationDto) =>
    Location(
      0,
      locationDto.name,
      locationDto.code,
      locationDto.latitude,
      locationDto.longitude,
      locationDto.capacity,
      locationDto.description,
      locationDto.photoUrl
    )
}

object LocationService extends LocationService {

  override val locationDao = LocationDao

  override def add(location: LocationDto): Future[Option[Location]] = db.run {
    for {
      locationId <- locationDao.add(populateLocation(location))
      location <- LocationDao.get(locationId)
    } yield location
  }

  override def getAll(): Future[Seq[Location]] = db.run {
    locationDao.getAll
  }

  override def get(id: Int): Future[Option[Location]] = db.run {
    locationDao.get(id)
  }

  override def delete(id: Int): Future[Int] = db.run {
    locationDao.delete(id)
  }
}
