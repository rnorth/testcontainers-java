# Commands and executions

## Container startup command

By default the container will execute whatever command is specified in the image's Dockerfile. To override this, and specify a different command, use `withCommand`:
```java
new GenericContainer(...)
        .withCommand("/app/start.sh")
```


## Executing a command

Your test can execute a command inside a running container, similar to a `docker exec` call:
```java
myContainer.execInContainer("touch", "/tmp/foo");
```

This can be useful for software that has a command line administration tool. You can also get the output from the command:
```java
ExecResult result = myContainer.execInContainer("tail", "-1", "/var/logs/foo");
assertThat(result.getStdout().contains("message"));
```

There are two limitations:
* There's no way to get the return code of the executed command
* This isn't supported if your docker daemon uses the older "lxc" execution engine.

## Environment variables

To add environment variables to the container, use `withEnv`:
```java
new GenericContainer(...)
		.withEnv("API_TOKEN", "foo")
```
