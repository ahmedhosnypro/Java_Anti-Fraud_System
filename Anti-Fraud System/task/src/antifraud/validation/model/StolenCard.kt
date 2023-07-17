package antifraud.validation.model

import jakarta.persistence.*

@Entity
class StolenCard(
    @Id @GeneratedValue(strategy = GenerationType.AUTO) @Column(name = "id", nullable = false) var id: Long? = null,
    @Column(name = "number", nullable = false) var number: String? = null,
)