package dao

import model.{ Company, Sponsor, User }
import utils.DatabaseConfig.profile.api._


/**
 * Created by gneotux on 19/07/15.
 */
trait SponsorDao {

  def create

  def getAll: DBIO[Seq[Sponsor]]

  def get(id: Int): DBIO[Option[Sponsor]]

  def getCompaniesByEventId(eventId: Int): DBIO[Seq[Company]]

  def add(user: Sponsor): DBIO[Int]

  def delete(id: Int): DBIO[Int]

  def deleteByEventIdAndCompanyId(eventId: Int, companyId: Int): DBIO[Int]
}

trait SponsorDaoSlickImpl extends SponsorDao {

  class Sponsors(tag: Tag) extends Table[Sponsor](tag, "sponsors") {

    def id: Rep[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def companyId: Rep[Int] = column[Int]("company_id")

    def eventId: Rep[Int] = column[Int]("event_id")

    def * = (id, companyId, eventId) <>((Sponsor.apply _).tupled, Sponsor.unapply)
  }

  val sponsors = TableQuery[Sponsors]

  override def create = sponsors.schema.create

  override def getAll: DBIO[Seq[Sponsor]] = sponsors.result

  override def get(id: Int): DBIO[Option[Sponsor]] = sponsors.filter(_.id === id).result.headOption

  override def getCompaniesByEventId(eventId: Int): DBIO[Seq[Company]] =
    (for {
      sponsors <- sponsors
      companies <- CompanyDao.companies if sponsors.companyId === companies.id
    } yield companies).result


  override def add(sponsor: Sponsor): DBIO[Int] = {
    (sponsors returning sponsors.map(_.id)) += sponsor
  }

  override def delete(id: Int): DBIO[Int] = sponsors.filter(_.id === id).delete

  override def deleteByEventIdAndCompanyId(eventId: Int, companyId: Int): DBIO[Int] =
    sponsors.filter(sponsor => sponsor.eventId === eventId && sponsor.companyId === companyId).delete
}

object SponsorDao extends SponsorDaoSlickImpl