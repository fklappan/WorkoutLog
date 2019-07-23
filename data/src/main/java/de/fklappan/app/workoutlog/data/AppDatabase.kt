package de.fklappan.app.workoutlog.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import de.fklappan.app.workoutlog.data.result.ResultDataModel
import de.fklappan.app.workoutlog.data.result.ResultDao
import de.fklappan.app.workoutlog.data.workout.WorkoutDataModel
import de.fklappan.app.workoutlog.data.workout.WorkoutDao

@Database(entities = [WorkoutDataModel::class, ResultDataModel::class], version = 2)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun workoutDao(): WorkoutDao
    abstract fun resultDao(): ResultDao

    companion object {

        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
                }

        private fun buildDatabase(context: Context) =
                Room.databaseBuilder(context.applicationContext,
                    AppDatabase::class.java, "workoutlog.db")
                    .addMigrations(MIGRATION_1_2)
                    .allowMainThreadQueries()
                    .build()

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE workout ADD COLUMN favorite INTEGER DEFAULT 0 NOT NULL")
            }
        }
    }





}