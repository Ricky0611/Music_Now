package com.example.rikki.musicnow.db

import androidx.room.*

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User)

    @Query("SELECT * FROM user")
    fun getAll(): List<User>

    @Query("SELECT * FROM user WHERE mobile_number LIKE :mobile")
    fun findByMobile(mobile: String): List<User>

    @Delete
    fun delete(user: User)
}