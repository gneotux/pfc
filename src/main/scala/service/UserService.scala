package service

import dao.{ PasswordDao, UserDao }
import model.{ User, UserPassword }
import router.dto.UserDto
import utils.DatabaseConfig._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scalaz.OptionT
import scalaz.OptionT._
import scalaz.std.scalaFuture._


trait UserService {

  def userDao: UserDao

  def passwordDao: PasswordDao

  def add(user: UserDto): Future[Option[User]]

  def getAll(): Future[Seq[User]]

  def get(id: Int): Future[Option[User]]

  def get(email: String): Future[Option[(User, UserPassword)]]

  def delete(id: Int):Future[Int]

  def update(id: Int, userDto: UserDto): Future[Option[User]]

  def populateUser: UserDto => User = (userDto: UserDto) =>
    User(
      0,
      userDto.email,
      userDto.firstName,
      userDto.lastName,
      userDto.twitterId,
      userDto.linkedinId,
      userDto.bio,
      userDto.permission
    )
}

object UserService extends UserService {

  override val userDao = UserDao

  override val passwordDao = PasswordDao

  override def add(user: UserDto): Future[Option[User]] = db.run {
    for {
      passwordId <- passwordDao.add(UserPassword newWithPassword user.password)
      userId <- userDao.add(populateUser(user).copy(passwordId = Some(passwordId)))
      // "This DBMS allows only a single AutoInc"
      // H2 doesn't allow return the whole user once created so we have to do this instead of returning the object from
      // the dao on inserting
      user <- UserDao.get(userId.getOrElse(-1))
    } yield user
  }

  override def getAll(): Future[Seq[User]] = db.run {
    userDao.getAll
  }

  override def get(id: Int): Future[Option[User]] = db.run {
    userDao.get(id)
  }

  override def get(email: String): Future[Option[(User, UserPassword)]] = db.run {
    userDao.get(email)
  }

  override def delete(id: Int):Future[Int] = db.run {
    userDao.delete(id)
  }

  override def update(id: Int, userDto: UserDto): Future[Option[User]] = {

    val toUpdate = populateUser(userDto).copy(id = id)

    val result = for {
      p <- optionT(db.run(userDao.get(id)))
      newPasswordId <- optionT(db.run(passwordDao.add(UserPassword newWithPassword userDto.password)).map(Option.apply))
      resultUpdate <- optionT(db.run(userDao.add(toUpdate.copy(passwordId = Option(newPasswordId)))).map(Option.apply))
      updated <- optionT(db.run(userDao.get(id)))
    } yield updated

    result.run
  }
}
