package antifraud.service

import antifraud.model.User
import antifraud.repository.UserRepository
import antifraud.security.SecurityUserDetails
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component

@Component
class SecurityUserDetailsService(
    private val userRepository: UserRepository,
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        userRepository.findByUsername(username).apply { if (isPresent) return SecurityUserDetails(get()) }
        throw UsernameNotFoundException("User not found")
    }

    fun save(user: User): User {
        user.password = BCryptPasswordEncoder().encode(user.password)
        return userRepository.save(user)
    }

    fun existByUsername(username: String) = userRepository.existsByUsername(username)
    fun listUsers(): MutableList<User> = userRepository.findAll()
    fun deleteUser(username: String) =
        userRepository.findByUsername(username).apply { if (isPresent) userRepository.delete(get()) }
}