package kdbrian.github.io.drag.util

import com.yubico.webauthn.exception.AssertionFailedException
import com.yubico.webauthn.exception.RegistrationFailedException
import org.springframework.data.core.PropertyReferenceException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
@Component
class GlobalErrorHandler {


    @ExceptionHandler(PropertyReferenceException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun propertyReferenceExceptionHandler(ex: PropertyReferenceException): ResponseEntity<String> {
        return ResponseEntity(ex.message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun illegalArgumentExceptionHandler(ex: IllegalArgumentException): ResponseEntity<String> {
        return ResponseEntity(ex.message, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(IllegalStateException::class)
    fun illegalStateExceptionHandler(ex: IllegalStateException): ResponseEntity<String> {
        return ResponseEntity(ex.message, HttpStatus.UNAUTHORIZED)
    }

    @ExceptionHandler(RegistrationFailedException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun registrationFailedExceptionHandler(ex: RegistrationFailedException): ResponseEntity<String> {
        return ResponseEntity("Passkey registration failed: ${ex.message}", HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(AssertionFailedException::class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    fun assertionFailedExceptionHandler(ex: AssertionFailedException): ResponseEntity<String> {
        return ResponseEntity("Passkey authentication failed: ${ex.message}", HttpStatus.UNAUTHORIZED)
    }

}