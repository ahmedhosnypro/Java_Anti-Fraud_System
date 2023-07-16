package antifraud.user.model

import jakarta.persistence.*


@Entity
class Privilege(
    @Id @GeneratedValue(strategy = GenerationType.AUTO) var id: Long? = null,

    var name: String? = null,

    @ManyToMany(mappedBy = "privileges") var roles: MutableList<Role>? = null
)