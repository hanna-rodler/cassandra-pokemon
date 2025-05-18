package cassandra.ex5_old;

import com.datastax.oss.driver.api.core.PagingIterable;
import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.Delete;
import com.datastax.oss.driver.api.mapper.annotations.Insert;
import com.datastax.oss.driver.api.mapper.annotations.Select;

@Dao
public interface PokemonDao {

    // Insert & update in Cassandra synonym
    @Insert
    void save(Pokemon pokemon);

    @Delete
    void delete(Pokemon pokemon);

    // kann nicht suchen, except for when allowFiltering = true, weil nicht in PK ist
    @Select(customWhereClause = "name = :name", allowFiltering = true)
    Pokemon findByName(String name);

    @Select(customWhereClause = "legendary = true")
    PagingIterable<Pokemon> getLegendary();

    @Select
    PagingIterable<Pokemon> getAllPokemons();
}
