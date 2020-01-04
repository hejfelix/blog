package freeapp

import cats.InjectK
import cats.free.Free
import cats.free.Free.inject
import models.{User, UserId}

sealed trait StorageAlg[T]
case class PutUser(u: User)             extends StorageAlg[UserId]
case class GetUser(id: UserId)          extends StorageAlg[Option[User]]
case class SetUser(id: UserId, u: User) extends StorageAlg[User]

object Storage {
  implicit def storage[F[_]: InjectK[StorageAlg, *[_]]]: Storage[F] = new Storage[F]
}
class Storage[F[_]: InjectK[StorageAlg, *[_]]] {
  type Storage[T] = Free[F, T]
  def putUser(u: User): Storage[UserId]           = inject[StorageAlg, F](PutUser(u))
  def getUser(id: UserId): Storage[Option[User]]  = inject[StorageAlg, F](GetUser(id))
  def setUser(id: UserId, u: User): Storage[User] = inject[StorageAlg, F](SetUser(id, u))

}
