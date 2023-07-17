package antifraud.user.util

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

        //admin privileges
        val adminPrivilege = createPrivilegeIfNotFound("ADMIN_PRIVILEGE")
        val accessAdminPrivilege = createPrivilegeIfNotFound("ACCESS_ADMIN_PRIVILEGE")
        val editRolePrivilege = createPrivilegeIfNotFound("EDIT_ROLE_PRIVILEGE")
        val deleteUserPrivilege = createPrivilegeIfNotFound("DELETE_USER_PRIVILEGE")
        //support privileges
        val listUserPrivilege = createPrivilegeIfNotFound("LIST_USER_PRIVILEGE")
        val stolenCardPrivilege = createPrivilegeIfNotFound("STOLEN_CARD_PRIVILEGE")
        val suspiciousIpPrivilege = createPrivilegeIfNotFound("SUSPICIOUS_IP_PRIVILEGE")
        //transaction privileges
        val checkTransactionPrivilege = createPrivilegeIfNotFound("CHECK_TRANSACTION_PRIVILEGE")


        val merchantPrivileges = mutableSetOf(
            checkTransactionPrivilege
        )
        val supportPrivilege = mutableSetOf(
            listUserPrivilege,
            stolenCardPrivilege,
            suspiciousIpPrivilege,
        )

        val adminPrivileges = mutableSetOf(
            adminPrivilege,
            deleteUserPrivilege,
            accessAdminPrivilege,
            editRolePrivilege,
            listUserPrivilege
        )

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
    open fun createRoleIfNotFound(name: String?, privileges: MutableSet<Privilege>?): Role {
        return roleRepository.save(
            roleRepository.findByName(name).orElse(Role(name = name, privileges = privileges))
        )
    }
}