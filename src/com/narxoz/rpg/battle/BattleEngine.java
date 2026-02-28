package com.narxoz.rpg.battle;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public final class BattleEngine {
    private static BattleEngine instance;
    private Random random = new Random(1L);

    private BattleEngine() {}

    public static BattleEngine getInstance() {
        if (instance == null) {
            instance = new BattleEngine();
        }
        return instance;
    }

    public BattleEngine setRandomSeed(long seed) {
        this.random = new Random(seed);
        return this;
    }

    public void reset() {}

    public EncounterResult runEncounter(List<Combatant> teamA, List<Combatant> teamB) {
        EncounterResult result = new EncounterResult();
        int currentRound = 0;
        result.addLog("Battle started between Heroes and Enemies!");
        while (isAnyAlive(teamA) && isAnyAlive(teamB)) {
            currentRound++;
            result.addLog("\n--- Round " + currentRound + " ---");
            performTurn(teamA, teamB, result);
            if (isAnyAlive(teamB)) {
                performTurn(teamB, teamA, result);
            }
        }
        result.setRounds(currentRound);
        result.setWinner(isAnyAlive(teamA) ? "Team Heroes" : "Team Enemies");
        result.addLog("\nWinner: " + result.getWinner() + " in " + currentRound + " rounds.");

        return result;
    }

    private void performTurn(List<Combatant> attackers, List<Combatant> defenders, EncounterResult result) {
        for (Combatant attacker : attackers) {
            if (!attacker.isAlive()) continue;
            List<Combatant> aliveDefenders = defenders.stream()
                    .filter(Combatant::isAlive)
                    .collect(Collectors.toList());

            if (aliveDefenders.isEmpty()) break;
            Combatant target = aliveDefenders.get(random.nextInt(aliveDefenders.size()));
            int damage = attacker.getAttackPower();

            target.takeDamage(damage);
            result.addLog(attacker.getName() + " attacks " + target.getName() + " for " + damage + " DMG.");
        }
    }

    private boolean isAnyAlive(List<Combatant> team) {
        return team.stream().anyMatch(Combatant::isAlive);
    }
}
