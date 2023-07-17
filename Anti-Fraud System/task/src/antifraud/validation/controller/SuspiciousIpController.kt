package antifraud.validation.controller

import antifraud.validation.dto.SuspiciousIpDTO
import antifraud.validation.service.SuspiciousIpService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
class SuspiciousIpController(
    private val suspiciousIpService: SuspiciousIpService,
) {
    @PostMapping("/api/antifraud/suspicious-ip")
    fun blockIp(@RequestBody suspiciousIpDTO: SuspiciousIpDTO): ResponseEntity<Any> {
        if (suspiciousIpDTO.ip?.let { suspiciousIpService.existsByIp(it) } == true) {
            return ResponseEntity.status(409).body("ip already exists")
        }

        return ResponseEntity.created(URI.create("/api/antifraud/suspicious-ip")).body(
            suspiciousIpService.save(SuspiciousIpDTO.toEntity(suspiciousIpDTO).apply { blocked = true })
        )
    }

    @DeleteMapping("/api/antifraud/suspicious-ip/{ip}")
    fun unblockIp(@PathVariable ip: String): ResponseEntity<Any> {
        val ipAddress = suspiciousIpService.findByIp(ip) ?: return ResponseEntity.notFound().build()

        suspiciousIpService.delete(ipAddress).apply {
            return ResponseEntity.ok(mapOf("status" to "ip ${ipAddress.ip} successfully removed!"))
        }
    }

    @GetMapping("/api/antifraud/suspicious-ip")
    fun listBlockedIpAddresses(): ResponseEntity<Any> =
        ResponseEntity.ok(suspiciousIpService.findAll().sortBy { it.id })
}