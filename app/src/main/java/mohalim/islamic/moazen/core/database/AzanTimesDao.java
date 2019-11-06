package mohalim.islamic.moazen.core.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import mohalim.islamic.moazen.core.model.AzanTimesItem;

@Dao
public interface AzanTimesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(AzanTimesItem azanTimesItem);

    @Query("DELETE FROM azan_times")
    void deleteAll();

    @Query("SELECT * from azan_times WHERE city = :city AND month = :month AND day = :day")
    AzanTimesItem getAzanTimesForday(String city, String month, String day);

    @Query("SELECT Count(*) from azan_times WHERE city = :city")
    long getCount(String city);


}
