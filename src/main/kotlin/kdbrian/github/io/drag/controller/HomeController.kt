package kdbrian.github.io.drag.controller

import jakarta.servlet.http.HttpServletRequest
import kdbrian.github.io.drag.domain.model.Document
import kdbrian.github.io.drag.domain.service.DocumentService
import kdbrian.github.io.drag.security.currentUser
import kdbrian.github.io.drag.util.paging.PageParameters
import org.springframework.data.domain.Page
import org.springframework.security.web.csrf.CsrfToken
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class HomeController(
    private val documentService: DocumentService,
) {


    @GetMapping
    fun home(
        request: HttpServletRequest,
        model: Model
    ): String {

        val page = model.getAttribute("page").toString().toIntOrNull() ?: 0
        val size = model.getAttribute("size").toString().toIntOrNull() ?: 20

        val findAll: Page<Document> = documentService.findAll(
            PageParameters(page, size,)
        )
        model.addAttribute("title", "Dr'_'g")
        model.addAttribute("pages", findAll)
        model.addAttribute("username", currentUser?.username)
        model.addAttribute("csrf", request.getAttribute("_csrf") as CsrfToken)

        return "index"
    }

}