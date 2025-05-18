package cassandra.ex5;

import java.util.UUID;

public class Trainer {
    private UUID trainerId;
    private String name;

    public Trainer() {}

    public Trainer(UUID trainerId, String name) {
        this.trainerId = trainerId;
        this.name = name;
    }

    public UUID getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(UUID trainerId) {
        this.trainerId = trainerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}