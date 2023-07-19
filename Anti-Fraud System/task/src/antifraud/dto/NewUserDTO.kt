package antifraud.dto

import antifraud.model.User
import jakarta.validation.constraints.NotBlank
import org.modelmapper.ModelMapper
import javax.validation.constraints.Size

class NewUserDTO(
    @field:NotBlank @field:Size
    @field:NotBlank var name: String,
    @field:NotBlank var username: String,
    @field:NotBlank var password: String,
) {
    companion object {
        fun toEntity(newUserDto: NewUserDTO): User {
            return ModelMapper().map(newUserDto, User::class.java)
        }
    }
}