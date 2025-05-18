package cassandra.ex5;

import com.datastax.oss.driver.api.mapper.annotations.ClusteringColumn;
import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;

import java.util.UUID;

@Entity
public class TrainerStats {

    @PartitionKey
    private UUID trainerId;
    @PartitionKey(1)
    private String species;
    private int totalTrainings;
    private int avgTotalUpdate;

    private int avgHpUpdate;

    private int avgAttackUpdate;

    private int avgDefenseUpdate;

    @CqlName("speed_attack_update")
    private int avgSpeedAttackUpdate;

    @CqlName("speed_defence_update")
    private int avgSpeedDefenceUpdate;

    private int avgSpeedUpdate;

    private int avgGenerationUpdate;

    public TrainerStats() {}

    public TrainerStats(UUID trainerId, String species, int totalTrainings, int avgTotalUpdate, int avgHpUpdate, int avgAttackUpdate, int avgDefenseUpdate, int avgSpeedAttackUpdate, int avgSpeedDefenceUpdate, int avgSpeedUpdate, int avgGenerationUpdate) {
        this.trainerId = trainerId;
        this.species = species;
        this.totalTrainings = totalTrainings;
        this.avgTotalUpdate = avgTotalUpdate;
        this.avgHpUpdate = avgHpUpdate;
        this.avgAttackUpdate = avgAttackUpdate;
        this.avgDefenseUpdate = avgDefenseUpdate;
        this.avgSpeedAttackUpdate = avgSpeedAttackUpdate;
        this.avgSpeedDefenceUpdate = avgSpeedDefenceUpdate;
        this.avgSpeedUpdate = avgSpeedUpdate;
        this.avgGenerationUpdate = avgGenerationUpdate;
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

    public int getTotalUpdate() {
        return avgTotalUpdate;
    }

    public void setTotalUpdate(int avgTotalUpdate) {
        this.avgTotalUpdate = avgTotalUpdate;
    }

    public int getAvgHpUpdate() {
        return avgHpUpdate;
    }

    public void setAvgHpUpdate(int avgHpUpdate) {
        this.avgHpUpdate = avgHpUpdate;
    }

    public int getAvgAttackUpdate() {
        return avgAttackUpdate;
    }

    public void setAvgAttackUpdate(int avgAttackUpdate) {
        this.avgAttackUpdate = avgAttackUpdate;
    }

    public int getAvgDefenseUpdate() {
        return avgDefenseUpdate;
    }

    public void setAvgDefenseUpdate(int avgDefenseUpdate) {
        this.avgDefenseUpdate = avgDefenseUpdate;
    }

    public int getAvgSpeedAttackUpdate() {
        return avgSpeedAttackUpdate;
    }

    public void setAvgSpeedAttackUpdate(int avgSpeedAttackUpdate) {
        this.avgSpeedAttackUpdate = avgSpeedAttackUpdate;
    }

    public int getAvgSpeedDefenceUpdate() {
        return avgSpeedDefenceUpdate;
    }

    public void setAvgSpeedDefenceUpdate(int avgSpeedDefenceUpdate) {
        this.avgSpeedDefenceUpdate = avgSpeedDefenceUpdate;
    }

    public int getAvgSpeedUpdate() {
        return avgSpeedUpdate;
    }

    public void setAvgSpeedUpdate(int avgSpeedUpdate) {
        this.avgSpeedUpdate = avgSpeedUpdate;
    }

    public int getAvgGenerationUpdate() {
        return avgGenerationUpdate;
    }

    public void setAvgGenerationUpdate(int avgGenerationUpdate) {
        this.avgGenerationUpdate = avgGenerationUpdate;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }
}
