package fr.pelt10.kubithon.hub.cosmetic.pets;

import com.flowpowered.math.vector.Vector3d;
import net.minecraft.entity.EntityLiving;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.ai.task.AITask;
import org.spongepowered.api.entity.ai.task.AITaskTypes;
import org.spongepowered.api.entity.ai.task.AbstractAITask;
import org.spongepowered.api.entity.living.Creature;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

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
        if(!player.isOnline()) {
            getOwner().ifPresent(agent -> {
                agent.remove();
                return;
            });
        }

        getOwner().ifPresent(agent -> {
            if(player.getLocation().getPosition().distance(agent.getLocation().getPosition()) > 10
                    && player.isOnGround())
                agent.setLocation(player.getLocation());

            Location<World> location = player.getLocation().add(player.getRotation().normalize().mul(10));
            ((EntityLiving)agent).getNavigator().tryMoveToXYZ(location.getX(), location.getY(), location.getZ(), 1.3);
        });
    }

    public boolean continueUpdating() {
        return true;
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
