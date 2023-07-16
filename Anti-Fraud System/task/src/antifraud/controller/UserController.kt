package antifraud.controller

import antifraud.dto.NewUserDto
import antifraud.dto.UserDto
import antifraud.service.SecurityUserDetailsService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
class UserController(
    private val securityUserDetailsService: SecurityUserDetailsService
) {
    @PostMapping("/api/auth/user")
    fun createUser(@RequestBody newUserDto: NewUserDto): ResponseEntity<Any> {
        if (securityUserDetailsService.existByUsername(newUserDto.username)) {
            return ResponseEntity.status(409).body("username already exists")
        }
        return ResponseEntity.created(URI.create("/api/auth/user"))
            .body(UserDto.fromEntity(securityUserDetailsService.save(NewUserDto.toEntity(newUserDto))))
    }

    @GetMapping("/api/auth/list")
    fun listUsers(): ResponseEntity<Any> {
        return ResponseEntity.ok(
            securityUserDetailsService.listUsers().map(UserDto::fromEntity)
        )
    }

    @DeleteMapping("/api/auth/user/{username}")
    fun deleteUser(@PathVariable username: String): ResponseEntity<Any> {
        if (!securityUserDetailsService.existByUsername(username)) {
            return ResponseEntity.notFound().build()
        }
        securityUserDetailsService.deleteUser(username).apply {
            if (this.isPresent) {
                return ResponseEntity.ok(mapOf("username" to username, "status" to "Deleted successfully!"))
            }
        }

        return ResponseEntity.internalServerError().build()
    }
}