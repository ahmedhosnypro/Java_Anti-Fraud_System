package antifraud.validation.repository

import antifraud.validation.model.SuspiciousIp
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface SuspiciousIpRepository : JpaRepository<SuspiciousIp, Long> {
    fun findByIp(ip: String): Optional<SuspiciousIp>
    fun existsByIp(ip: String): Boolean
}