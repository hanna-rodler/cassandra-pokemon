package cassandra.ex5;

import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.Delete;
import com.datastax.oss.driver.api.mapper.annotations.Insert;

@Dao
public interface TrainerStatsDao {
    @Insert
    void save(TrainerStats trainerStats);

    @Delete
    void delete(TrainerStats trainerStats);
}
