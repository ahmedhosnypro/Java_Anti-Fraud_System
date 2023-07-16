package antifraud.user

import antifraud.user.model.Privilege
import antifraud.user.model.Role
import antifraud.user.repository.PrivilegeRepository
import antifraud.user.repository.RoleRepository
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional


@Component
open class SetupDataLoader(
    private val roleRepository: RoleRepository,
    private val privilegeRepository: PrivilegeRepository,
) : ApplicationListener<ContextRefreshedEvent> {
    private var alreadySetup = false

    @Transactional
    override fun onApplicationEvent(event: ContextRefreshedEvent) {
        if (alreadySetup) return

        val adminPrivilege = createPrivilegeIfNotFound("ADMIN_PRIVILEGE")
        val deleteUserPrivilege = createPrivilegeIfNotFound("DELETE_USER_PRIVILEGE")
        val listUserPrivilege = createPrivilegeIfNotFound("LIST_USER_PRIVILEGE")
        val checkTransactionPrivilege = createPrivilegeIfNotFound("CHECK_TRANSACTION_PRIVILEGE")
        val accessAdminPrivilege = createPrivilegeIfNotFound("ACCESS_ADMIN_PRIVILEGE")
        val editRolePrivilege = createPrivilegeIfNotFound("EDIT_ROLE_PRIVILEGE")

        val merchantPrivileges = mutableListOf(checkTransactionPrivilege)
        val supportPrivilege = mutableListOf(listUserPrivilege)
        val adminPrivileges = mutableListOf(adminPrivilege, deleteUserPrivilege, accessAdminPrivilege, editRolePrivilege, listUserPrivilege)

        createRoleIfNotFound("ADMINISTRATOR", adminPrivileges)
        createRoleIfNotFound("SUPPORT", supportPrivilege)
        createRoleIfNotFound("MERCHANT", merchantPrivileges)

        alreadySetup = true
    }

    @Transactional
    open fun createPrivilegeIfNotFound(name: String?): Privilege {
        return privilegeRepository.save(
            privilegeRepository.findByName(name).orElse(Privilege(name = name))
        )
    }

    @Transactional
    open fun createRoleIfNotFound(name: String?, privileges: MutableList<Privilege>?): Role {
        return roleRepository.save(
            roleRepository.findByName(name).orElse(Role(name = name, privileges = privileges))
        )
    }
}