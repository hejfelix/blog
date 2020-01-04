package freeapp

import cats.InjectK
import cats.free.Free
import cats.free.Free.inject

sealed trait LoggingAlg[T]
final case class Info(msg: String)                                extends LoggingAlg[Unit]
final case class Error(msg: String, throwable: Option[Throwable]) extends LoggingAlg[Unit]

object Logging {
  implicit def logging[F[_]: InjectK[LoggingAlg, *[_]]]: Logging[F] = new Logging[F]
}
class Logging[F[_]: InjectK[LoggingAlg, *[_]]] {
  type Logging[T] = Free[F, T]
  def info(msg: String): Logging[Unit]                                = inject[LoggingAlg, F](Info(msg))
  def error(msg: String, throwable: Option[Throwable]): Logging[Unit] = inject[LoggingAlg, F](Error(msg, throwable))
}
