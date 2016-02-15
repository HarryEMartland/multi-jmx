# Multi-JMX
**Continuous Integration:** [![wercker status](https://app.wercker.com/status/e7b9fd49750cf96037dcbd3b0bda6c3d/s/master "wercker status")](https://app.wercker.com/project/bykey/e7b9fd49750cf96037dcbd3b0bda6c3d)

JMX client for multiple connections.

This command line tool allows you to connect to multiple MBean servers at once and compare the results from multiple MBeans.
Connections can be made over a network or using local process sockets.

MBean attributes along with method calls can be used.
Method calls are denoted with the syntax ``<methodName>(<arg1>,<arg2>, ... ,<argx>)`` and the arguments are specified using SpEL.
The type of the argument is used to specify which argument is called on the mbean so casting may be required.
To specify a literal type wrap the value in one of the wrappers below.

## Literal arguments
To specify a literal argument the following wrappers can be used.
* BooleanType(Boolean value)
* CharType(Character value)
* DoubleType(Double value)
* FloatType(Double value)
* IntType(Integer value)
* LongType(Long value)
* NullType(Class type)

e.g.
``methodName(new IntType(32))``

note: when using the null type use the full canonical name for the class and use the SpEL expression ``T(<class name>)``.

## Multiple values
It is possible to request multiple values from the same or different MBeans.
If the values are from different MBeans you must provide the mbean for each attribute/method.
To specify an MBean use the -o and then specify the ObjectName.
To call a method or attribute add the -a parameter then the name of the attribute or the method stub.

## Ordering of Values
When dealing with a number of servers it is often only then high or low values which you are interested in.
The argument -v will order the results returned from the MBean, the order will be determined by the compareTo function on the returned object.
High value will be at the bottom where the will be visible first on the command line.
To reverse the order pass the -r argument.
The default order is the order that the connections were specified however it is possible to order the connections by their string value by using the -c argument.

## Help
To view the help text run the jar with no arguments or pass the -h argument
```
usage: multi-jmx -a <attribute> [-c] [-d <delimiter>] [-f <file path>] [-h] -o <object name> [-p <password>] [-r] [-t <number of threads>] [-u <username>] [-v]
     -a,--attribute <attribute>       JMX attribute to read from e.g. 'AvailableProcessors'
     -c,--order-connection            Order the results by connection
     -d,--delimiter <delimiter>       Delimiter used to split results
     -f,--file <file path>            Read JMX connections from file
     -h,--help                        Display help
     -o,--object-name <object name>   JMX object name to read from e.g. 'java.lang:type=OperatingSystem'
     -p,--password <password>         Password to connect to JMX server
     -r,--reverse-order               Order the results in reverse
     -t <number of threads>           Maximum number of threads (default unlimited)
     -u,--username <username>         Username to connect to JMX server
     -v,--order-value                 Order the results by value
```


#### Attribute Example
    java -jar target/multi-jmx-1.0-SNAPSHOT-jar-with-dependencies.jar -o java.lang:type=OperatingSystem -a AvailableProcessors <pid or url>

#### Method Call Example
    java -jar target/multi-jmx-1.0-SNAPSHOT-jar-with-dependencies.jar -o com.sun.management:type=DiagnosticCommand -a vmVersion() <pid or url>

#### Method Call Example (With Arguments)
    java -jar target/multi-jmx-1.0-SNAPSHOT-jar-with-dependencies.jar -o com.sun.management:type=DiagnosticCommand -a vmUptime() <pid or url>
