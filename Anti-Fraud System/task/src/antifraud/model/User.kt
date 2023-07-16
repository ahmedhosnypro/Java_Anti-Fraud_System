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

    @Column(name = "username") var username: String? = null,

    @Column(name = "password") var password: String? = null,

    @Column(name = "name") var name: String? = null,
)
