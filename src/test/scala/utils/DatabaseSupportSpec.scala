package utils

import dao._
import model._
import org.joda.time.DateTime
import org.joda.time.format.{ DateTimeFormatter, DateTimeFormat }
import org.specs2.mutable.Specification
import org.specs2.specification.BeforeAfterEach
import slick.jdbc.meta.MTable
import utils.DatabaseConfig._
import utils.DatabaseConfig.profile.api._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object DatabaseSupportSpec {

  val formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss");
  val now = formatter.parseDateTime("22/07/2015 21:51:00");


  lazy val passwords = Seq(
    UserPassword(1, Some("$2a$10$U3gBQ50FY5qiQ5XeQKgWwO6AADKjaGqh/6l3RzWitAWelWCQxffUC"), "$2a$10$U3gBQ50FY5qiQ5XeQKgWwO"),
    UserPassword(2, Some("$2a$10$U3gBQ50FY5qiQ5XeQKgWwO6AADKjaGqh/6l3RzWitAWelWCQxffUC"), "$2a$10$U3gBQ50FY5qiQ5XeQKgWwO"),
    UserPassword(3, Some("$2a$10$U3gBQ50FY5qiQ5XeQKgWwO6AADKjaGqh/6l3RzWitAWelWCQxffUC"), "$2a$10$U3gBQ50FY5qiQ5XeQKgWwO")
  )
  lazy val users = Seq(
    User(1, "test1@test.com", Some("name1"), Some("surname1"), None, None, None, "ADMIN",Some(1)),
    User(2, "test2@test.com", Some("name2"), Some("surname2"), None, None, None, "USER",Some(2)),
    User(3, "test3@test.com", Some("name3"), Some("surname3"), None, None, None, "USER",Some(3))
  )
  
  lazy val activities = Seq(
    Activity(1, 1, 1, 1, Some("Welcome talk"), Some("This is the meeting and greetings for the scala days"), Some("Meeting and saying hellos"), now, now, Some("github/gneotux")),
    Activity(2, 1, 2, 5, Some("Is Java Alive?"), Some("Do we need Java anymore?"), Some("Discuss about the future of Scala in the JVM"), now, now, Some("github/gneotux"))
  )
  
  lazy val events = Seq(
    Event(1, "ScalaDays", Some("The most important event in the scala world"), Some("www.scaladays.com"), Some("#scaladays"), Some("http://scaladays.jpg")),
    Event(2, "ScalaWorld", Some("The second most important event in the scala world"), Some("www.scala-world.com"), Some("#scalaworld"), Some("http://scalaworld.jpg")),
    Event(3, "E3", Some("The event for the videogames lovers"), Some("www.e3.com"), Some("#e3"), Some("http://e3.jpg"))
  )

  lazy val eventDays = Seq(
    EventDay(1, 1, now, now),
    EventDay(2, 1, now, now),
    EventDay(3, 1, now, now),
    EventDay(4, 2, now, now),
    EventDay(5, 2, now, now),
    EventDay(6, 2, now, now),
    EventDay(7, 3, now, now),
    EventDay(8, 3, now, now),
    EventDay(9, 3, now, now)
  )
  
  lazy val locations = Seq(
    Location(1, "Auditorium UC3M", Some("ROOM2"), 1231231111.22312D, 12312333123.123123D, 200, Some("University Carlos III space for acts"), Some("www.uc3m.es")),
    Location(2, "Leganes F.C. Stadium", Some("ROOM$"), 1231231111.22312D, 12312333123.123123D, 2000, Some("Leganes football club stadium"), Some("www.leganes.es"))
  )
  
  lazy val activityTypes = Seq(
    ActivityType(1, "Presentation"),
    ActivityType(2, "Webinar"),
    ActivityType(3, "Code kata"),
    ActivityType(4, "Workshop"),
    ActivityType(5, "Discussion")
  )

  lazy val tags = Seq(
    Tag(1, "introductory", Some("yellow"), Some("INTRO")),
    Tag(2, "beginner/amateur", Some("green"), Some("BEGINNER")),
    Tag(3, "advanced users", Some("red"), Some("ADVANCE"))
  )

  lazy val activityTags = Seq(
    ActivityTag(1, 1, 1),
    ActivityTag(2, 2, 1),
    ActivityTag(3, 3, 1)
  )
  
  lazy val atendees = Seq(
    Atendee(1, 2 , 1)
  )

  lazy val speakers = Seq(
    Speaker(1, 1 , 1)
  )
  
  lazy val companies = Seq(
    Company(1, "rrhh@commodityvectors.com", Some("+341234567"), Some("We do some cool stuff"), Some("www.commodityvectors.com"), Some("commodityvectors.com/logo.jpg")),
    Company(2, "rrhh@genscape.com", Some("+341234567"), Some("We do some cool stuff too"), Some("www.genscape.com"), Some("genscpae.com/logo.jpg"))
  )

  lazy val sponsors = Seq(
    Sponsor(1, 1, 1)
  )
}

trait SpecSupport extends Specification with BeforeAfterEach {

  def createSchema = {
    val dropAll = (
      PasswordDao.passwords.schema ++
      UserDao.users.schema ++
      ActivityDao.activities.schema ++
      EventDao.events.schema ++
      EventDayDao.eventDays.schema ++
      LocationDao.locations.schema ++
      ActivityTypeDao.activityTypes.schema ++
      TagDao.tags.schema ++
      ActivityTagDao.activityTags.schema ++
      AtendeeDao.atendees.schema ++
      SpeakerDao.speakers.schema ++
      CompanyDao.companies.schema ++
      SponsorDao.sponsors.schema
    ).drop

    val createAll =
      DBIO.seq(
        (PasswordDao.passwords.schema ++
          UserDao.users.schema ++
          ActivityDao.activities.schema ++
          EventDao.events.schema ++
          EventDayDao.eventDays.schema ++
          LocationDao.locations.schema ++
          ActivityTypeDao.activityTypes.schema ++
          TagDao.tags.schema ++
          ActivityTagDao.activityTags.schema ++
          AtendeeDao.atendees.schema ++
          SpeakerDao.speakers.schema ++
          CompanyDao.companies.schema ++
          SponsorDao.sponsors.schema
          ).create,
        PasswordDao.passwords ++= DatabaseSupportSpec.passwords,
        UserDao.users ++= DatabaseSupportSpec.users,
        ActivityDao.activities ++= DatabaseSupportSpec.activities,
        EventDao.events ++= DatabaseSupportSpec.events,
        EventDayDao.eventDays ++= DatabaseSupportSpec.eventDays,
        LocationDao.locations ++= DatabaseSupportSpec.locations,
        ActivityTypeDao.activityTypes ++= DatabaseSupportSpec.activityTypes,
        TagDao.tags ++= DatabaseSupportSpec.tags,
        ActivityTagDao.activityTags ++= DatabaseSupportSpec.activityTags, 
        SpeakerDao.speakers ++= DatabaseSupportSpec.speakers,
        AtendeeDao.atendees ++= DatabaseSupportSpec.atendees,
        CompanyDao.companies ++= DatabaseSupportSpec.companies,
        SponsorDao.sponsors ++= DatabaseSupportSpec.sponsors
      )

    val results = db.run(MTable.getTables).flatMap {
      tables => if (tables.toList.size > 1) {
        db.run(dropAll).flatMap(_ => db.run(createAll))
      } else db.run(createAll)
    }

    Await.result(results, Duration.Inf)
  }

  override def before: Unit= {
    createSchema
  }

  override def after: Unit= { }
}

