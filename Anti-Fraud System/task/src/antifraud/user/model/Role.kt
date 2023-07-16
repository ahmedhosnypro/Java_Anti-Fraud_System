package antifraud.user.model

import jakarta.persistence.*


@Entity
class Role(
    @Id @GeneratedValue(strategy = GenerationType.AUTO) var id: Long? = null,

    var name: String? = null,

    @ManyToMany(mappedBy = "roles") val users: Collection<User>? = null,

    @ManyToMany(fetch = FetchType.EAGER) @JoinTable(
        name = "roles_privileges",
        joinColumns = [JoinColumn(name = "role_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "privilege_id", referencedColumnName = "id")]
    ) var privileges: MutableList<Privilege>? = null
)