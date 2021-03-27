package nl.altindag.gatekeeper;

import nl.altindag.gatekeeper.exception.GatekeeperException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GatekeeperShould {

    @Test
    void ensureCallerIsAnyOf() {
        class Foo {
            String foo() {
                Gatekeeper.ensureCallerIsAnyOf(GatekeeperShould.class);
                return "foo";
            }
        }

        String foo = new Foo().foo();
        assertThat(foo).isEqualTo("foo");
    }

    @Test
    void ensureCallerIsAnyOfMultipleTypes() {
        class Foo {
            String foo() {
                Gatekeeper.ensureCallerIsAnyOf(ArrayList.class, GatekeeperShould.class);
                return "foo";
            }
        }

        String foo = new Foo().foo();
        assertThat(foo).isEqualTo("foo");
    }

    @Test
    void throwExceptionWhenGatekeeperIsCalledFromACallerThatIsNotSpecifiedAsAllowedCaller() {
        class Foo {
            String foo() {
                Gatekeeper.ensureCallerIsAnyOf(ArrayList.class);
                return "foo";
            }
        }

        Foo foo = new Foo();

        assertThatThrownBy(foo::foo)
                .isInstanceOf(GatekeeperException.class)
                .hasMessageMatching("Class (.*) tried to call a restricted method. " +
                        "Only classes of the type \\[java.util.ArrayList\\] are allowed to call the method \\[foo\\] from " +
                        "class \\[nl.altindag.gatekeeper.GatekeeperShould\\$3Foo\\]");
    }

    @Test
    @SuppressWarnings({"ConstantConditions", "ConfusingArgumentToVarargsMethod"})
    void throwExceptionWhenNullIsSuppliedToTheGatekeeper() {
        assertThatThrownBy(() -> Gatekeeper.ensureCallerIsAnyOf(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("At least one allowed caller class should be present");
    }

    @Test
    void throwExceptionWhenEmptyCallerIsSuppliedToTheGatekeeper() {
        assertThatThrownBy(Gatekeeper::ensureCallerIsAnyOf)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("At least one allowed caller class should be present");
    }

}
