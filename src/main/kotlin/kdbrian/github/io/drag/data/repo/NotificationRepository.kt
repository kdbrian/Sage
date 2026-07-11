package kdbrian.github.io.drag.data.repo

import kdbrian.github.io.drag.domain.model.Notification
import kdbrian.github.io.drag.util.paging.defaultPageable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface NotificationRepository : JpaRepository<Notification, String> {
    fun findByUser_Id(userId: String, pageable: Pageable = defaultPageable): Page<Notification>
    fun findByUser_IdAndReadFalse(userId: String, pageable: Pageable = defaultPageable): Page<Notification>
}
