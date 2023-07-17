package antifraud.service

import antifraud.model.Privilege
import antifraud.model.Role
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import java.util.stream.Collectors

@Component
class SecurityUserDetailsService(
    private val userService: UserService,
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userService.findByUsername(username) ?: return org.springframework.security.core.userdetails.User(
            " ", " ", true, true, true, true, getAuthorities(listOf(Role(name = "ROLE_USER")))
        )
        return User(user.username,
            user.password,
            user.enabled,
            user.accountNonExpired,
            user.credentialsNonExpired,
            user.accountNonLocked,
            user.roles?.let { getAuthorities(it) }
        )
    }

    private fun getAuthorities(roles: Collection<Role>): MutableList<GrantedAuthority> {
        return getGrantedAuthorities(getPrivileges(roles))
    }

    private fun getPrivileges(roles: Collection<Role>): MutableList<String?> {
        val privileges: MutableList<String?> = mutableListOf()
        val collection: MutableList<Privilege> = mutableListOf()
        for (role in roles) {
            privileges.add(role.name)
            role.privileges?.let { collection.addAll(it) }
        }
        for (item in collection) {
            privileges.add(item.name)
        }
        return privileges
    }

    private fun getGrantedAuthorities(privileges: MutableList<String?>): MutableList<GrantedAuthority> {
        return privileges.stream().map { privilege: String? -> SimpleGrantedAuthority(privilege) }
            .collect(Collectors.toList())
    }
}


