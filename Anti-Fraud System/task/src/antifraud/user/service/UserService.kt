package antifraud.user.service

import antifraud.security.SecurityConfig
import antifraud.user.model.Role
import antifraud.user.model.User
import antifraud.user.repository.RoleRepository
import antifraud.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.jvm.optionals.getOrNull

@Service
class UserService(
    private val userRepository: UserRepository, private val roleRepository: RoleRepository,
) {
    fun save(user: User): User {
        user.password = SecurityConfig.bCryptPasswordEncoder().encode(user.password)
        return userRepository.save(user)
    }

    fun existByUsername(username: String) = userRepository.existsByUsername(username)
    fun listUsers(): MutableList<User> = userRepository.findAll()
    fun deleteUser(username: String) =
        userRepository.findByUsername(username).apply { if (isPresent) userRepository.delete(get()) }

    fun count() = userRepository.count()

    fun setRole(user: User, role: Role) {
        if (user.roles.isNullOrEmpty()) {
            user.roles = mutableSetOf()
        }
        user.roles = mutableSetOf(role)
        userRepository.save(user)
    }

    fun setAccess(user: User, nonLocked: Boolean) {
        user.accountNonLocked = nonLocked
        userRepository.save(user)
    }

    fun findByUsername(username: String) = userRepository.findByUsername(username).getOrNull()
}