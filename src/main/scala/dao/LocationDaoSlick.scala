package dao

import model.Location
import utils.DatabaseConfig.profile.api._

/**
 * Created by gneotux on 17/07/15.
 */
trait LocationDao {

  def create

  def getAll: DBIO[Seq[Location]]

  def get(id: Int): DBIO[Option[Location]]

  def add(location: Location): DBIO[Int]

  def delete(id: Int): DBIO[Int]
}

trait LocationDaoSlickImpl extends LocationDao {

  class Locations(tag: Tag) extends Table[Location](tag, "locations") {

    def id: Rep[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def name: Rep[String] =  column[String]("name")

    def code: Rep[Option[String]] = column[Option[String]]("code")

    def latitude: Rep[Double] =  column[Double]("latitude")

    def longitude: Rep[Double] =  column[Double]("longitude")

    def capacity: Rep[Int] =  column[Int]("capacity")

    def description: Rep[Option[String]] = column[Option[String]]("description")

    def photoUrl: Rep[Option[String]] = column[Option[String]]("photo_url")

    def * = (id, name, code, latitude, longitude, capacity, description, photoUrl) <>((Location.apply _).tupled, Location.unapply)
  }

  val locations = TableQuery[Locations]

  override def create = locations.schema.create

  override def getAll: DBIO[Seq[Location]] = locations.result

  override def get(id: Int): DBIO[Option[Location]] = locations.filter(_.id === id).result.headOption

  override def add(location: Location): DBIO[Int] = {
    (locations returning locations.map(_.id)) += location
  }

  override def delete(id: Int): DBIO[Int] = locations.filter(_.id === id).delete
}

object LocationDao extends LocationDaoSlickImpl
