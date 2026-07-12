package kdbrian.github.io.drag.data.repo

import kdbrian.github.io.drag.domain.model.PasskeyCredential
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PasskeyCredentialRepository : JpaRepository<PasskeyCredential, String> {
    fun findByCredentialId(credentialId: String): PasskeyCredential?
    fun findByUser_Id(userId: String): List<PasskeyCredential>
}
