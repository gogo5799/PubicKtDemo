package com.yitinglin.kotlinjetpackdemo.service.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.yitinglin.kotlinjetpackdemo.model.ClientUser
import com.yitinglin.kotlinjetpackdemo.service.db.entities.User

@Database(
    entities = [User::class],
    version = 1
)
abstract class AppDatabase:RoomDatabase() {

    abstract fun getUserDao():UserDao

    companion object{
        @Volatile
        private var INSTANCE:AppDatabase? =null

        fun getDatabase(context: Context):AppDatabase{
            val tempInstance=INSTANCE
            if (tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance=Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "MyDatabase.db"
                ).build()
                INSTANCE=instance
                return instance
            }
        }
    }

}