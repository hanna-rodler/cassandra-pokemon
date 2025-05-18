package cassandra.ex5;

import com.datastax.oss.driver.api.mapper.annotations.ClusteringColumn;
import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;

import java.util.UUID;
import java.util.List;

@Entity
public class TrainerPokemon {

    @ClusteringColumn
    @CqlName("pokemon_id")
    private int pokemonId;

    @PartitionKey
    @CqlName("trainer_id")
    private UUID trainerId;

    private String species; // type
    private int level;
    private int xp;
    private int completedTrainingSessions;
//    private List<TrainingSession> trainingSessions;

    public TrainerPokemon() {}

    public TrainerPokemon(int pokemonId, UUID trainerId, String species, int level, int xp, int completedTrainingSessions) {
        this.pokemonId = pokemonId;
        this.trainerId = trainerId;
        this.species = species;
        this.level = level;
        this.xp = xp;
        this.completedTrainingSessions = completedTrainingSessions;
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

/*    public List<TrainingSession> getTrainingSessions() {
        return trainingSessions;
    }

    public void setTrainingSessions(List<TrainingSession> trainingSessions) {
        this.trainingSessions = trainingSessions;
    }*/

    public int getCompletedTrainingSessions() {
        return completedTrainingSessions;
    }

    public void setCompletedTrainingSessions(int completedTrainingSessions) {
        this.completedTrainingSessions = completedTrainingSessions;
    }

    @Override
    public String toString() {
        return "TrainerPokemon{" +
                "pokemonId=" + pokemonId +
                ", trainerId=" + trainerId +
                ", species (type)='" + species + '\'' +
                ", level=" + level +
                ", xp=" + xp +
                ", completedTrainingSessions=" + completedTrainingSessions +
                '}';
    }
}
