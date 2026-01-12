# order-service — Debug / Run instructions

This note explains how to run `order-service` in debug mode (containerized) and how to attach an IDE debugger.

Files
- `docker-compose.debug.yml` — local override to expose JDWP debug port 5005 and start the JVM with JDWP (suspend=y by default).

Quick commands (PowerShell)

Start `order-service` in debug (with dependencies from main compose):
```powershell
cd C:\Users\Stavr\order-tracking-microservices
# Starts order-service with the debug override; the JVM will wait for debugger on port 5005
docker compose -f docker-compose.yml -f order-service\docker-compose.debug.yml up --build -d order-service
```

Stop and tear down:
```powershell
cd C:\Users\Stavr\order-tracking-microservices
docker compose -f docker-compose.yml -f order-service\docker-compose.debug.yml down
```

Attach your IDE debugger
- Host: `localhost`
- Port: `5005`
- Create a Remote JVM Debug configuration in your IDE and attach.

Notes
- The debug configuration uses `suspend=y` so the JVM will pause on startup until a debugger attaches. If you prefer the JVM to run immediately and allow attaching later, edit `order-service/docker-compose.debug.yml` and change `suspend=y` → `suspend=n`.
- If port `5005` is already in use on the host, change the host side of the mapping in `docker-compose.debug.yml` (e.g. `5007:5005`) and attach your debugger to the chosen host port.
- If you prefer to debug locally (without Docker), open the `order-service` module in your IDE and run a Spring Boot `Debug` configuration against the `OrderserviceApplication` main class.

Helpful commands
```powershell
# Build locally (requires JDK and JAVA_HOME)
.\order-service\mvnw.cmd -f order-service\pom.xml -DskipTests clean package

# Build the image using compose
docker compose build order-service

# One-off run from image (exposes debug port 5005)
docker run --rm -p 8080:8080 -p 5005:5005 --name order-service-debug order-tracking-microservices-order-service \
  java -agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=*:5005 -jar app.jar
```

Troubleshooting
- Ensure Docker Desktop/daemon is running: `docker info`
- If containers fail to start, view logs: `docker compose logs -f --tail 200 order-service`
- If `mvnw` complains about JAVA_HOME, set `$env:JAVA_HOME` to your JDK path before running local builds.

