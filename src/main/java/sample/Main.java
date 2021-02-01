package sample;

import io.cloudstate.javasupport.*;
import sample.domain.TemperatureLocationEntity;

public final class Main {

    public static void main(String[] args) throws Exception {
        new CloudState()
                .registerEventSourcedEntity(
                        TemperatureLocationEntity.class,
                        TemperatureTracking.getDescriptor().findServiceByName("TemperatureTrackingService")
                )
                .start().toCompletableFuture().get();
    }

}
