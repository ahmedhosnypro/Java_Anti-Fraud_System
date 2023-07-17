package antifraud.model

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.sql.Timestamp

@Entity
class Ip(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id", nullable = false) var id: Long? = null,
    @Column(name = "ip", nullable = false) var ip: String? = null,
    @Column(name = "blocked", nullable = false) var blocked: Boolean = false,

    @CreationTimestamp @Column(name = "created_at", nullable = false) var createdAt: Timestamp? = null,
    @UpdateTimestamp @Column(name = "updated_at", nullable = false) var updatedAt: Timestamp? = null,
)