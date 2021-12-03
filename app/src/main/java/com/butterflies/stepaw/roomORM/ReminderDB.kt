package com.butterflies.stepaw.roomORM

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [ReminderEntity::class], version = 1)
 abstract class ReminderDB : RoomDatabase(){
     abstract fun reminderdao():ReminderDao
}