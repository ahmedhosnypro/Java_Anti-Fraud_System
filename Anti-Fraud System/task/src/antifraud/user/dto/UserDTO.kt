package antifraud.user.dto

import antifraud.user.model.User
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size
import org.modelmapper.ModelMapper

class UserDTO(
    @NotBlank @NotEmpty @Size(min = 8, max = 50) @JsonProperty var id: Long? = null,
    @NotBlank @NotEmpty @Size(min = 8, max = 50) @JsonProperty var name: String? = null,
    @NotBlank @NotEmpty @Size(min = 8, max = 50) @JsonProperty var username: String? = null,
    @NotBlank @NotEmpty @Size(min = 8, max = 50) @JsonProperty var password: String? = null,
    @NotBlank @NotEmpty @Size(min = 8, max = 50) @JsonProperty var role: String? = null,
) {
    companion object {
        fun fromEntity(user: User): UserDTO {
            return ModelMapper().map(user, UserDTO::class.java).apply {
                role = user.roles?.firstOrNull()?.name
            }
        }

        fun toEntity(userDto: UserDTO): User {
            return ModelMapper().map(userDto, User::class.java)
        }
    }
}