# spring-boot-eks

[![CI/CD](https://github.com/hendisantika/spring-boot-eks/actions/workflows/docker-build-push.yml/badge.svg)](https://github.com/hendisantika/spring-boot-eks/actions/workflows/docker-build-push.yml)

A Spring Boot REST API, secured with JWT, that is built and tested in GitHub Actions, packaged as a Docker image,
pushed to Docker Hub and deployed to Amazon EKS.

## Tech stack

| Component       | Version                                  |
|-----------------|------------------------------------------|
| Java (JDK)      | **25** (Eclipse Temurin)                 |
| Spring Boot     | 4.0.5                                    |
| Build tool      | Maven (wrapper included: `./mvnw`)       |
| Security        | Spring Security + JWT (jjwt 0.13.0)      |
| Persistence     | Spring Data JPA, PostgreSQL 42.7.10 / H2 |
| Container image | `eclipse-temurin:25-jdk-alpine`          |
| Orchestration   | Kubernetes on Amazon EKS                 |

## Prerequisites

- JDK 25
- Docker (optional, for building the image)
- `kubectl` and the AWS CLI (optional, for deploying to EKS)

## Running locally

With no database settings, the app falls back to an in-memory H2 database:

```bash
./mvnw spring-boot:run
```

To use PostgreSQL, set these environment variables:

| Variable      | Default                                | Description                    |
|---------------|----------------------------------------|--------------------------------|
| `DB_URL`      | `jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1` | JDBC URL                       |
| `DB_USERNAME` | `yu71`                                 | Database user                  |
| `DB_PASSWORD` | `53cret`                               | Database password              |
| `DB_DRIVER`   | `org.h2.Driver`                        | e.g. `org.postgresql.Driver`   |
| `DB_DDL_AUTO` | `create-drop`                          | Hibernate `ddl-auto` mode      |
| `DB_SHOW_SQL` | `true`                                 | Log SQL statements             |

The app listens on port `8080`.

## Build & test

```bash
./mvnw clean verify
```

## Endpoints

All endpoints except `/auth/*` require a `Authorization: Bearer <token>` header.

| Method | Path     | Description                                      |
|--------|----------|--------------------------------------------------|
| GET    | `/`      | Root greeting                                    |
| GET    | `/hello` | Hello endpoint                                   |
| POST   | `/add`   | Add an entry, body: `{"name": "...", "value": "..."}` |
| GET    | `/all`   | List all entries                                 |

## Docker

```bash
docker build -t spring-boot-eks .
docker run -p 8080:8080 spring-boot-eks
```

The multi-stage `Dockerfile` builds with `maven:3.9.11-eclipse-temurin-25` and runs on `eclipse-temurin:25-jdk-alpine`.

## CI/CD pipeline

`.github/workflows/docker-build-push.yml` runs on pushes to `main`, `develop`, `qa` and `prod`:

1. **build-test** – set up Temurin JDK 25, run `mvn -B clean verify`, then rename the JAR to `<env>_<short-sha>.jar`.
2. **docker-build-push** – build the image and push it to the branch's Docker Hub repo
   (`deploy-repo-main`, `deploy-repo-dev`, `deploy-repo-qa`, `deploy-repo-prod`).
3. **deploy-main-to-eks** (`main` only) – update kubeconfig for the `demo-cluster` EKS cluster (`ap-southeast-1`),
   create or update the `demo-db-secret` secret, apply `k8s/` manifests and wait for the rollout.

### Required repository secrets

| Secret                                         | Used by                 |
|------------------------------------------------|-------------------------|
| `DOCKERHUB_USERNAME`, `DOCKERHUB_TOKEN`        | Docker Hub login & push |
| `AWS_ACCESS_KEY_ID`, `AWS_SECRET_ACCESS_KEY`   | EKS access              |
| `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`         | Kubernetes DB secret    |

## Kubernetes manifests

- `k8s/namespace.yaml` – `demo-main` namespace
- `k8s/deployment.yaml` – `demo-app` deployment (reads DB credentials from `demo-db-secret`)
- `k8s/service.yaml` – `demo-app-service` LoadBalancer
