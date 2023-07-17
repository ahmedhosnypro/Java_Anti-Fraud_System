package antifraud.service

import antifraud.security.SecurityConfig
import antifraud.model.Role
import antifraud.model.User
import antifraud.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.jvm.optionals.getOrNull

@Service
@Transactional
class UserService(
    private val userRepository: UserRepository,
)  {
     fun save(user: User): User {
        user.password = SecurityConfig.bCryptPasswordEncoder().encode(user.password)
        return userRepository.save(user)
    }

     fun existByUsername(username: String) = userRepository.existsByUsername(username)
     fun listUsers(): MutableList<User> = userRepository.findAll()

    @Transactional
     fun deleteUser(username: String): Unit? =
        findByUsername(username)?.let { userRepository.delete(it) }

     fun count() = userRepository.count()

    @Transactional
     fun setRole(user: User, role: Role) {
        if (user.roles.isNullOrEmpty()) {
            user.roles = mutableSetOf()
        }
        user.roles = mutableSetOf(role)
        userRepository.save(user)
    }

    @Transactional
     fun setAccess(user: User, nonLocked: Boolean) {
        user.accountNonLocked = nonLocked
        userRepository.save(user)
    }

     fun findByUsername(username: String) = userRepository.findByUsername(username).getOrNull()
}