package antifraud.user.dto

import antifraud.user.model.User
import com.fasterxml.jackson.annotation.JsonProperty
import org.modelmapper.ModelMapper

class UserDto(
    @JsonProperty var id: Long? = null,
    @JsonProperty var name: String? = null,
    @JsonProperty var username: String? = null,
    @JsonProperty var role: String? = null,
) {
    companion object {

        fun fromEntity(user: User): UserDto {
            return ModelMapper().map(user, UserDto::class.java)
                .apply {
                    role = user.roles?.firstOrNull()?.name
                }
        }
    }
}