package antifraud.controller

import antifraud.dto.LockUserDto
import antifraud.dto.NewUserDTO
import antifraud.dto.UserDTO
import antifraud.util.RoleSet
import antifraud.service.RoleService
import antifraud.service.UserService
import antifraud.util.LockOperationSet
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
class UserController(
    private val userService: UserService,
    private val roleService: RoleService,

    ) {
    private var alreadySetup = false

    @PostMapping("/api/auth/user")
    fun createUser(@RequestBody newUserDto: NewUserDTO): ResponseEntity<Any> {
        if (userService.count() == 0L) {
            return ResponseEntity.created(URI.create("/api/auth/user"))
                .body(UserDTO.fromEntity(userService.save(NewUserDTO.toEntity(newUserDto).apply {
                    this.roles = mutableSetOf(roleService.findByName("ADMINISTRATOR").get())
                    this.accountNonLocked = true
                    alreadySetup = true
                })))
        }

        if (userService.existByUsername(newUserDto.username)) {
            return ResponseEntity.status(409).body("username already exists")
        }
        return ResponseEntity.created(URI.create("/api/auth/user"))
            .body(UserDTO.fromEntity(userService.save(NewUserDTO.toEntity(newUserDto).apply {
                this.roles = mutableSetOf(roleService.findByName("MERCHANT").get())
            })))
    }

    @GetMapping("/api/auth/list")
    fun listUsers(): ResponseEntity<Any> {
        return ResponseEntity.ok(
            userService.listUsers().map(UserDTO::fromEntity)
        )
    }

    @DeleteMapping("/api/auth/user/{username}")
    fun deleteUser(@PathVariable username: String): ResponseEntity<Any> {
        if (!userService.existByUsername(username)) {
            return ResponseEntity.notFound().build()
        }

        val user = userService.findByUsername(username) ?: return ResponseEntity.notFound().build()
        if (user.roles?.contains(roleService.findByName("ADMINISTRATOR").get()) == true) {
            return ResponseEntity.status(403).body("Cannot delete administrator")
        }

        userService.deleteUser(username)
        return ResponseEntity.ok(mapOf("username" to username, "status" to "Deleted successfully!"))
    }

    @PutMapping("/api/auth/role")
    fun setRole(@RequestBody userDto: UserDTO): ResponseEntity<Any> {
        if (userDto.role == null || userDto.role?.uppercase() !in RoleSet.entries.map { it.name }) {
            return ResponseEntity.badRequest().build()
        }

        val role = userDto.role?.let { roleService.findByName(it).get() } ?: return ResponseEntity.badRequest().build()
        val user =
            userDto.username?.let { userService.findByUsername(it) } ?: return ResponseEntity.notFound().build()


        if (user.roles?.contains(role) == true) {
            return ResponseEntity.status(409).body("user already has this role")
        }

        userService.setRole(user, role).apply {
            UserDTO.fromEntity(user).apply {
                return ResponseEntity.ok(this)
            }
        }
    }

    @PutMapping("/api/auth/access")
    fun enableUser(@RequestBody lockUserDto: LockUserDto): ResponseEntity<Any> {
        if (lockUserDto.username == null || lockUserDto.operation == null || lockUserDto.operation !in LockOperationSet.entries.map { it.name }) {
            return ResponseEntity.badRequest().build()
        }

        val nonLocked = lockUserDto.operation?.let { LockOperationSet.valueOf(it).state }
            ?: return ResponseEntity.badRequest().build()
        val user = userService.findByUsername(lockUserDto.username!!) ?: return ResponseEntity.notFound().build()

        if (user.accountNonLocked == nonLocked) {
            return ResponseEntity.status(409).body("user already has this status")
        }

        userService.setAccess(user, nonLocked).apply {
            return ResponseEntity.ok(
                mapOf("status" to "User ${user.username} ${if (user.accountNonLocked) "UNLOCKED" else "LOCKED"}!")
            )
        }
    }
}