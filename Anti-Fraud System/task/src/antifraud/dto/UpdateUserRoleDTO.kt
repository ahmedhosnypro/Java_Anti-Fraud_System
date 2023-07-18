package antifraud.dto

import antifraud.util.RoleSet
import antifraud.util.UserRoleConstraint
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class UpdateUserRoleDTO(
    @field:NotBlank @field:Size(min = 8, max = 50)  var username: String? = null,
    @field:NotBlank @field:UserRoleConstraint(acceptedRoles = [RoleSet.SUPPORT, RoleSet.MERCHANT]) var role: String? = null,
)