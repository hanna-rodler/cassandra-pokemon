package cassandra.ex5;

import com.datastax.oss.driver.api.core.PagingIterable;
import com.datastax.oss.driver.api.mapper.annotations.*;

import java.util.Optional;
import java.util.UUID;

@Dao
public interface TrainerStatsDao {
    @Insert
    void save(TrainerStats trainerStats);

    @Delete
    void delete(TrainerStats trainerStats);

    @Select
    @Query("SELECT * FROM trainer_stats")
    PagingIterable<TrainerStats> getAllTrainerStats();

    @Query("SELECT * FROM big_data_pokemon.trainer_stats WHERE trainer_id = :trainer_id LIMIT 1")
    Optional<TrainerStats> findTopSpeciesByTrainerId(@CqlName("trainer_id") UUID trainerId);
}
