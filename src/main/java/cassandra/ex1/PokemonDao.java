package cassandra.ex1;

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

    // ist Teil von key, muss nicht extra custom where clause machen
    @Select
    PagingIterable<Pokemon> findByGeneration(int generation);
}
