package antifraud.dto

import antifraud.model.User
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size
import org.modelmapper.ModelMapper
class NewUserDto(
    @NotBlank @NotEmpty @Size(min = 8, max = 50) @JsonProperty("name") var name: String,
    @NotBlank @NotEmpty @Size(min = 8, max = 50) @JsonProperty var username: String,
    @NotBlank @NotEmpty @Size(min = 8, max = 50) @JsonProperty var password: String,
) {
    companion object {
        fun toEntity(newUserDto: NewUserDto): User {
            return ModelMapper().map(newUserDto, User::class.java)
        }
    }
}