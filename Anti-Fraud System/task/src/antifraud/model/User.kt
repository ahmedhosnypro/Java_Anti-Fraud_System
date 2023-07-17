package antifraud.model

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
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id", nullable = false) var id: Long? = null,
    @Column(name = "username", nullable = false) var username: String? = null,
    @Column(name = "password", nullable = false) var password: String? = null,
    @Column(name = "name", nullable = false) var name: String? = null,
    @Column(name = "enabled", nullable = false) var enabled: Boolean = true,
    @Column(name = "accountNonExpired", nullable = false) var accountNonExpired: Boolean = true,
    @Column(name = "accountNonLocked", nullable = false) var accountNonLocked: Boolean = false,
    @Column(name = "credentialsNonExpired", nullable = false) var credentialsNonExpired: Boolean = true,


    @ManyToMany(fetch = FetchType.EAGER) @JoinTable(
        name = "users_roles",
        joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "role_id", referencedColumnName = "id")],
    ) var roles: MutableSet<Role>? = null,

    //todo solve issue of timestamp with transactional annotation methods
//    @CreationTimestamp @Temporal(TemporalType.TIMESTAMP)  @Column(name = "created_at", nullable = false) var createdAt: Timestamp? = null,
//    @UpdateTimestamp @Temporal(TemporalType.TIMESTAMP)   @Column(name = "updated_at", nullable = false) var updatedAt: Timestamp? = null,
)

