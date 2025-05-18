package cassandra.ex5;

import com.datastax.oss.driver.api.mapper.annotations.*;

import java.util.UUID;

@Dao
public interface TrainerPokemonDao {

    // Insert & update in Cassandra synonym
    @Insert
    void save(TrainerPokemon trainerPokemon);

    @Delete
    void delete(TrainerPokemon trainerPokemon);

    @Select(customWhereClause = "trainer_id = :trainer_id AND pokemon_id = :pokemon_id")
    @CqlName("trainer_pokemon")
    TrainerPokemon getPokemonById(@CqlName("trainer_id") UUID trainerId,
                                  @CqlName("pokemon_id") int pokemonId);

    @Query("UPDATE big_data_pokemon.trainer_pokemon SET xp = :xp, level = :level, completed_training_sessions = :completed_training_sessions WHERE trainer_id = :trainer_id AND pokemon_id = :pokemon_id")
    void updateStats(
            @CqlName("trainer_id") UUID trainerId,
            @CqlName("pokemon_id") int pokemonId,
            int xp, int level,
            @CqlName("completed_training_sessions") int completedTrainingSessions);


}
