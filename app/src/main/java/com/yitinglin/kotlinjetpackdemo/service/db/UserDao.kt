package com.yitinglin.kotlinjetpackdemo.service.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.yitinglin.kotlinjetpackdemo.model.ClientUser
//import com.yitinglin.kotlinjetpackdemo.service.db.entities.CURRENT_USER_ID
import com.yitinglin.kotlinjetpackdemo.service.db.entities.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(user: User)
    //取出

    @Query("SELECT * FROM user")
    suspend fun getUser():User

    //更新
    @Update
    suspend fun updateUsers(users: User)

    //刪除
    @Delete
    suspend fun deleteUsers(users: User)
}