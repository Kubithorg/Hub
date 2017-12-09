package fr.pelt10.kubithon.hub.cosmetic.pets;

import org.spongepowered.api.entity.ai.task.AITask;
import org.spongepowered.api.entity.ai.task.AITaskTypes;
import org.spongepowered.api.entity.ai.task.AbstractAITask;
import org.spongepowered.api.entity.living.Creature;
import org.spongepowered.api.entity.living.player.Player;

public class CustomAITask extends AbstractAITask<Creature> {
    private Player player;

    public CustomAITask(Player player) {
        super(AITaskTypes.RUN_AROUND_LIKE_CRAZY);
        this.player = player;
    }

    public void start() {
    }

    public boolean shouldUpdate() {
        return true;
    }

    public void update() {
        getOwner().ifPresent(agent -> agent.setTarget(player));
    }

    public boolean continueUpdating() {
        return false;
    }

    public void reset() {
    }

    public boolean canRunConcurrentWith(AITask other) {
        return false;
    }

    public boolean canBeInterrupted() {
        return false;
    }
}
