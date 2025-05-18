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
    @ClusteringColumn(1)
    private String species;
    @ClusteringColumn
    private int totalTrainings;
    private double avgTotalUpdate;

    private double avgHpUpdate;

    private double avgAttackUpdate;

    private double avgDefenseUpdate;

    @CqlName("avg_speed_attack_update")
    private double avgSpeedAttackUpdate;

    @CqlName("avg_speed_defence_update")
    private double avgSpeedDefenceUpdate;

    private double avgSpeedUpdate;

    private double avgGenerationUpdate;

    public TrainerStats() {}

    public TrainerStats(UUID trainerId, String species, int totalTrainings, double avgTotalUpdate, double avgHpUpdate, double avgAttackUpdate, double avgDefenseUpdate, double avgSpeedAttackUpdate, double avgSpeedDefenceUpdate, double avgSpeedUpdate, double avgGenerationUpdate) {
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

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public int getTotalTrainings() {
        return totalTrainings;
    }

    public void setTotalTrainings(int totalTrainings) {
        this.totalTrainings = totalTrainings;
    }

    public double getAvgTotalUpdate() {
        return avgTotalUpdate;
    }

    public void setAvgTotalUpdate(double avgTotalUpdate) {
        this.avgTotalUpdate = avgTotalUpdate;
    }

    public double getAvgHpUpdate() {
        return avgHpUpdate;
    }

    public void setAvgHpUpdate(double avgHpUpdate) {
        this.avgHpUpdate = avgHpUpdate;
    }

    public double getAvgAttackUpdate() {
        return avgAttackUpdate;
    }

    public void setAvgAttackUpdate(double avgAttackUpdate) {
        this.avgAttackUpdate = avgAttackUpdate;
    }

    public double getAvgDefenseUpdate() {
        return avgDefenseUpdate;
    }

    public void setAvgDefenseUpdate(double avgDefenseUpdate) {
        this.avgDefenseUpdate = avgDefenseUpdate;
    }

    public double getAvgSpeedAttackUpdate() {
        return avgSpeedAttackUpdate;
    }

    public void setAvgSpeedAttackUpdate(double avgSpeedAttackUpdate) {
        this.avgSpeedAttackUpdate = avgSpeedAttackUpdate;
    }

    public double getAvgSpeedDefenceUpdate() {
        return avgSpeedDefenceUpdate;
    }

    public void setAvgSpeedDefenceUpdate(double avgSpeedDefenceUpdate) {
        this.avgSpeedDefenceUpdate = avgSpeedDefenceUpdate;
    }

    public double getAvgSpeedUpdate() {
        return avgSpeedUpdate;
    }

    public void setAvgSpeedUpdate(double avgSpeedUpdate) {
        this.avgSpeedUpdate = avgSpeedUpdate;
    }

    public double getAvgGenerationUpdate() {
        return avgGenerationUpdate;
    }

    public void setAvgGenerationUpdate(double avgGenerationUpdate) {
        this.avgGenerationUpdate = avgGenerationUpdate;
    }

    public String print() {
        return "TrainerStats{" +
                "trainerId=" + trainerId +
                ", species='" + species + '\'' +
                ", totalTrainings=" + totalTrainings +
                ", avgTotalUpdate=" + avgTotalUpdate +
                ", avgHpUpdate=" + avgHpUpdate +
                ", avgAttackUpdate=" + avgAttackUpdate +
                ", avgDefenseUpdate=" + avgDefenseUpdate +
                ", avgSpeedAttackUpdate=" + avgSpeedAttackUpdate +
                ", avgSpeedDefenceUpdate=" + avgSpeedDefenceUpdate +
                ", avgSpeedUpdate=" + avgSpeedUpdate +
                ", avgGenerationUpdate=" + avgGenerationUpdate +
                '}';
    }
}
