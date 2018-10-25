# Creating a container

## Creating a generic container based on an image

Testcontainers' generic container support offers the most flexibility, and makes it easy to use virtually any container
images as temporary test dependencies. For example, if you might use it to test interactions with:

* NoSQL databases or other data stores (e.g. redis, elasticsearch, mongo)
* Web servers/proxies (e.g. nginx, apache)
* Log services (e.g. logstash, kibana)
* Other services developed by your team/organization which are already dockerized

### Example

A generic container rule can be used with any public docker image; for example:
```java
// Set up a redis container
@ClassRule
public static GenericContainer redis =
	new GenericContainer("redis:3.0.2")
               .withExposedPorts(6379);


// Set up a plain OS container and customize environment, 
//   command and exposed ports. This just listens on port 80 
//   and always returns '42'
@ClassRule
public static GenericContainer alpine =
	new GenericContainer("alpine:3.2")
    		.withExposedPorts(80)
               .withEnv("MAGIC_NUMBER", "42")
               .withCommand("/bin/sh", "-c", 
               "while true; do echo \"$MAGIC_NUMBER\" | nc -l -p 80; done");
```

These containers, as `@ClassRule`s, will be started before any tests in the class run, and will be destroyed after all
tests have run.


With a generic container, you set the container image using a parameter to the rule constructor, e.g.:
```java
new GenericContainer("jboss/wildfly:9.0.1.Final")
```

