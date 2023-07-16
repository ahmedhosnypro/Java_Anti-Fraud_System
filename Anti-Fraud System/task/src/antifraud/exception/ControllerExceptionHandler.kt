package antifraud.exception

import jakarta.validation.ConstraintViolationException
import jakarta.validation.ValidationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus

/**
 * This class handles exceptions thrown by the controllers
 * @author Ahmed Hosny
 * @version 1.0
 * @since 2023-04-07
 */
@ControllerAdvice
class ControllerExceptionHandler {
    /**
     * when trying to save an invalid item
     *
     * @return 400 (Bad Request)
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException::class)
    fun exceptionHandler(ignored: ValidationException?): ResponseEntity<Any> {
        return ResponseEntity.badRequest().build()
    }

    /**
     * when ConstraintViolationException is thrown
     *
     * @return 400 (Bad Request)
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(
        ConstraintViolationException::class
    )
    fun exceptionHandler(ignored: ConstraintViolationException?): ResponseEntity<Any> {
        return ResponseEntity.badRequest().build()
    }

    /**
     * when a user tries to register with an existing username
     *
     * @return 409 (Conflict)
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UsernameNotFoundException::class)
    fun exceptionHandler(ignored: UsernameNotFoundException?): ResponseEntity<Any> {
        return ResponseEntity.status(409).build()
    }


    /**
     * JSON parse error
     *
     * @return 409 (Conflict)
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun exceptionHandler(ignored: HttpMessageNotReadableException?): ResponseEntity<Any> {
        return ResponseEntity.badRequest().build()
    }
}