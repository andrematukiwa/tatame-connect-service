# Tatame Connect - Backend

Backend do MVP do Tatame Connect: cadastro de professores e academias, e busca de
professores pela academia. Java 21 + Spring Boot, Postgres, LocalStack (S3-compatible) e
RabbitMQ (preparado, sem uso ainda).

## Rodando localmente

1. Subir a infra local (Postgres + LocalStack S3 + RabbitMQ):

   ```
   docker compose -f setup/docker-compose.yml up -d
   ```

2. Rodar a aplicação com o profile `local`:

   ```
   mvn spring-boot:run -Dspring-boot.run.profiles=local
   ```

3. API disponível em `http://localhost:8081`. Swagger UI em
   `http://localhost:8081/swagger-ui.html`.

## Testes

```
mvn test
```

## Estrutura

Pacotes organizados por feature (`feature/auth`, `feature/professor`,
`feature/academia`, `feature/storage`), cada um com `controller`, `service` (+`impl`),
`repository` (+`entity`), `model/dto`, `converter` e `exception`. Mesmo padrão usado nos
demais serviços Java da empresa, adaptado para um projeto pessoal (sem Keycloak, sem AWS
de verdade — ver `S3Config`/`application-local.yaml` para como o storage aponta para o
LocalStack local).
