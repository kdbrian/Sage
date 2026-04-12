package com.kdbrian.sage.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kdbrian.sage.data.local.dao.InAppActivityDao
import com.kdbrian.sage.domain.model.InAppActivity

@Database(
    version = 1,
    exportSchema = false,
    entities = [
        InAppActivity::class
    ]
)
abstract class SageDb : RoomDatabase() {

    abstract fun inAppActivityDao(): InAppActivityDao

    companion object {
        private var INSTANCE: SageDb? = null

        fun getDb(context: Context): SageDb {
            return INSTANCE ?: synchronized(this) {
                Room
                    .databaseBuilder(
                        context = context,
                        name = "sage_db",
                        klass = SageDb::class.java
                    )
                    .fallbackToDestructiveMigration(true)
                    .build()
                    .also {
                        INSTANCE = it
                    }
            }
        }
    }


}