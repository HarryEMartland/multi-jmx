# multi-jmx
JMX client to display values for multiple connections at once

## Example
    java -jar target/multi-jmx-1.0-SNAPSHOT-jar-with-dependencies.jar -o java.lang:type=OperatingSystem -a AvailableProcessors <pid or url>
