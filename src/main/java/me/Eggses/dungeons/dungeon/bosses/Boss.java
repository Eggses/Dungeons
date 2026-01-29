package me.Eggses.dungeons.dungeon.bosses;

import me.Eggses.dungeons.dungeon.bosses.phases.PhaseController;
import me.Eggses.dungeons.dungeon.bosses.mechanics.CleanUp;
import me.Eggses.dungeons.dungeon.players.Players;
import me.Eggses.dungeons.entities.attributes.AttributeController;
import me.Eggses.dungeons.entities.mobs.DungeonEntity;
import me.Eggses.dungeons.entities.mobs.DungeonMob;
import me.Eggses.dungeons.entities.mobs.EntityManager;
import me.Eggses.dungeons.entities.nameutility.MobName;
import me.Eggses.dungeons.eventhandler.EventBehaviour;
import me.Eggses.dungeons.eventhandler.EventContext;
import me.Eggses.dungeons.eventhandler.EventDefinition;
import me.Eggses.dungeons.eventhandler.EventManager;
import me.Eggses.dungeons.tasks.ActiveTasks;
import me.Eggses.dungeons.tasks.Task;
import me.Eggses.dungeons.tasks.TaskContext;
import me.Eggses.dungeons.tasks.TaskRunner;
import me.Eggses.dungeons.utility.text.MessageCreator;
import me.Eggses.dungeons.utility.text.TextFormatter;
import net.kyori.adventure.text.Component;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Bosses
 * <p>
 * Mechanics are added in Phases. Some mechanics end on that phase, some do not, and start appearing
 * from that phase onwards. To add a Task based mechanic, if your code has a {@code TaskContext<Boss>},
 * use that to add tasks. If you are working with an {@code EventBehaviour<E>} that lacks this object,
 * use the Bosses method {@code addOneOffTask(Task<Boss> task)} to add a {@code Task<Boss>} that will run.
 * <p>
 * For mechanics that execute instantly, then run a delayed task, such as a Bomb that targets N players,
 * after 5 seconds bomb detonates, this task should not schedule a task to find N players, instead,
 * the players should be found in the core lambda, then the delayed explosion is made using a task.
 */
public class Boss implements DungeonEntity {

    private final DungeonMob bossMob;
    private final BossBarController bossBarController;
    private final World world;

    private final Component bossName;
    private final Component bossChatName;
    private final String colourScheme;
    private final MessageCreator messageCreator;

    private final double maxHealth;
    private double health;

    private final PhaseController phaseController;
    private final EventManager phaseEventManager;
    private final ActiveTasks phaseActiveTasks;

    private final TaskContext<Boss> bossMobFullContext;
    private final TaskContext<Boss> bossPhaseContext;

    private final Players players;
    private final List<CleanUp> cleanUps;

    private final Runnable onBossDeath;

    public Boss(DungeonBossBuilder builder,
                EntityManager entityManager,
                World world,
                Players players,
                TaskRunner taskRunner,
                MessageCreator messageCreator,
                TextFormatter textFormatter,
                Runnable onBossDeath) {

        this.bossMob = new DungeonMob(
                builder.getMobBuilder(),
                world,
                taskRunner,
                messageCreator,
                textFormatter
        );
        this.world = world;
        this.messageCreator = messageCreator;
        this.bossName = builder.getBossName();
        this.colourScheme = builder.getColourScheme();
        this.bossBarController = new BossBarController(this, colourScheme, messageCreator, builder.getStyle());

        Component prefix = messageCreator.createMessage(colourScheme + "[");
        Component suffix = messageCreator.createMessage(colourScheme + "]<gray>: ");
        this.bossChatName = prefix.append(bossName).append(suffix);
        this.maxHealth = builder.getHealth();
        this.health = maxHealth;

        this.phaseController = new PhaseController(this, builder.getPhases());
        this.phaseEventManager = new EventManager();
        this.phaseActiveTasks = new ActiveTasks();

        this.bossMobFullContext = new TaskContext<>(this, bossMob.getActiveTasks(), taskRunner);
        this.bossPhaseContext = new TaskContext<>(this, phaseActiveTasks, taskRunner);
        this.players = players;
        this.cleanUps = builder.getCleanUps();
        this.onBossDeath = onBossDeath;

        takeDamage(0.0);
        entityManager.addMob(this);
    }

    public double getHealth() {
        return health;
    }

    public double getRemainingHealthPercentage() {
        return (health / maxHealth) * 100.0;
    }

    private void tryChangePhase() {
        phaseController.attemptToChangePhase(getRemainingHealthPercentage(), bossPhaseContext);
    }

    public void cleanUpOnPhaseEnd() {
        phaseEventManager.removeAll();
        phaseActiveTasks.endAllTasks();
    }

    public void failBossFight() {
        endTasks();
        getEntity().remove();
        bossBarController.removeAllViewers();
        cleanUps.forEach(CleanUp::cleanUp);
    }

    public void tryEndBossFight() {
        if (health <= 0) defeatBossFight();
    }

    private void defeatBossFight() {
        getEntity().setHealth(0.0);
        endTasks();
        bossBarController.removeAllViewers();
        cleanUps.forEach(CleanUp::cleanUp);
        onBossDeath.run();
    }

    public boolean isInFight(Player player) {
        return players.contains(player);
    }

    public Set<Player> getPlayersInFight() {
        return players.getPlayers();
    }

    public <E extends Event> void addPhaseEvent(EventDefinition<E> eventDefinition) {
        phaseEventManager.addEventBehaviour(eventDefinition.eventClass(), eventDefinition.createEventBehaviour().get());
    }

    public <E extends Event> void addPermanentEvent(EventDefinition<E> eventDefinition) {
        addEvent(eventDefinition.eventClass(), eventDefinition.createEventBehaviour().get());
    }

    public void addOneOffTask(Task<Boss> task) {
        task.runTask(bossMobFullContext);
    }

    public void addBossBarViewer(Player player) {
        bossBarController.addViewer(player);
    }

    public void removeBossBarViewer(Player player) {
        bossBarController.removeViewer(player);
    }

    public World getBossWorld() {
        return world;
    }

    public Component createMessage(String message) {
        Component finalMessage = messageCreator.createMessage(colourScheme + message);
        return bossChatName.append(finalMessage);
    }

    @Override
    public UUID getUUID() {
        return bossMob.getUUID();
    }

    @Override
    public LivingEntity getEntity() {
        return bossMob.getEntity();
    }

    @Override
    public void endTasks() {
        bossMob.endTasks();
        phaseActiveTasks.endAllTasks();
    }

    @Override
    public AttributeController getAttributeController() {
        return bossMob.getAttributeController();
    }

    @Override
    public <E extends Event> void addEvent(Class<E> eventClass, EventBehaviour<E> eventBehaviour) {
        bossMob.addEvent(eventClass, eventBehaviour);
    }

    @Override
    public <E extends Event> void handleEvent(E event, EventContext eventContext) {
        phaseEventManager.handleEvent(event, eventContext);
        bossMob.handleEvent(event, eventContext);
    }

    @Override
    public int getDungeonLevel() {
        return bossMob.getDungeonLevel();
    }

    @Override
    public MobName getMobName() {
        return null;
    }

    @Override
    public void takeDamage(double damage) {
        health = Math.max(0.0, health - damage);

        String healthPart = "<white> ⟡ </white>" + "<dark_green>" + String.format("%.0f", health) + "<gray>ʜᴘ</gray>";
        Component newName = bossName.append(messageCreator.createMessage(healthPart));
        bossMob.getEntity().customName(newName);

        bossBarController.update();
        tryChangePhase();
    }
}
