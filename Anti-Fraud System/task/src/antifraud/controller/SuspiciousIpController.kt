package antifraud.controller

import antifraud.dto.IpDTO
import antifraud.service.IpService
import antifraud.util.IpAddressUtil
import jakarta.validation.Valid
import jakarta.validation.constraints.Pattern
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@Validated
@EnableMethodSecurity
@RequestMapping("/api/antifraud")
class SuspiciousIpController(
    private val ipService: IpService
) {
    @PostMapping("/suspicious-ip")
    @PreAuthorize("hasAuthority('BLOCK_IP_PRIVILEGE')")
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

    @DeleteMapping("/suspicious-ip/{ip}")
    @PreAuthorize("hasAuthority('UNBLOCK_IP_PRIVILEGE')")
    fun unblockIp(
        @PathVariable @Pattern(regexp = IpAddressUtil.IPV4_REGEX)  ip: String
    ): ResponseEntity<Any> {
        val ipAddress = ipService.findByIp(ip) ?: return ResponseEntity.notFound().build()

        ipService.delete(ipAddress).apply {
            return ResponseEntity.ok(mapOf("status" to "IP ${ipAddress.ip} successfully removed!"))
        }
    }

    @GetMapping("/suspicious-ip")
    @PreAuthorize("hasAuthority('LIST_BLOCKED_IP_PRIVILEGE')")
    fun listBlockedIpAddresses(): ResponseEntity<Any> =
        ResponseEntity.ok(ipService.findAll().sortedBy { it.id }.map { IpDTO.fromEntity(it) })
}