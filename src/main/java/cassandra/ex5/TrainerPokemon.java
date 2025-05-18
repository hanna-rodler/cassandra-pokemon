package cassandra.ex5;

import com.datastax.oss.driver.api.mapper.annotations.ClusteringColumn;
import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;

import java.util.UUID;

@Entity
public class TrainerPokemon {

    @ClusteringColumn(0)
    @CqlName("pokemon_id")
    private int pokemonId;

    @PartitionKey
    @CqlName("trainer_id")
    private UUID trainerId;

    private String species;
    private String type;
    private int level;
    private int xp;

    public TrainerPokemon() {}

    public TrainerPokemon(int pokemonId, UUID trainerId, String species, String type, int level, int xp) {
        this.pokemonId = pokemonId;
        this.trainerId = trainerId;
        this.species = species;
        this.type = type;
        this.level = level;
        this.xp = xp;
    }

    public int getPokemonId() {
        return pokemonId;
    }

    public void setPokemonId(int pokemonId) {
        this.pokemonId = pokemonId;
    }

    public UUID getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(UUID trainerId) {
        this.trainerId = trainerId;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }
}
