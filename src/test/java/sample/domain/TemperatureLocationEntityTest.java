package sample.domain;

import io.cloudstate.javasupport.eventsourced.CommandContext;
import org.junit.Test;
import org.mockito.Mockito;
import sample.TemperatureTracking;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TemperatureLocationEntityTest {
    private String entityId = "entityId1";
    private CommandContext context = Mockito.mock(CommandContext.class);

    @Test
    public void setNameCommandTest() {
        TemperatureLocationEntity entity = new TemperatureLocationEntity(entityId);

        String newName = "lower floor";

        entity.setName(TemperatureTracking.SetNameCommand.newBuilder().setEntityId(entityId).setName(newName).build(), context);

        Temperaturelocation.NameSetEvent event = Temperaturelocation.NameSetEvent.newBuilder()
                .setName(newName)
                .build();
        Mockito.verify(context).emit(event);
    }

    @Test
    public void setTemperatureCommandTest() {
        TemperatureLocationEntity entity = new TemperatureLocationEntity(entityId);

        int newTemperature = 15;

        entity.setTemperature(TemperatureTracking.SetTemperatureCommand.newBuilder().setEntityId(entityId).setValue(newTemperature).build(), context);

        Temperaturelocation.TemperatureSetEvent event = Temperaturelocation.TemperatureSetEvent.newBuilder()
                .setTemperature(newTemperature)
                .build();
        Mockito.verify(context).emit(event);
    }

    @Test
    public void setNameAndTemperatureEventTest() {
        TemperatureLocationEntity entity = new TemperatureLocationEntity(entityId);
        String name = "unit-test";
        int temperature = -4;

        // set state
        entity.nameSet(Temperaturelocation.NameSetEvent.newBuilder().setName(name).build());
        entity.temperatureSet(Temperaturelocation.TemperatureSetEvent.newBuilder().setTemperature(temperature).build());

        // validate state
        TemperatureTracking.TemperatureState state = entity.getValue(TemperatureTracking.GetTemperatureCommand.newBuilder().setEntityId(entityId).build());
        assertThat(state.getEntityId(), is(entityId));
        assertThat(state.getName(), is(name));
        assertThat(state.getValue(), is(temperature));
    }
}
