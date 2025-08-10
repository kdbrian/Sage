package com.kdbrian.sage.di

import com.kdbrian.sage.data.local.model.InAppActivity
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import org.koin.dsl.module

val realmModules = module {

    single<RealmConfiguration>{
        val realmConfiguration = RealmConfiguration.Builder(
            schema = setOf(
                InAppActivity::class
            )
        )
        realmConfiguration.apply {
            name("sage-realm.realm")
        }
        realmConfiguration.build()
    }


    single {
        Realm.open(
            get()
        )
    }


}