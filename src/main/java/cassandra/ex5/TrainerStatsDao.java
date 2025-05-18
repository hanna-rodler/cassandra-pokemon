package cassandra.ex5;

import com.datastax.oss.driver.api.mapper.annotations.*;

import java.util.UUID;

@Dao
public interface TrainerStatsDao {
    @Insert
    void save(TrainerStats trainerStats);

    @Delete
    void delete(TrainerStats trainerStats);

    @Query("SELECT * FROM big_data_pokemon.trainer_stats WHERE trainer_id = :trainer_id AND species = :species")
    TrainerStats findByKey(@CqlName("trainer_id") UUID trainerId, String species);
}
