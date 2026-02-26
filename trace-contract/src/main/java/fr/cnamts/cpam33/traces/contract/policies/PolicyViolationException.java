package fr.cnamts.cpam33.traces.contract.policies;

import java.util.List;

public class PolicyViolationException extends RuntimeException {

    private final List<PolicyViolation> violations;

    public PolicyViolationException(String message, List<PolicyViolation> violations) {
        super(message);
        this.violations = List.copyOf(violations);
    }

    public List<PolicyViolation> violations() {
        return violations;
    }



}
