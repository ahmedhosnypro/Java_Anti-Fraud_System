package antifraud.dto

import antifraud.model.User
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import org.modelmapper.ModelMapper
import javax.validation.constraints.Size

class NewUserDTO(
    @field:NotBlank @field:NotEmpty @field:Size
    @field:NotBlank @field:NotEmpty @field:Size(min = 8, max = 50) @JsonProperty("name") var name: String,
    @field:NotBlank @field:NotEmpty @field:Size(min = 8, max = 50) @JsonProperty var username: String,
    @field:NotBlank @field:NotEmpty @field:Size(min = 8, max = 50) @JsonProperty var password: String,
) {
    companion object {
        fun toEntity(newUserDto: NewUserDTO): User {
            return ModelMapper().map(newUserDto, User::class.java)
        }
    }
}