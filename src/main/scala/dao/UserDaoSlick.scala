package dao

import model.{ User, UserPassword }
import utils.DatabaseConfig._
import utils.DatabaseConfig.profile.api._


trait UserDao {

  def create

  def getAll: DBIO[Seq[User]]

  def get(id: Int): DBIO[Option[User]]

  def get(email: String): DBIO[Option[(User, UserPassword)]]

  def add(user: User): DBIO[Option[Int]]

  def delete(id: Int): DBIO[Int]
}

trait UserDaoSlickImpl extends UserDao {

  class Users(tag: Tag) extends Table[User](tag, "users") {

    def id: Rep[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def email: Rep[String] = column[String]("email")

    def firstName: Rep[Option[String]] = column[Option[String]]("first_name")

    def lastName: Rep[Option[String]] = column[Option[String]]("last_name")

    def twitterId: Rep[Option[String]] = column[Option[String]]("twitter_id")

    def linkedinId: Rep[Option[String]] = column[Option[String]]("linkedin_id")

    def bio: Rep[Option[String]] = column[Option[String]]("bio")

    def permission: Rep[String] = column[String]("permission")

    def passwordId: Rep[Option[Int]] = column[Option[Int]]("password_id")

    def * = (id, email, firstName, lastName, twitterId, linkedinId, bio, permission, passwordId) <>((User.apply _).tupled, User.unapply)
  }

  val users = TableQuery[Users]

  override def create = users.schema.create

  override def getAll: DBIO[Seq[User]] = users.result

  override def get(id: Int): DBIO[Option[User]] = users.filter(_.id === id).result.headOption

  override def get(email: String): DBIO[Option[(User, UserPassword)]] =
    (for {
      user <- users.filter(_.email === email)
      password <- PasswordDao.passwords.filter(_.id === user.passwordId)
    } yield (user, password)).result.headOption

  override def add(user: User): DBIO[Option[Int]] = {
    (users returning users.map(_.id)) insertOrUpdate  user
  }

  override def delete(id: Int): DBIO[Int] = users.filter(_.id === id).delete
}

object UserDao extends UserDaoSlickImpl
