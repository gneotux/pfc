package model


case class AuthInfo(user: User) {
  def hasPermissions(permission: String): Boolean = user.permission == permission
}
