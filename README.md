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

Start Ollama
ollama serve
By default, it listens on:
http://localhost:11434
You can then access the REST API or connect external applications to it.
Check if it's running
List installed models:
ollama list

Test with curl
Run:

curl -N http://localhost:8080/api/chat ^
-H "Content-Type: application/json" ^
-d "{\"message\":\"Explain Spring Boot\"}"

PowerShell users can also use:

Invoke-RestMethod `
  -Method Post `
-Uri http://localhost:8080/api/chat `
  -ContentType "application/json" `
-Body '{"message":"Explain Spring Boot"}'

The curl -N option disables output buffering so you can see the stream as it arrives.