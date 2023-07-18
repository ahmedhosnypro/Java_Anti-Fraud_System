package antifraud.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size

data class LockUserDto(
    @field:NotEmpty @field:Size(min = 8, max = 50) var username: String? = null,
    @field:NotBlank var operation: String? = null,
)