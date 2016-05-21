Template - Embedded Jetty/SpringMVC/Thymeleaf Application
=========================================================

## Running
### With Maven
    mvn exec:java -Dexec.mainClass=ca.unx.template.Main

### Basic Jar
The default package goal will build a jar in the target directory with
dependency jars copied into target/lib.
    mvn package
    java -jar target/jetty-springmvc-thymeleaf-template-0.0.1-SNAPSHOT.jar

### Fat Jar
If you would rather have a single jar that contains all the
dependencies, use the 'fatjar' profile.
    mvn -Pfatjar package
    java -jar target/jetty-springmvc-thymeleaf-template-0.0.1-SNAPSHOT.jar