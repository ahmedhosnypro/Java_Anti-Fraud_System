package antifraud.dto

import antifraud.model.User
import com.fasterxml.jackson.annotation.JsonProperty
import org.modelmapper.ModelMapper

class UserDto(
    @JsonProperty var id: Long? = null,
    @JsonProperty var name: String? = null,
    @JsonProperty var username: String? = null,
) {
    companion object {
        fun fromEntity(user: User): UserDto {
            return ModelMapper().map(user, UserDto::class.java)
        }
    }
}