package antifraud.util

import antifraud.model.Privilege
import antifraud.model.Role
import antifraud.repository.PrivilegeRepository
import antifraud.repository.RoleRepository
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional


@Component
class SetupDataLoader(
    private val roleRepository: RoleRepository,
    private val privilegeRepository: PrivilegeRepository,
) : ApplicationListener<ContextRefreshedEvent> {
    private var alreadySetup = false

    @Transactional
    override fun onApplicationEvent(event: ContextRefreshedEvent) {
        if (alreadySetup) return

        //admin privileges
        val accessAdminPrivilege = createPrivilegeIfNotFound("ACCESS_ADMIN_PRIVILEGE")
        val editUserRolePrivilege = createPrivilegeIfNotFound("UPDATE_USER_ROLE_PRIVILEGE")
        val editUserAccessPrivilege = createPrivilegeIfNotFound("UPDATE_USER_ACCESS_PRIVILEGE")
        val deleteUserPrivilege = createPrivilegeIfNotFound("DELETE_USER_PRIVILEGE")
        //support privileges
        val listUserPrivilege = createPrivilegeIfNotFound("LIST_USER_PRIVILEGE")
        val addStolenCardPrivilege = createPrivilegeIfNotFound("ADD_STOLEN_CARD_PRIVILEGE")
        val deleteStolenCardPrivilege = createPrivilegeIfNotFound("DELETE_STOLEN_CARD_PRIVILEGE")
        val listStolenCardPrivilege = createPrivilegeIfNotFound("LIST_STOLEN_CARD_PRIVILEGE")
        val blockIpPrivilege = createPrivilegeIfNotFound("BLOCK_IP_PRIVILEGE")
        val unblockIpPrivilege = createPrivilegeIfNotFound("UNBLOCK_IP_PRIVILEGE")
        val listBlockedIpPrivilege = createPrivilegeIfNotFound("LIST_BLOCKED_IP_PRIVILEGE")
        val listTransactionPrivilege = createPrivilegeIfNotFound("LIST_TRANSACTION_PRIVILEGE")
        val updateTransactionPrivilege = createPrivilegeIfNotFound("UPDATE_TRANSACTION_PRIVILEGE")
        //transaction privileges
        val checkTransactionPrivilege = createPrivilegeIfNotFound("CHECK_TRANSACTION_PRIVILEGE")


        val merchantPrivileges = mutableSetOf(
            checkTransactionPrivilege
        )
        val supportPrivilege = mutableSetOf(
            listUserPrivilege,
            addStolenCardPrivilege,
            deleteStolenCardPrivilege,
            listStolenCardPrivilege,
            blockIpPrivilege,
            unblockIpPrivilege,
            listBlockedIpPrivilege,
            listTransactionPrivilege,
            updateTransactionPrivilege
        )

        val adminPrivileges = mutableSetOf(
            deleteUserPrivilege, accessAdminPrivilege, editUserRolePrivilege, editUserAccessPrivilege, listUserPrivilege
        )

        createRoleIfNotFound("ADMINISTRATOR", adminPrivileges)
        createRoleIfNotFound("SUPPORT", supportPrivilege)
        createRoleIfNotFound("MERCHANT", merchantPrivileges)

        alreadySetup = true
    }

    @Transactional
    fun createPrivilegeIfNotFound(name: String?): Privilege {
        return privilegeRepository.save(
            privilegeRepository.findByName(name).orElse(Privilege(name = name))
        )
    }

    @Transactional
    fun createRoleIfNotFound(name: String?, privileges: MutableSet<Privilege>?): Role {
        return roleRepository.save(
            roleRepository.findByName(name).orElse(Role(name = name, privileges = privileges))
        )
    }
}