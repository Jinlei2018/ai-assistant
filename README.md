# Spring Boot OpenAI Bot

Sample application showing how to use Spring Boot with OpenAI's GPT-3 API.

This is a fully reactive application that uses Spring WebFlux and the OpenAI streaming API, 
that can be packaged as a GraalVM native image.

## Features

* Spring Boot 3
* Fully reactive with Spring WebFlux and Spring WebClient
* OpenAI streaming API
* Native image with GraalVM
* Deployment to Azure Container Apps

## Getting Started

### Prerequisites

- Java 17
- Access to OpenAI's GPT-3 API

### Installation

```bash
./mvnw package
```

### Quickstart

You will need to set the following environment variables to access OpenAI's API:

```bash
export APPLICATION_OPENAI_KEY=<your-openai-api-key>
export APPLICATION_OPENAI_URL=<your-openai-url>
```

## Demo

```bash
./mvnw spring-boot:run
```

## Resources

To customize the OpenAI prompt, you can check the following resource:

- [Prompt Engineering Guide](https://github.com/dair-ai/Prompt-Engineering-Guide)

## Start Ollama
### Command: ollama serve
By default, it listens on:
http://localhost:11434
You can then access the REST API or connect external applications to it.
Check if it's running
List installed models:
ollama list

### Test with curl
Run:

curl -N http://localhost:8080/api/chat ^
-H "Content-Type: application/json" ^
-d "{\"message\":\"Explain Spring Boot\"}"

### PowerShell users can also use:

Invoke-RestMethod `
  -Method Post `
-Uri http://localhost:8080/api/chat `
  -ContentType "application/json" `
-Body '{"message":"Explain Spring Boot"}'

The curl -N option disables output buffering so you can see the stream as it arrives.


##  Local PostgreSQL Setup

This project uses **PostgreSQL** running in **Docker** for local development.

### Prerequisites

* Docker Desktop
* DBeaver (Community Edition or later)

---

### Start PostgreSQL

From the project root, start the database:

```bash
docker compose up -d
```

Verify that the container is running:

```bash
docker ps
```

You should see a container similar to:

```
aiassistant-postgres
```

---

### Stop PostgreSQL

To stop the database:

```bash
docker compose down
```

> The database data is stored in a Docker volume, so it will be preserved between restarts unless you explicitly remove the volume.

---

### Connect with DBeaver

1. Open **DBeaver**.
2. Select **Database → New Database Connection**.
3. Choose **PostgreSQL**.
4. Enter the following connection settings:

| Property | Value       |
| -------- | ----------- |
| Host     | localhost   |
| Port     | 5432        |
| Database | aiassistant |
| Username | postgres    |
| Password | postgres    |

5. Click **Test Connection**.
6. If prompted, download the PostgreSQL JDBC driver.
7. Click **Finish**.

After connecting, you should see:

```
PostgreSQL
└── aiassistant
    └── Schemas
        └── public
```

At this stage, no application tables exist yet. They will be created in later commits when JPA persistence is introduced.

---

### Verify the Connection

Open an SQL Editor in DBeaver and execute:

```sql
SELECT version();
```

Then verify the active database:

```sql
SELECT current_database();
```

Expected result:

```
aiassistant
```

Your local PostgreSQL environment is now ready for development.
