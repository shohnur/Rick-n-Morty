package uz.task.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Data::class], version = 1)
abstract class DB:RoomDatabase() {
    abstract fun dao():Dao
}