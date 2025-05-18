package cassandra.ex5;

import com.datastax.oss.driver.api.mapper.annotations.ClusteringColumn;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;

import java.util.UUID;

@Entity
public class HighestTrainerStats {

    @PartitionKey
    private UUID trainerId;

    @ClusteringColumn
    private int totalTrainings;

    @ClusteringColumn(1)
    private String species;

    public HighestTrainerStats() {}

    public HighestTrainerStats(UUID trainerId, String species, int totalTrainings) {
        this.trainerId = trainerId;
        this.species = species;
        this.totalTrainings = totalTrainings;
    }

    public UUID getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(UUID trainerId) {
        this.trainerId = trainerId;
    }

    public int getTotalTrainings() {
        return totalTrainings;
    }

    public void setTotalTrainings(int totalTrainings) {
        this.totalTrainings = totalTrainings;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }
}
