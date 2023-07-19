package antifraud.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty

data class LockUserDto(
    @field:NotBlank var username: String? = null,
    @field:NotBlank var operation: String? = null,
)