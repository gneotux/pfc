package dao

import model.Company
import utils.DatabaseConfig.profile.api._


trait CompanyDao {

  def create

  def getAll: DBIO[Seq[Company]]

  def get(id: Int): DBIO[Option[Company]]

  def add(user: Company): DBIO[Option[Int]]

  def delete(id: Int): DBIO[Int]
}

trait CompanyDaoSlickImpl extends CompanyDao {

  class Companies(tag: Tag) extends Table[Company](tag, "companies") {

    def id: Rep[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def name: Rep[String] = column[String]("name")

    def email: Rep[String] = column[String]("email")

    def phone: Rep[Option[String]] = column[Option[String]]("phone")

    def description: Rep[Option[String]] = column[Option[String]]("description")

    def website: Rep[Option[String]] = column[Option[String]]("website")

    def logoUrl: Rep[Option[String]] = column[Option[String]]("logo_url")

    def * = (id, name, email, phone, description, website, logoUrl) <>((Company.apply _).tupled, Company.unapply)
  }

  val companies = TableQuery[Companies]

  override def create = companies.schema.create

  override def getAll: DBIO[Seq[Company]] = companies.result

  override def get(id: Int): DBIO[Option[Company]] = companies.filter(_.id === id).result.headOption

  override def add(user: Company): DBIO[Option[Int]] = {
    (companies returning companies.map(_.id)) insertOrUpdate  user
  }

  override def delete(id: Int): DBIO[Int] = companies.filter(_.id === id).delete
}

object CompanyDao extends CompanyDaoSlickImpl
