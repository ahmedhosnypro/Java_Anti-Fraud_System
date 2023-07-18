package antifraud.dto

import antifraud.model.User
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.modelmapper.ModelMapper

data class UserDTO(
    var id: Long? = null,
    @field:NotBlank @field:Size(min = 3, max = 50) var name: String? = null,
    @field:NotBlank @field:Size(min = 3, max = 50) var username: String? = null,
    var role: String? = null,
) {
    companion object {
        fun fromEntity(user: User): UserDTO {
            return ModelMapper().map(user ,UserDTO::class.java).apply {
                role = user.roles?.firstOrNull()?.name
            }
        }
    }
}