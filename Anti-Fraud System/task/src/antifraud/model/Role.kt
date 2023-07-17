package antifraud.model

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.sql.Timestamp


@Entity
class Role(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null,

    var name: String? = null,

    @ManyToMany(mappedBy = "roles") val users: Collection<User>? = null,

    @ManyToMany(fetch = FetchType.EAGER) @JoinTable(
        name = "roles_privileges",
        joinColumns = [JoinColumn(name = "role_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "privilege_id", referencedColumnName = "id")]
    ) var privileges: MutableSet<Privilege>? = null,

    @CreationTimestamp @Temporal(TemporalType.TIMESTAMP)  @Column(name = "created_at", nullable = false) var createdAt: Timestamp? = null,
    @UpdateTimestamp @Temporal(TemporalType.TIMESTAMP)   @Column(name = "updated_at", nullable = false) var updatedAt: Timestamp? = null,
)