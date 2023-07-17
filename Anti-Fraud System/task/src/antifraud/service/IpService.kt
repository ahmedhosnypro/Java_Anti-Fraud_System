package antifraud.service

import antifraud.repository.IpRepository
import antifraud.model.Ip
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class IpService(
    private val ipRepository: IpRepository,
) {
    fun save(ip: Ip): Ip = ipRepository.save(ip)
    fun findByIp(ip: String): Ip? = ipRepository.findByIp(ip).getOrNull()
    fun existsByIp(ip: String): Boolean = ipRepository.existsByIp(ip)
    fun delete(ip: Ip) = ipRepository.delete(ip)
    fun findAll(): MutableList<Ip> = ipRepository.findAll()
}