# tracking-service — Debug / Run instructions

This note explains how to run `tracking-service` in debug mode (containerized) and how to attach an IDE debugger.

Files
- `docker-compose.debug.yml` — local override to expose JDWP debug (container 5005) mapped to host port 5006, and start the JVM with JDWP (suspend=y by default).

Quick commands (PowerShell)

Start `tracking-service` in debug (with dependencies from main compose):
```powershell
cd C:\Users\Stavr\order-tracking-microservices
# Starts tracking-service with the debug override; the JVM will wait for debugger on host port 5006
docker compose -f docker-compose.yml -f tracking-service\docker-compose.debug.yml up --build -d tracking-service
```

Stop and tear down:
```powershell
cd C:\Users\Stavr\order-tracking-microservices
docker compose -f docker-compose.yml -f tracking-service\docker-compose.debug.yml down
```

Attach your IDE debugger
- Host: `localhost`
- Port: `5006` (tracking-service maps host 5006 -> container 5005)
- Create a Remote JVM Debug configuration in your IDE and attach.

Notes
- The debug configuration uses `suspend=y` so the JVM will pause on startup until a debugger attaches. If you prefer the JVM to run immediately and allow attaching later, edit `tracking-service/docker-compose.debug.yml` and change `suspend=y` → `suspend=n`.
- If port `5006` is already in use on the host, change the host side of the mapping in `docker-compose.debug.yml` (e.g. `5008:5005`) and attach your debugger to that host port.
- If you prefer to debug locally (without Docker), open the `tracking-service` module in your IDE and run a Spring Boot `Debug` configuration against the `TrackingserviceApplication` main class.

Helpful commands
```powershell
# Build locally (requires JDK and JAVA_HOME)
.\tracking-service\mvnw.cmd -f tracking-service\pom.xml -DskipTests clean package

# Build the image using compose
docker compose build tracking-service

# One-off run from image (maps container 5005 -> host 5006)
docker run --rm -p 8081:8081 -p 5006:5005 --name tracking-service-debug order-tracking-microservices-tracking-service \
  java -agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=*:5005 -jar app.jar
```

Troubleshooting
- Ensure Docker Desktop/daemon is running: `docker info`
- If containers fail to start, view logs: `docker compose logs -f --tail 200 tracking-service`
- If `mvnw` complains about JAVA_HOME, set `$env:JAVA_HOME` to your JDK path before running local builds.

