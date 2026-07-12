package kdbrian.github.io.drag.controller

import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.web.csrf.CsrfToken
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class LoginController {

    @GetMapping("/login")
    fun login(
        request: HttpServletRequest,
        @RequestParam(required = false) error: String?,
        @RequestParam(required = false) logout: String?,
        model: Model,
    ): String {
        model.addAttribute("title", "Dr'_'g")
        model.addAttribute("error", error != null)
        model.addAttribute("loggedOut", logout != null)
        model.addAttribute("csrf", request.getAttribute("_csrf") as CsrfToken)
        return "login"
    }
}
