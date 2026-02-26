package fr.cnamts.cpam33.traces.contract.policies;


import java.time.Duration;
import java.util.List;
import java.util.Objects;

public record TracePolicyOptions(
        List<String> allowedSchemas,
        Duration futureTolerance
) {
    public TracePolicyOptions {
        Objects.requireNonNull(allowedSchemas, "allowedSchemas");
        Objects.requireNonNull(futureTolerance, "futureTolerance");
    }

    public static TracePolicyOptions defaults() {
        return new TracePolicyOptions(List.of("1", "2"), Duration.ofMinutes(2));
    }

}
