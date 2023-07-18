package antifraud.controller

import antifraud.dto.IpDTO
import antifraud.service.IpService
import antifraud.util.IpAddressUtil
import jakarta.validation.Valid
import jakarta.validation.constraints.Pattern
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@Validated
class SuspiciousIpController(
    private val ipService: IpService
) {
    @PostMapping("/api/antifraud/suspicious-ip")
    fun blockIp(@RequestBody @Valid ipDTO: IpDTO): ResponseEntity<Any> {
        if (ipDTO.ip?.let { ipService.existsByIp(it) } == true) {
            return ResponseEntity.status(409).body("ip already exists")
        }

        return ResponseEntity.ok().body(
            IpDTO.fromEntity(
                ipService.save(IpDTO.toEntity(ipDTO).apply { blocked = true })
            )
        )
    }

    @DeleteMapping("/api/antifraud/suspicious-ip/{ip}")
    fun unblockIp(
        @PathVariable @Pattern(regexp = IpAddressUtil.IPV4_REGEX)  ip: String
    ): ResponseEntity<Any> {
        val ipAddress = ipService.findByIp(ip) ?: return ResponseEntity.notFound().build()

        ipService.delete(ipAddress).apply {
            return ResponseEntity.ok(mapOf("status" to "IP ${ipAddress.ip} successfully removed!"))
        }
    }

    @GetMapping("/api/antifraud/suspicious-ip")
    fun listBlockedIpAddresses(): ResponseEntity<Any> =
        ResponseEntity.ok(ipService.findAll().sortedBy { it.id }.map { IpDTO.fromEntity(it) })
}