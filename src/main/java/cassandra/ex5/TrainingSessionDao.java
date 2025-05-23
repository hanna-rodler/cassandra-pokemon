package cassandra.ex5;

import com.datastax.oss.driver.api.mapper.annotations.*;

import java.time.Instant;
import java.util.UUID;

@Dao
public interface TrainingSessionDao {
    @Insert
    void save(TrainingSession trainingSession);

    @Delete
    void delete(TrainingSession trainingSession);

    @Select
    TrainingSession getByPrimaryKey(UUID sessionId, int pokemonId, UUID trainerId, Instant timestamp, String species);
}
