# ophim-backend

Quarkus backend proxy for the OPhim API documented in `../ophim-api-documentation.md`.

## Run

```powershell
mvn quarkus:dev
```

First run will download Maven dependencies.

## Configuration

Copy `.env.example` to `.env` for local overrides. The real `.env` file is
ignored by Git.

```dotenv
QUARKUS_HTTP_PORT=8080
CORS_ORIGINS=*
OPHIM_BASE_URL=https://ophim1.com
APP_LOG_LEVEL=INFO
```

## Project structure

```text
src/main/java/com/example
├── client      # Gateway goi API OPhim ben ngoai
├── controller  # REST controller expose API cho frontend
├── dto         # Response DTO dung chung
├── exception   # Custom exception va mapper loi
└── service     # Business logic, validate input, dieu phoi gateway
```

## Endpoints

```text
GET http://localhost:8080/v1/api/home
GET http://localhost:8080/v1/api/danh-sach/{slug}
GET http://localhost:8080/v1/api/phim/{slug}
GET http://localhost:8080/v1/api/phim/{slug}/images
GET http://localhost:8080/v1/api/phim/{slug}/keywords
GET http://localhost:8080/v1/api/phim/{slug}/peoples
GET http://localhost:8080/v1/api/tim-kiem?keyword={keyword}
GET http://localhost:8080/v1/api/the-loai
GET http://localhost:8080/v1/api/the-loai/{slug}
GET http://localhost:8080/v1/api/quoc-gia
GET http://localhost:8080/v1/api/quoc-gia/{slug}
GET http://localhost:8080/v1/api/nam-phat-hanh
GET http://localhost:8080/v1/api/nam-phat-hanh/{year}
```

List endpoints support the documented query params: `page`, `limit`,
`sort_field`, `sort_type`, `category`, `country`, and `year` depending on the
route.

## Swagger

```text
Swagger UI: http://localhost:8080/swagger-ui
OpenAPI JSON/YAML: http://localhost:8080/q/openapi
```
