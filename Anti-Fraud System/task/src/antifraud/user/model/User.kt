package antifraud.user.model

import jakarta.persistence.*

/**
 * User entity
 *
 * @author Ahmed Hosny
 * @version 1.0
 * @since 2023-04-07
 */
@Entity(name = "users")
class User(
    @Id @GeneratedValue(strategy = GenerationType.AUTO) @Column(name = "id", nullable = false) var id: Long? = null,
    var username: String? = null,
    var password: String? = null,
    var name: String? = null,
    var enabled: Boolean = true,
    var accountNonExpired: Boolean = true,
    var accountNonLocked: Boolean = false,
    var credentialsNonExpired: Boolean = true,


    @ManyToMany(fetch = FetchType.EAGER) @JoinTable(
        name = "users_roles",
        joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "role_id", referencedColumnName = "id")],
    ) var roles: MutableSet<Role>? = null,
)