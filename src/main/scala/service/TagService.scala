package service

import dao.TagDao
import model.Tag
import router.dto.TagDto
import utils.DatabaseConfig._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scalaz.OptionT
import scalaz.OptionT._
import scalaz.std.scalaFuture._

/**
 * Created by gneotux on 17/07/15.
 */
trait TagService {

  def tagDao: TagDao

  def add(tag: TagDto): Future[Option[Tag]]

  def getAll(): Future[Seq[Tag]]

  def get(id: Int): Future[Option[Tag]]

  def delete(id: Int): Future[Int]

  def update(id: Int, tagDto: TagDto): Future[Option[Tag]]

  def populateTag: TagDto => Tag = (tagDto: TagDto) =>
    Tag(
      0,
      tagDto.name,
      tagDto.color,
      tagDto.shortName
    )
}

object TagService extends TagService {

  override val tagDao = TagDao

  override def add(tag: TagDto): Future[Option[Tag]] = db.run {
    for {
      tagId <- tagDao.add(populateTag(tag))
      tag <- TagDao.get(tagId.getOrElse(-1))
    } yield tag
  }

  override def getAll(): Future[Seq[Tag]] = db.run {
    tagDao.getAll
  }

  override def get(id: Int): Future[Option[Tag]] = db.run {
    tagDao.get(id)
  }

  override def delete(id: Int): Future[Int] = db.run {
    tagDao.delete(id)
  }

  override def update(id: Int, tagDto: TagDto): Future[Option[Tag]] = {

    val toUpdate = populateTag(tagDto).copy(id = id)

    val result = for {
      p <- optionT(db.run(tagDao.get(id)))
      resultUpdate <- optionT(db.run(tagDao.add(toUpdate)).map(Option.apply))
      updated <- optionT(db.run(tagDao.get(id)))
    } yield updated

    result.run
  }
}
