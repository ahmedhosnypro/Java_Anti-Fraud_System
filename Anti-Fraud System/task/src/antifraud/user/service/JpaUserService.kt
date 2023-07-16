package antifraud.user.service

import antifraud.security.SecurityConfig
import antifraud.user.model.Role
import antifraud.user.model.User
import antifraud.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.jvm.optionals.getOrNull

@Service
@Transactional
open class JpaUserService(
    private val userRepository: UserRepository,
) : UserService {
    override fun save(user: User): User {
        user.password = SecurityConfig.bCryptPasswordEncoder().encode(user.password)
        return userRepository.save(user)
    }

    override fun existByUsername(username: String) = userRepository.existsByUsername(username)
    override fun listUsers(): MutableList<User> = userRepository.findAll()

    @Transactional
    override fun deleteUser(username: String): Unit? =
        findByUsername(username)?.let { userRepository.delete(it) }

    override fun count() = userRepository.count()

    @Transactional
    override fun setRole(user: User, role: Role) {
        if (user.roles.isNullOrEmpty()) {
            user.roles = mutableSetOf()
        }
        user.roles = mutableSetOf(role)
        userRepository.save(user)
    }

    @Transactional
    override fun setAccess(user: User, nonLocked: Boolean) {
        user.accountNonLocked = nonLocked
        userRepository.save(user)
    }

    override fun findByUsername(username: String) = userRepository.findByUsername(username).getOrNull()
}