package cassandra.ex5;

import java.util.LinkedHashMap;
import java.util.Map;

class StatsAccumulator {
    int count = 0;
    double hp = 0, attack = 0, defense = 0, speedAttack = 0, speedDefense = 0, speed = 0,
            total = 0, generation = 0;
    int pokemonId = -1;

    void add(TrainingSession ts) {
        count++;
        pokemonId = ts.getPokemonId();
        hp += ts.getHpUpdate();
        total = ts.getTotalUpdate();
        attack += ts.getAttackUpdate();
        defense += ts.getDefenseUpdate();
        speedAttack += ts.getSpeedAttackUpdate();
        speedDefense += ts.getSpeedDefenceUpdate();
        speed += ts.getSpeedUpdate();
        generation += ts.getGenerationUpdate();

    }

    Map<String, Double> averages() {
        Map<String, Double> avg = new LinkedHashMap<>();
        avg.put("avg_hp", hp / count);
        avg.put("avg_total", hp / total);
        avg.put("avg_attack", attack / count);
        avg.put("avg_defense", defense / count);
        avg.put("avg_speed_attack", speedAttack / count);
        avg.put("avg_speed_defense", speedDefense / count);
        avg.put("avg_speed", speed / count);
        avg.put("avg_generation", speed / generation);
        return avg;
    }
}
