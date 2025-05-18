package cassandra.ex5;

import com.datastax.oss.driver.api.core.PagingIterable;
import com.datastax.oss.driver.api.mapper.annotations.*;

import java.util.Optional;
import java.util.UUID;

@Dao
public interface HighestTrainerStatsDao {
    @Insert
    void save(TrainerStats trainerStats);

    @Delete
    void delete(TrainerStats trainerStats);

    @Query("SELECT * FROM big_data_pokemon.highest_trainer_stats WHERE trainer_id = :trainer_id LIMIT 1")
    Optional<HighestTrainerStats> findTopSpeciesByTrainerId(@CqlName("trainer_id") UUID trainerId);
}
