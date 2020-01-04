package freeapp

import java.util.UUID

import cats.data.EitherK
import cats.free.Free
import cats.{~>, Id}
import models.{PhoneNumber, User, UserId}

import scala.collection.mutable

object Main extends App {

  type FreeApp[T] = EitherK[StorageAlg, LoggingAlg, T]

  def program(implicit L: Logging[FreeApp], S: Storage[FreeApp]): Free[FreeApp, Unit] =
    for {
      _ <- L.info("Application started")
      phoneNumber = PhoneNumber(List(4, 5), List(1, 3, 3, 7, 4, 2, 0, 0))
      user        = User("John Doe", 42, phoneNumber)
      addedUser <- S.putUser(user)
      _         <- L.info(s"Added user: $addedUser")
    } yield ()

  val loggerInterpreter: LoggingAlg ~> cats.Id = new (LoggingAlg ~> cats.Id) {
    override def apply[A](fa: LoggingAlg[A]): Id[A] = fa match {
      case Info(msg)     => println(s"INFO: $msg")
      case Error(msg, t) => System.err.println(s"ERROR: $msg, ${t.mkString}")
    }
  }

  val storageInterpreter: StorageAlg ~> cats.Id = new (StorageAlg ~> cats.Id) {
    val users = mutable.HashMap[UserId, User]()
    override def apply[A](fa: StorageAlg[A]): Id[A] = fa match {
      case PutUser(u) =>
        val id = UserId(UUID.randomUUID())
        users.put(id, u)
        id
      case GetUser(id) =>
        users.get(id)
      case SetUser(id, u) =>
        users.put(id, u)
        u
    }
  }

  val interpreter: FreeApp ~> Id = storageInterpreter or loggerInterpreter

  program.foldMap(interpreter)
}
