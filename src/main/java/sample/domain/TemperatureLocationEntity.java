package sample.domain;

import com.google.protobuf.Empty;
import io.cloudstate.javasupport.EntityId;
import io.cloudstate.javasupport.eventsourced.*;
import sample.TemperatureTracking;

/** An event sourced entity. */
@EventSourcedEntity
public class TemperatureLocationEntity {
    private final String entityId;
    private String name;
    private int temperature;
    
    public TemperatureLocationEntity(@EntityId String entityId) {
        this.entityId = entityId;
    }

    @CommandHandler
    public Empty setName(TemperatureTracking.SetNameCommand set, CommandContext ctx) {
        String newName = set.getName();
        if (newName.isEmpty()) {
            throw ctx.fail("The name may not be empty.");
        }
        Temperaturelocation.NameSetEvent event = Temperaturelocation.NameSetEvent.newBuilder()
                .setName(newName)
                .build();
        ctx.emit(event);
        return Empty.getDefaultInstance();
    }

    @EventHandler
    public void nameSet(Temperaturelocation.NameSetEvent set) {
        this.name = set.getName();
    }

    @CommandHandler
    public Empty setTemperature(TemperatureTracking.SetTemperatureCommand set, CommandContext ctx) {
        Temperaturelocation.TemperatureSetEvent event = Temperaturelocation.TemperatureSetEvent.newBuilder()
                .setTemperature(set.getValue())
                .build();
        ctx.emit(event);
        return Empty.getDefaultInstance();
    }

    @EventHandler
    public void temperatureSet(Temperaturelocation.TemperatureSetEvent set) {
        this.temperature = set.getTemperature();
    }
    
    @CommandHandler
    public TemperatureTracking.TemperatureState getValue(TemperatureTracking.GetTemperatureCommand getValueCommand) {
        return TemperatureTracking.TemperatureState.newBuilder()
                .setEntityId(entityId)
                .setName(this.name)
                .setValue(this.temperature)
                .build();
    }
}
