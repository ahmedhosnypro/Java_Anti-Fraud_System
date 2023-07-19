package antifraud.dto

import antifraud.util.RoleSet
import antifraud.util.UserRoleConstraint
import jakarta.validation.constraints.NotBlank

data class UpdateUserRoleDTO(
    @field:NotBlank var username: String? = null,
    @field:UserRoleConstraint(acceptedRoles = [RoleSet.SUPPORT, RoleSet.MERCHANT]) var role: String? = null,
)