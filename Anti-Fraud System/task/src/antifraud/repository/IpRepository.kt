package antifraud.repository

import antifraud.model.Ip
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface IpRepository : JpaRepository<Ip, Long> {
    fun findByIp(ip: String): Optional<Ip>
    fun existsByIp(ip: String): Boolean
}