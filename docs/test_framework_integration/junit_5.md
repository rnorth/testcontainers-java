# Jupiter / JUnit 5

## Extension

Jupiter integration is provided by means of the `@Testcontainers` annotation.
  
The extension finds all fields that are annotated with `@Container` and calls their container lifecycle 
methods (methods on the `Startable` interface). Containers declared as static fields will be shared between test 
methods. They will be started only once before any test method is executed and stopped after the last test method has 
executed. Containers declared as instance fields will be started and stopped for every test method.
  
**Note:** This extension has only be tested with sequential test execution. Using it with parallel test execution is 
unsupported and may have unintended side effects.
  
*Example:*
```java
@Testcontainers
class MyTestcontainersTests {
   
     // will be shared between test methods
    @Container
    private static final MySQLContainer MY_SQL_CONTAINER = new MySQLContainer();
    
     // will be started before and stopped after each test method
    @Container
    private PostgreSQLContainer postgresqlContainer = new PostgreSQLContainer()
            .withDatabaseName("foo")
            .withUsername("foo")
            .withPassword("secret");
    @Test
    void test() {
        assertTrue(MY_SQL_CONTAINER.isRunning());
        assertTrue(postgresqlContainer.isRunning());
    }
}
```
