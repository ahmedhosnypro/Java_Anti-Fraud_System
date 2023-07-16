package antifraud.user.repository

import antifraud.user.model.Privilege
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface PrivilegeRepository : JpaRepository<Privilege, Long> {
    fun findByName(name: String?): Optional<Privilege>
}