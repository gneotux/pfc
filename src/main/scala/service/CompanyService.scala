package service

import dao.{ CompanyDao, PasswordDao }
import model.Company
import router.dto.CompanyDto
import utils.DatabaseConfig._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


trait CompanyService {

  def companyDao: CompanyDao

  def add(company: CompanyDto): Future[Option[Company]]

  def getAll(): Future[Seq[Company]]

  def get(id: Int): Future[Option[Company]]
  
  def delete(id: Int):Future[Int]

  def populateCompany: CompanyDto => Company = (companyDto: CompanyDto) =>
    Company(
      0,
      companyDto.name,
      companyDto.email,
      companyDto.phone,
      companyDto.description,
      companyDto.website,
      companyDto.logoUrl
    )
}

object CompanyService extends CompanyService {

  override val companyDao = CompanyDao

  override def add(company: CompanyDto): Future[Option[Company]] = db.run {
    for {
      companyId <- companyDao.add(populateCompany(company))
      company <- CompanyDao.get(companyId)
    } yield company
  }

  override def getAll(): Future[Seq[Company]] = db.run {
    companyDao.getAll
  }

  override def get(id: Int): Future[Option[Company]] = db.run {
    companyDao.get(id)
  }

  override def delete(id: Int):Future[Int] = db.run {
    companyDao.delete(id)
  }
}
