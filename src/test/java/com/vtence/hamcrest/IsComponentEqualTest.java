package com.vtence.hamcrest;

import org.hamcrest.Matcher;
import org.junit.Test;

import javax.persistence.Embeddable;

import static com.vtence.hamcrest.IsComponentEqual.componentEqualTo;
import static com.vtence.hamcrest.SamePersistentFieldsAs.samePersistentFieldsAs;

public class IsComponentEqualTest extends AbstractMatcherTest {

    Value aValue = new Value("expected");
    Value aMatchingValue = new Value("expected");
    ExampleComponent expectedComponent = new ExampleComponent("same", 1, aValue);
    ExampleComponent shouldMatch = new ExampleComponent("same", 1, aMatchingValue);

    @Override
    protected Matcher<?> createMatcher() {
        return componentEqualTo(expectedComponent);
    }

    @Test
    public void
    matchesWhenAllPersistentFieldsMatch() {
      assertMatches("does not match component", componentEqualTo(expectedComponent), shouldMatch);
    }

    @Test
    public void
    matchesTwoNullComponents() {
      assertMatches("null does not match null", componentEqualTo(null), null);
    }

    @Test
    public void
    matchesNullToAComponentWithNullProperties() {
      assertMatches("null does not match",
          componentEqualTo(null), new ExampleComponent(null, null, null));
    }

    @Test
    public void
    matchesAComponentWithNullPropertiesToNull() {
      assertMatches("does not match null",
              componentEqualTo(new ExampleComponent(null, null, null)), null);
    }

    @Test
    public void
    hasHumanReadableDescription() {
        assertDescription("with fields [string: \"same\", integer: <1>, value: <expected>]", samePersistentFieldsAs(expectedComponent));
    }

    @Test
    public void
    reportsMismatchWhenComponentIsNull() {
      assertMismatchDescription("is null",
              componentEqualTo(expectedComponent), null);
    }

    @Test
    public void
    describesNullExpectationClearly() {
      assertDescription("null", componentEqualTo(null));
    }

    public static class Value {
        public Value(Object value) {
            this.value = value;
        }

        public final Object value;

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            return value.equals(((Value) o).value);
        }
    }

    @Embeddable
    public static class ExampleComponent {

        private String string;
        private Integer integer;
        private Value value;

        public ExampleComponent(String string, Integer integer, Value value) {
            this.string = string;
            this.integer = integer;
            this.value = value;
        }
    }
}