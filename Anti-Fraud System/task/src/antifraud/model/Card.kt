package antifraud.model

import jakarta.persistence.*

@Entity
class Card(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id", nullable = false) var id: Long? = null,
    @Column(name = "number", nullable = false) var number: String? = null,
)