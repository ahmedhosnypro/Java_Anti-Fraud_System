package antifraud.user.service

import antifraud.user.model.Role
import antifraud.user.model.User
import java.util.*

interface UserService {
    fun save(user: User): User
    fun existByUsername(username: String): Boolean
    fun findByUsername(username: String): User?
    fun listUsers(): MutableList<User>
    fun deleteUser(username: String): Unit?
    fun setRole(user: User, role: Role)
    fun setAccess(user: User, nonLocked: Boolean)
    fun count() : Long
}