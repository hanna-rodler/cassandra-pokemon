package cassandra.ex5;

import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.Delete;
import com.datastax.oss.driver.api.mapper.annotations.Insert;
import com.datastax.oss.driver.api.mapper.annotations.Select;

@Dao
public interface TrainerPokemonDao {

    // Insert & update in Cassandra synonym
    @Insert
    void save(TrainerPokemon trainerPokemon);

    @Delete
    void delete(TrainerPokemon trainerPokemon);

    @Select(customWhereClause = "pokemonId: pokemon_id")
    TrainerPokemon getPokemonById(int pokemonId);
}
