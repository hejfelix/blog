import java.util.UUID

package object models {

  case class PhoneNumber(areaCode: List[Int], number: List[Int]) {
    def pretty = s"+${areaCode.mkString} ${number.mkString}"
  }

  case class User(name: String, age: Int, phoneNumber: PhoneNumber)
  case class UserId(id: UUID)

  sealed trait AppError
  final case class DuplicateEntryError(msg: String) extends AppError
  final case class FormatError(msg: String)         extends AppError

}
