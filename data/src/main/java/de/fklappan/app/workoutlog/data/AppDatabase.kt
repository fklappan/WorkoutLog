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
import de.fklappan.app.workoutlog.data.tag.TagDao
import de.fklappan.app.workoutlog.data.tag.TagDataModel
import de.fklappan.app.workoutlog.data.workout.WorkoutDataModel
import de.fklappan.app.workoutlog.data.workout.WorkoutDao

@Database(entities = [WorkoutDataModel::class, ResultDataModel::class, TagDataModel::class], version = 3, exportSchema = true)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun workoutDao(): WorkoutDao
    abstract fun resultDao(): ResultDao
    abstract fun tagDao(): TagDao

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
                    .addMigrations(MIGRATION_2_3)
                    .allowMainThreadQueries()
                    .build()

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE workout ADD COLUMN favorite INTEGER DEFAULT 0 NOT NULL")
            }
        }

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE tag (tag_id INTEGER PRIMARY KEY, text TEXT NOT NULL, workout_id INTEGER NOT NULL, FOREIGN KEY (workout_id) REFERENCES workout (workout_Id) ON DELETE NO ACTION ON UPDATE NO ACTION )")
            }
        }
    }





}