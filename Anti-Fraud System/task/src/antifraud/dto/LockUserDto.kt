package antifraud.dto

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class LockUserDto(
    @field:NotBlank @field:NotEmpty @field:Size(min = 8, max = 50) @JsonProperty var username: String? = null,
    @field:NotNull @field:NotBlank @field:NotEmpty var operation: String? = null,
)