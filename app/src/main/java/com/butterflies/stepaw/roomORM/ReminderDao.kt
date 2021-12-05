package com.butterflies.stepaw.roomORM

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


@Dao
interface ReminderDao {
    @Query("select * from remindersTable")
    fun getAll():MutableList<ReminderEntity>

    @Insert
    fun insertAll(vararg reminder:ReminderEntity)

    @Query("select * from remindersTable where id = :uid")
    fun getById(uid:Int):ReminderEntity

    @Query("DELETE from remindersTable where id LIKE :uid")
    fun deleteByID(uid:Int):Int

}