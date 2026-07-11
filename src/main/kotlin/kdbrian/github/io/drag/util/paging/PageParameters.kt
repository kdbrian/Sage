package kdbrian.github.io.drag.util.paging

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort


data class PageParameters(
    val pageNumber: Int = 0,
    val pageSize: Int = 20,
    val sortOrders: Map<String, Sort.Direction> = mapOf(
        "updatedAt" to Sort.Direction.DESC
    )
)

val defaultPageable: Pageable = PageParameters().asPageable

val PageParameters.asPageable: Pageable
    get() = PageRequest.of(
        pageNumber, pageSize, Sort.by(
            sortOrders.map {
                when (it.value) {
                    Sort.Direction.ASC -> Sort.Order.asc(it.key)
                    Sort.Direction.DESC -> Sort.Order.desc(it.key)
                }
            }
        )
    )