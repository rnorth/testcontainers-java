# Waiting for containers to start or be ready

## Wait strategies vs Startup strategies

## Wait Strategies

Ordinarily Testcontainers will wait for up to 60 seconds for the container's first mapped network port to start listening.

This simple measure provides a basic check whether a container is ready for use.

If the default 60s timeout is not sufficient, it can be altered with the `withStartupTimeout()` method.

If waiting for a listening TCP port is not sufficient to establish whether the container is ready, you can use the
`waitingFor()` method with other `WaitStrategy` implementations as shown below.

#### Waiting for startup examples

You can choose to wait for an HTTP(S) endpoint to return a particular status code.

Waiting for 200 OK:
```java
@ClassRule
public static GenericContainer elasticsearch =
    new GenericContainer("elasticsearch:2.3")
               .withExposedPorts(9200)
               .waitingFor(Wait.forHttp("/all"));
```

Wait for 200 or 401 status codes on an HTTPS endpoint:
```java
@ClassRule
public static GenericContainer elasticsearch =
    new GenericContainer("elasticsearch:2.3")
               .withExposedPorts(9200)
               .waitingFor(
               		Wait.forHttp("/all")
               			 .forStatusCode(200)
               			 .forStatusCode(401)
               			 .usingTls());
```

Wait for 200...299 or 401 status codes on an HTTPS endpoint:
```java
@ClassRule
public static GenericContainer elasticsearch =
    new GenericContainer("elasticsearch:2.3")
               .withExposedPorts(9200)
               .waitingFor(
               		Wait.forHttp("/all")
               			 .forStatusCodeMatching(it -> it >= 200 && it < 300 || it == 401)
               			 .usingTls());
```

If the used image supports Docker's [Healthcheck](https://docs.docker.com/engine/reference/builder/#healthcheck) feature, you can directly leverage the `healthy` state of the container as your wait condition:
```java
@ClassRule
public static GenericContainer container =
    new GenericContainer("image-with-healthcheck:4.2")
               .waitingFor(Wait.forHealthcheck());
```

For futher options, check out the `Wait` convenience class, or the various subclasses of `WaitStrategy`. If none of these options
meet your requirements, you can create your own subclass of `AbstractWaitStrategy` with an appropriate wait
mechanism in `waitUntilReady()`. The `GenericContainer.waitingFor()` method accepts any valid `WaitStrategy`.

## Startup Strategies