package antifraud.validation.dto

import antifraud.validation.model.SuspiciousIp
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import org.modelmapper.ModelMapper

class SuspiciousIpDTO(
    var id: Long? = null,
    @NotNull @NotBlank @NotEmpty
    @Pattern(regexp = """(\b25[0-5]|\b2[0-4][0-9]|\b[01]?[0-9][0-9]?)(\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)){3}""")
    var ip: String? = null,
) {
    companion object {
        fun fromEntity(suspiciousIp: SuspiciousIp): SuspiciousIpDTO =
            ModelMapper().map(suspiciousIp, SuspiciousIpDTO::class.java)

        fun toEntity(suspiciousIpDTO: SuspiciousIpDTO): SuspiciousIp =
            ModelMapper().map(suspiciousIpDTO, SuspiciousIp::class.java)
    }
}