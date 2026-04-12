package com.kdbrian.sage.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kdbrian.sage.data.datastore.AppDataStore
import com.kdbrian.sage.domain.model.InAppActivity
import com.kdbrian.sage.domain.repo.AppActivityService
import com.kdbrian.sage.domain.repo.CategoryRepo
import com.kdbrian.sage.domain.repo.DocumentRepo
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber

class MainViewModel(
    private val appDataStore: AppDataStore,
    private val inAppActivityService: AppActivityService,
    private val documentRepo: DocumentRepo,
    private val categoryRepo: CategoryRepo
) : ViewModel() {

    init {
        viewModelScope.launch {
            val firstTime = appDataStore.firstTime.first()
            Timber.d("Firsttime $firstTime")
            if (firstTime) {
                inAppActivityService.logActivity(
                    InAppActivity
                        .Builder()
                        .name("First Time in app")
                        .timeStamp(System.currentTimeMillis())
                        .build()
                )
                appDataStore.updateFirstTime(false)
            }

        }
    }

    val books = documentRepo.loadDefaultDocumentsAll()

}