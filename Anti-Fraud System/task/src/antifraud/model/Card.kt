package antifraud.model

import jakarta.persistence.*

@Entity
class Card(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id", nullable = false) var id: Long? = null,
    @Column(nullable = false, unique = true) var number: String? = null,
    @Column(nullable = false) var stolen: Boolean = false,
    @Column(nullable = false) var maxAllowed: Int = MAX_ALLOWED_DEFAULT,
    @Column(nullable = false) var maxManualProcessing: Int = MAX_MANUAL_PROCESSING_DEFAULT,
) {
    companion object {
        const val MAX_ALLOWED_DEFAULT = 200
        const val MAX_MANUAL_PROCESSING_DEFAULT = 1500
    }
}