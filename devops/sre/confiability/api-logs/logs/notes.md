# Pillars of Observability

## Logs
- **Helps in Root Cause Analysis (RCA)**
- **Reduces Mean Time To Recovery (MTTR)**

## Metrics
- **Golden Signals:**
  - **Latency:** The time it takes for a request to be processed.
  - **Error Rate:** The percentage of requests that result in an error.
  - **Request Rate:** The number of requests per unit of time.
  - **Saturation:** The utilization of resources, such as CPU and memory, relative to their total capacity.

## Tracing
- **Allows following the path of a request through different services and components, identifying where delays or failures occur.**

# Logging Frameworks
## Appenders

In logging frameworks like Logback and Log4j, an **appender** is a component responsible for delivering log messages to their final destination. Each appender defines a specific output target, such as a file, console, database, or remote server. By configuring different appenders, you can control where and how log messages are recorded.

Here are some common types of appenders:

1. **ConsoleAppender**: Sends log messages to the console (standard output or standard error).
2. **FileAppender**: Writes log messages to a file.
3. **RollingFileAppender**: Similar to FileAppender but with the ability to roll over the log files based on size, date, or other criteria.
4. **JDBCAppender**: Logs messages to a database using JDBC.
5. **SMTPAppender**: Sends log messages via email.
6. **SocketAppender**: Sends log messages to a remote server through a socket connection.
7. **AsyncAppender**: Buffers log messages and dispatches them asynchronously to improve performance.

Each appender can have its own configuration, including filters, formatters (layout), and thresholds, allowing for fine-grained control over the logging process. For example, you might use different layouts to format log messages differently for the console and a log file.