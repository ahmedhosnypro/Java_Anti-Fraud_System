package antifraud.model

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.sql.Timestamp


@Entity
class Privilege(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null,
    @Column(name = "name", nullable = false) var name: String? = null,

    @ManyToMany(mappedBy = "privileges") var roles: MutableList<Role>? = null,

    @CreationTimestamp @Temporal(TemporalType.TIMESTAMP)  @Column(name = "created_at", nullable = false) var createdAt: Timestamp? = null,
    @UpdateTimestamp @Temporal(TemporalType.TIMESTAMP)   @Column(name = "updated_at", nullable = false) var updatedAt: Timestamp? = null,
)