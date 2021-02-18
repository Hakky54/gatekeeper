/*
 * Copyright 2021-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nl.altindag.gatekeeper;

import nl.altindag.gatekeeper.exception.GatekeeperException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Hakan Altindag
 */
public final class Gatekeeper {

    private Gatekeeper() {}

    @SuppressWarnings("rawtypes")
    public static void ensureCallerIsAnyOf(Class... allowedCallerClasses) {
        if (allowedCallerClasses == null || allowedCallerClasses.length == 0) {
            throw new IllegalArgumentException("At least one allowed caller class should be present");
        }

        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StackTraceElement caller = stackTrace[3];
        StackTraceElement target = stackTrace[2];

        List<Class> classes = Arrays.asList(allowedCallerClasses);

        if (classes.stream().anyMatch(allowedCallerClass -> !allowedCallerClass.getName().equals(caller.getClassName()))) {
            throw new GatekeeperException(String.format(
                    "Class [%s] tried to call a restricted method. Only classes of the type [%s] are allowed to call the method [%s] from class [%s]",
                    caller.getClassName(),
                    classes.stream().map(Class::getName).collect(Collectors.joining(", ")),
                    target.getMethodName(),
                    target.getClassName()));
        }
    }

}