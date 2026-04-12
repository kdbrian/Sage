package com.kdbrian.sage.data.local.impl

import android.content.Context
import com.kdbrian.sage.data.local.SageDb
import com.kdbrian.sage.data.local.dao.InAppActivityDao
import com.kdbrian.sage.domain.model.InAppActivity
import com.kdbrian.sage.domain.repo.AppActivityService
import com.kdbrian.sage.util.dispatchIo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AppActivityServiceImpl(
    sageDb: SageDb
) : AppActivityService {

    private val inAppActivityDao = sageDb.inAppActivityDao()

    override suspend fun logActivity(inAppActivity: InAppActivity) =
        dispatchIo {
            inAppActivityDao.logActivity(inAppActivity)
        }

    override fun logs(): Flow<InAppActivity> =
        inAppActivityDao.logs()
}