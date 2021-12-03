package com.butterflies.stepaw.roomORM

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remindersTable")
data class ReminderEntity(
    @PrimaryKey val id:Int,
    @ColumnInfo(name="hour") val hour:String,
    @ColumnInfo(name="minute") val minute:String,
    @ColumnInfo(name="label") val label:String

)