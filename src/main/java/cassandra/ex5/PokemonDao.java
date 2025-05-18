package cassandra.ex5;

import com.datastax.oss.driver.api.core.PagingIterable;
import com.datastax.oss.driver.api.mapper.annotations.*;

@Dao
public interface PokemonDao {

    // Insert & update in Cassandra synonym
    @Insert
    void save(Pokemon pokemon);

    @Delete
    void delete(Pokemon pokemon);

    @Select(customWhereClause = "name = :name", allowFiltering = true)
    @CqlName("pokemon")
    Pokemon findByName(String name);

    @Select
    @CqlName("pokemon")
    Pokemon findById(int id);
}
