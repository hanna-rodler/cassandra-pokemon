package cassandra.ex5;

import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.Delete;
import com.datastax.oss.driver.api.mapper.annotations.Insert;

@Dao
public interface TrainingSessionDao {
    @Insert
    void save(TrainingSession trainingSession);

    @Delete
    void delete(TrainingSession trainingSession);
}
