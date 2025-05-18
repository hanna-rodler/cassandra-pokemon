package cassandra.ex5_old;

import com.datastax.oss.driver.api.mapper.annotations.ClusteringColumn;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;

import java.util.UUID;

@Entity
public class TrainerPokemon {
    @PartitionKey
    private UUID trainerId;

    @ClusteringColumn
    private int pokemonId;

    private String pokemonName;
    private int level;
    private int xp;
    private int completedTrainingSessions;

    public TrainerPokemon() {

    }

    public TrainerPokemon(UUID trainerId, int pokemonId, String pokemonName, int level, int xp, int completedTrainingSessions) {
        this.trainerId = trainerId;
        this.pokemonId = pokemonId;
        this.pokemonName = pokemonName;
        this.level = level;
        this.xp = xp;
        this.completedTrainingSessions = completedTrainingSessions;
    }

    // getters and setters


    public UUID getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(UUID trainerId) {
        this.trainerId = trainerId;
    }

    public int getPokemonId() {
        return pokemonId;
    }

    public void setPokemonId(int pokemonId) {
        this.pokemonId = pokemonId;
    }

    public String getPokemonName() {
        return pokemonName;
    }

    public void setPokemonName(String pokemonName) {
        this.pokemonName = pokemonName;
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

    public int getCompletedTrainingSessions() {
        return completedTrainingSessions;
    }

    public void setCompletedTrainingSessions(int completedTrainingSessions) {
        this.completedTrainingSessions = completedTrainingSessions;
    }
}
