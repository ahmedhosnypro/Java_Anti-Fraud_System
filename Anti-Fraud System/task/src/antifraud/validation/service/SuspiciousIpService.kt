package antifraud.validation.service

import antifraud.validation.repository.SuspiciousIpRepository
import antifraud.validation.model.SuspiciousIp
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class SuspiciousIpService(
    private val suspiciousIpRepository: SuspiciousIpRepository,
) {
    fun save(suspiciousIp: SuspiciousIp): SuspiciousIp = suspiciousIpRepository.save(suspiciousIp)
    fun findByIp(ip: String): SuspiciousIp? = suspiciousIpRepository.findByIp(ip).getOrNull()
    fun existsByIp(ip: String): Boolean = suspiciousIpRepository.existsByIp(ip)
    fun delete(suspiciousIp: SuspiciousIp) = suspiciousIpRepository.delete(suspiciousIp)
    fun findAll(): MutableList<SuspiciousIp> = suspiciousIpRepository.findAll()
}