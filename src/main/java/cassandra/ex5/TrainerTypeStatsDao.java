package cassandra.ex5;

import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.Delete;
import com.datastax.oss.driver.api.mapper.annotations.Insert;

@Dao
public interface TrainerTypeStatsDao {
    @Insert
    void save(TrainerTypeStats trainerTypeStats);

    @Delete
    void delete(TrainerTypeStats trainerTypeStats);
}
