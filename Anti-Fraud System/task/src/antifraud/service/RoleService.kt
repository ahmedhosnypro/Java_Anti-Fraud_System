package antifraud.service

import antifraud.repository.RoleRepository
import org.springframework.stereotype.Service

@Service
class RoleService(
    private val roleRepository: RoleRepository
) {
    fun findByName(name: String) = roleRepository.findByName(name)

}