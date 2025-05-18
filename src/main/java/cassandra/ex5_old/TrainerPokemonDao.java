package cassandra.ex5_old;

import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.Delete;
import com.datastax.oss.driver.api.mapper.annotations.Insert;
import com.datastax.oss.driver.api.mapper.annotations.Select;

import java.util.List;
import java.util.UUID;


@Dao
public interface TrainerPokemonDao {
    @Insert
    void save(TrainerPokemon tp);

    @Delete
    void delete(TrainerPokemon pokemon);

    @Select
    List<TrainerPokemon> findByTrainerId(UUID trainerId);
}

