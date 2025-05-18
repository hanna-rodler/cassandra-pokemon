package cassandra.ex5;

import com.datastax.oss.driver.api.mapper.annotations.ClusteringColumn;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;

import java.util.UUID;

@Entity
public class TrainerTypeStats {

    @PartitionKey
    private UUID trainerId;
    @ClusteringColumn
    private String pokemonType;
    private int totalTrainings;
    private double averageStatImprovement; // could be abstracted from session data

    public TrainerTypeStats() {}

    public TrainerTypeStats(UUID trainerId, String pokemonType, int totalTrainings, double averageStatImprovement) {
        this.trainerId = trainerId;
        this.pokemonType = pokemonType;
        this.totalTrainings = totalTrainings;
        this.averageStatImprovement = averageStatImprovement;
    }

    public UUID getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(UUID trainerId) {
        this.trainerId = trainerId;
    }

    public String getPokemonType() {
        return pokemonType;
    }

    public void setPokemonType(String pokemonType) {
        this.pokemonType = pokemonType;
    }

    public int getTotalTrainings() {
        return totalTrainings;
    }

    public void setTotalTrainings(int totalTrainings) {
        this.totalTrainings = totalTrainings;
    }

    public double getAverageStatImprovement() {
        return averageStatImprovement;
    }

    public void setAverageStatImprovement(double averageStatImprovement) {
        this.averageStatImprovement = averageStatImprovement;
    }
}
