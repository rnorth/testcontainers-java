# Advanced options

## Container labels

To add a custom label to the container, use `withLabel`:
```java
new GenericContainer(...)
        .withLabel("your.custom", "label")
```




## Customizing the container

It is possible to use the [`docker-java`](https://github.com/docker-java/docker-java) API directly to customize containers before creation. This is useful if there is a need to use advanced Docker features that are not exposed by the Testcontainers API. Any customizations you make using `withCreateContainerCmdModifier` will be applied _on top_ of the container definition that Testcontainers creates, but before it is created.

For example, this can be used to change the container hostname:
```java
new GenericContainer<>("redis:3.0.2")
        .withCreateContainerCmdModifier(cmd -> cmd.withHostName("the-cache"))
        ...
```

... or modify container memory (see [this](https://fabiokung.com/2014/03/13/memory-inside-linux-containers/) if it does not appear to work):
```java
new GenericContainer<>("alpine:3.3")
        .withCreateContainerCmdModifier(cmd -> cmd.withMemory((long) 4 * 1024 * 1024))
        .withCreateContainerCmdModifier(cmd -> cmd.withMemorySwap((long) 4 * 1024 * 1024))
        ...
```

> It is recommended to use this sparingly, and follow changes to the `docker-java` API if you choose to use this. It is typically quite stable, though.

For what is possible, consult the [`docker-java CreateContainerCmd` source code](https://github.com/docker-java/docker-java/blob/master/src/main/java/com/github/dockerjava/api/command/CreateContainerCmd.java)
