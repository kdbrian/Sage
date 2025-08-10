package com.kdbrian.sage.data.local.model.impl

import com.kdbrian.sage.data.local.model.InAppActivity
import com.kdbrian.sage.data.local.model.dao.InAppActivityDao
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.isManaged
import io.realm.kotlin.query.RealmElementQuery
import io.realm.kotlin.query.RealmQuery
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.lang.Exception

class InAppActivityDaoImpl(
    private val realm: Realm
) : InAppActivityDao {

    override suspend fun logActivity(inAppActivity: InAppActivity) {
        withContext(Dispatchers.Default) {
            try {
                realm.write {
                    copyToRealm(inAppActivity)
                }
            } catch (e: Exception) {
                Timber.tag("logActivity").d(e.message.toString())
            }
        }
    }

    override suspend fun logs(): Flow<InAppActivity> = withContext(Dispatchers.Default) {
        val asFlow = realm.query(clazz = InAppActivity::class).find()
            .toList()
            .asFlow()
        asFlow
    }

    override suspend fun loadLogById(id: String): InAppActivity? =
        withContext(Dispatchers.Default) {
            realm.query(
                clazz = InAppActivity::class,
                query = "_id = $0",
                id
            ).find().firstOrNull()
        }

    override suspend fun clearLogs() = withContext(Dispatchers.Default) {
        realm.write { delete(InAppActivity::class) }
    }
}