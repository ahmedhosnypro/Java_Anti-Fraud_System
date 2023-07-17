package antifraud.dto

import antifraud.util.IpAddressUtil
import jakarta.validation.constraints.*

data class TransactionDto(
    @field:NotNull @field:NotBlank @field:NotEmpty var amount: Int,

    @field:NotNull @field:NotBlank @field:NotEmpty
    @field:Pattern(regexp = IpAddressUtil.IPV4_REGEX)
    var ip: String? = null,

    @field:NotNull @field:NotBlank @field:NotEmpty @field:Size(min = 16, max = 16) var number: String? = null,
)