package com.example.todo_app

import android.app.Application
import androidx.room.Room
import com.example.todo_app.db.TodoDatabase

class MainApplication : Application() {

    companion object {
        @Volatile
        private var INSTANCE: TodoDatabase? = null

        fun getDatabase(application: Application): TodoDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    application.applicationContext,
                    TodoDatabase::class.java,
                    "todo_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }

    val database: TodoDatabase by lazy {
        getDatabase(this)
    }
}
