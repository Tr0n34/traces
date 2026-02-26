package fr.cnamts.cpam33.traces.contract.policies;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

public interface Policy<T> {

    PolicyResult check(T input);

    default Policy<T> and(Policy<T> other) {
        Objects.requireNonNull(other);
        return input -> this.check(input).merge(other.check(input));
    }

    default Policy<T> then(Function<PolicyResult, PolicyResult> post) {
        Objects.requireNonNull(post);
        return input -> post.apply(this.check(input));
    }

    static <T> Policy<T> ofRule(String code, Predicate<? super T> predicate, String message) {
        Objects.requireNonNull(code);
        Objects.requireNonNull(predicate);
        Objects.requireNonNull(message);
        return input -> predicate.test(input)
                ? PolicyResult.ok()
                : PolicyResult.ko(new PolicyViolation(code, message));
    }

    static <T> Policy<T> alwaysOk() {
        return input -> PolicyResult.ok();
    }

    static <T> Policy<T> requireNonBlank(String code, Function<T, String> getter, String message) {
        return ofRule(code, t -> {
            var v = getter.apply(t);
            return v != null && !v.isBlank();
        }, message);
    }

    static <T> Policy<T> requireNonNull(String code, Function<T, ?> getter, String message) {
        return ofRule(code, t -> getter.apply(t) != null, message);
    }

    static <T> Policy<T> requireMaxLength(String code, Function<T, String> getter, int max, String message) {
        return ofRule(code, t -> {
            var v = getter.apply(t);
            return v == null || v.length() <= max;
        }, message);
    }

    static <T> Policy<T> requireOneOf(String code, Function<T, String> getter, List<String> allowed, String message) {
        return ofRule(code, t -> {
            var v = getter.apply(t);
            return v != null && allowed.contains(v);
        }, message);
    }
}