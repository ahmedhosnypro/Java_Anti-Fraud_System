package antifraud.dto

import antifraud.model.Ip
import antifraud.util.IpAddressUtil
import jakarta.validation.constraints.*
import org.modelmapper.ModelMapper

data class IpDTO(
    var id: Long? = null,

    @field:NotNull @field:NotBlank @field:NotEmpty
    @field:Pattern(regexp =IpAddressUtil.IPV4_REGEX)
    var ip: String? = null,
) {
    companion object {
        fun fromEntity(ip: Ip): IpDTO =
            ModelMapper().map(ip, IpDTO::class.java)

        fun toEntity(ipDTO: IpDTO): Ip =
            ModelMapper().map(ipDTO, Ip::class.java)
    }
}