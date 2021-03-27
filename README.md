[![Actions Status](https://github.com/Hakky54/gatekeeper/workflows/Build/badge.svg)](https://github.com/Hakky54/gatekeeper/actions)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=io.github.hakky54%3Agatekeeper&metric=security_rating)](https://sonarcloud.io/dashboard?id=io.github.hakky54%3Agatekeeper)
[![Known Vulnerabilities](https://snyk.io/test/github/Hakky54/gatekeeper/badge.svg)](https://snyk.io/test/github/Hakky54/gatekeeper)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=io.github.hakky54%3Agatekeeper&metric=coverage)](https://sonarcloud.io/dashboard?id=io.github.hakky54%3Agatekeeper)
[![Apache2 license](https://img.shields.io/badge/license-Aache2.0-blue.svg)](https://github.com/Hakky54/gatekeeper/blob/master/LICENSE)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.hakky54/gatekeeper/badge.svg)](https://mvnrepository.com/artifact/io.github.hakky54/gatekeeper)
[![javadoc](https://javadoc.io/badge2/io.github.hakky54/gatekeeper/javadoc.svg)](https://javadoc.io/doc/io.github.hakky54/gatekeeper)

[![SonarCloud](https://sonarcloud.io/images/project_badges/sonarcloud-white.svg)](https://sonarcloud.io/dashboard?id=io.github.hakky54%3Agatekeeper)

# Gatekeeper 🔐 [![Tweet](https://img.shields.io/twitter/url/http/shields.io.svg?style=social)](https://twitter.com/intent/tweet?text=Easily%20protect%20your%20publicly%20accessible%20internal%20implementations%20with%20Gatekeeper&url=https://github.com/Hakky54/gatekeeper&via=hakky541&hashtags=security,developer,java,gatekeeper)
![braces-clipart-punctuation-6-original](img/gatekeepers.jpg)

## Introduction
Gatekeeper is a library which guards your publicly accessible internal implementations.

### History
Library maintainers have a hard time to keep internal implementations internal and prevent it to be accessible by library users.
The library maintainer cannot easily change internal implementations as it may lead into breaking changes for the library users.
A straight forward method for library maintainers would be making classes package protected and accessible through public api's so-called Service classes.
Encapsulation internal api's and exposing it through service classes is the preferred way but cannot be achieved in some use cases. Restrictions by Java Modules will not work if the end-user is not using java modules.
Gatekeeper will ensure that these internal implementations remain internal by validating the caller. You as a library developer can choose who is allowed to call your classes.

### Minimum requirements:
- Java 8

# Install library with:
### Install with [Maven](https://mvnrepository.com/artifact/io.github.hakky54/gatekeeper)
```xml
<dependency>
    <groupId>io.github.hakky54</groupId>
    <artifactId>gatekeeper</artifactId>
    <version>1.0.3</version>
</dependency>
```
### Install with Gradle
```groovy
implementation 'io.github.hakky54:gatekeeper:1.0.2'
```
### Install with Scala SBT
```
libraryDependencies += "io.github.hakky54" % "gatekeeper" % "1.0.2"
```
### Install with Apache Ivy
```xml
<dependency org="io.github.hakky54" name="gatekeeper" rev="1.0.2" />
```

## Usage
**Internal API - Protected by Gatekeeper**
```java
import nl.altindag.gatekeeper.Gatekeeper;

public class FooInternal {

    public String hello() {
        Gatekeeper.ensureCallerIsAnyOf(FooService.class);
        return "Hello";
    }
}
```
**Public API - Exposed**
```java
public class FooService {
    
    private final FooInternal fooInternal = new FooInternal();

    public String hello() {
        return fooInternal.hello();
    }
}
```
```java
public class App {

    public static void main(String[] args) {
        FooService fooService = new FooService();
        String hello = fooService.hello();
        System.out.println(hello); // ---> prints Hello
        
        FooInternal fooInternal = new FooInternal();
        fooInternal.hello(); // ---> throws a (runtime) GatekeeperException with an explaining message
    }
    
}
```
## Bulletproof?
This library provides a protection by checking the caller at runtime, but it is sadly not 100% bulletproof. It just makes it a bit tougher for the end-user to use the protected classes. 
Unfortunately this protection can be bypassed with two methods:
1. Calling the protected class with a class that has the same class name and package to specified allowable class.
2. Overriding the jar with a modified version which doesn't validate at all.

You should know about these use cases before considering using this library. It cannot give 100% protection if the end-user is using one of the two methods to bypass it. 
If you are ok with that, go ahead or else I would recommend to use java's Sealed classes which are available from Java 15 onwards.

## Contributing

There are plenty of ways to contribute to this project:

* Give it a star
* Share it with a [![Tweet](https://img.shields.io/twitter/url/http/shields.io.svg?style=social)](https://twitter.com/intent/tweet?text=Easily%20protect%20your%20publicly%20accessible%20internal%20implementations%20with%20Gatekeeper&url=https://github.com/Hakky54/gatekeeper&via=hakky541&hashtags=security,developer,java,gatekeeper)
* Submit a PR

## Resources
- [Gatekeeper image](https://innovationcloud.com/blog/the-gatekeepers-heavy-burden-of-decision-making-explained.html)
