package kdbrian.github.io.drag.resource

import kdbrian.github.io.drag.domain.dto.DocumentCategoryDto
import kdbrian.github.io.drag.domain.model.DocumentCategory
import kdbrian.github.io.drag.domain.service.DocumentCategoryService
import kdbrian.github.io.drag.util.paging.PageParameters
import org.springframework.data.domain.Page
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/documents/category")
@Controller
class DocumentCategoryController(
    private val documentCategoryService: DocumentCategoryService
) {


    @GetMapping
    @QueryMapping
    fun categories(
        @Argument @RequestBody
        parameters: PageParameters
//        @RequestParam(defaultValue = "0") page: Int,
//        @RequestParam(defaultValue = "20") pageSize: Int,
//        @RequestParam sortOrders: Map<String, Sort.Order>
    ): Page<DocumentCategory> {
        return documentCategoryService.all(parameters)
    }

    @MutationMapping
    @PostMapping("/new")
    fun createCategory(
        @Argument
        @RequestBody
        dto: DocumentCategoryDto
    ): DocumentCategory {
        return documentCategoryService.createCategory(dto)
    }


    @QueryMapping
    @GetMapping("/{id}")
    fun categoryById(
        @PathVariable
        @Argument
        id: String
    ): DocumentCategory {
        return documentCategoryService.categoryById(id)
    }


    @PatchMapping("/{id}/update")
    @MutationMapping
    fun updateCategory(
        @PathVariable @Argument
        id: String,
        @RequestBody @Argument
        dto: DocumentCategoryDto
    ): DocumentCategory {
        return documentCategoryService.updateCategory(id, dto)
    }


    @DeleteMapping("/{id}")
    @MutationMapping
    fun deleteCategoryById(
        @PathVariable
        @Argument
        id: String
    ): Boolean {
        return documentCategoryService.delete(id)
    }


}