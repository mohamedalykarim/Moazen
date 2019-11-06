package mohalim.islamic.moazen.core.database;

import android.content.Context;
import android.content.Entity;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import mohalim.islamic.moazen.core.model.AzanTimesItem;

@Database(entities = {AzanTimesItem.class}, version = 1, exportSchema = false)
public abstract class AzanTimesDatabase extends RoomDatabase {
    public abstract AzanTimesDao azanTimesDao();

    private static volatile AzanTimesDatabase INSTANCE;

    public static AzanTimesDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AzanTimesDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AzanTimesDatabase.class, "azan_times_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
