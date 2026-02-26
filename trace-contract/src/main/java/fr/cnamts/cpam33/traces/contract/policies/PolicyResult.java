package fr.cnamts.cpam33.traces.contract.policies;

import java.util.ArrayList;
import java.util.List;

public final class PolicyResult {

    private final List<PolicyViolation> violations;

    private PolicyResult(List<PolicyViolation> violations) {
        this.violations = List.copyOf(violations);
    }

    public static PolicyResult ok() {
        return new PolicyResult(List.of());
    }

    public static PolicyResult ko(PolicyViolation violation) {
        return new PolicyResult(List.of(violation));
    }

    public boolean isOk() {
        return violations.isEmpty();
    }

    public List<PolicyViolation> violations() {
        return violations;
    }

    public PolicyResult merge(PolicyResult other) {
        if (this.isOk() && other.isOk()) return ok();
        List<PolicyViolation> merged = new ArrayList<>(this.violations.size() + other.violations.size());
        merged.addAll(this.violations);
        merged.addAll(other.violations);
        return new PolicyResult(merged);
    }

    public PolicyResult orThrow(RuntimeException ex) {
        if (!isOk()) throw ex;
        return this;
    }

    public PolicyViolationException toException(String summary) {
        return new PolicyViolationException(summary, violations);
    }
}

