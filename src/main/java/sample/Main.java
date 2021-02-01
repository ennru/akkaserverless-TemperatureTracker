package sample;

import io.cloudstate.javasupport.*;
import sample.domain.TemperatureLocationEntity;
import sample.domain.Temperaturelocation;

public final class Main {

    public static void main(String[] args) throws Exception {
        new CloudState()
                .registerEventSourcedEntity(
                        TemperatureLocationEntity.class,
                        TemperatureTracking.getDescriptor().findServiceByName("TemperatureTrackingService"),
                        Temperaturelocation.getDescriptor()
                )
                .start().toCompletableFuture().get();
    }

}
