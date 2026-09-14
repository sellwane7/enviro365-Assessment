# Enviro365 Investments — Withdrawal Notice Backend

Junior Developer Assessment — Spring Boot backend.

## Tech stack
- Java 17, Spring Boot 3.3.4
- Spring Web, Spring Data JPA, Spring Validation
- `spring-security-crypto` (BCrypt password hashing for investor login)
- H2 in-memory database

## Package
`com.enviro.assessment.junior.sellwane`

## How to run (IntelliJ)
1. Open the `backend` folder in IntelliJ (`File > Open`).
2. Let Maven download dependencies.
3. Open `EnviroAssessmentApplication.java` and click the green ▶ Run button.
4. The API starts at `http://localhost:8080`.

## How to run (command line)
```bash
cd backend
mvn clean install
mvn spring-boot:run
```
Use `mvn clean ...` rather than running an old `target/*.jar`, so you're
always running the latest build.

## Sample data & demo logins
On every startup, `DataLoader` seeds 7 investors, all with the password
`password123` (stored as a BCrypt hash):

| Email | Age | Products |
|---|---|---|
| thabo.nkosi@example.com | 71 | Retirement Annuity (R500,000), Unit Trust (R120,000) |
| lerato.dlamini@example.com | 36 | Living Annuity (R300,000), Tax-Free Savings (R36,000) |
| sipho.mokoena@example.com | 27 | Unit Trust (R85,000), Living Annuity (R150,000) |
| annelize.vandermerwe@example.com | 68 | Retirement Annuity (R720,000), Tax-Free Savings (R40,000) |
| naledi.khumalo@example.com | 41 | Unit Trust (R60,000) |
| johan.pretorius@example.com | 65 | Living Annuity (R410,000), Unit Trust (R95,000) |
| grace.adeyemi@example.com | 76 | Retirement Annuity (R610,000), Tax-Free Savings (R30,000) |

Johan is exactly 65, which is a useful edge case: the rule is "older than
65", so his retirement withdrawal is still rejected.

Browse `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:enviro365db`,
user `sa`, no password) to inspect the database directly.

## API endpoints

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/auth/login` | Log in. Body: `{"email":"...","password":"..."}`. Returns investor id/name/email/age, or 401. |
| GET | `/api/investors/{id}/portfolio` | Investor details + product list |
| POST | `/api/withdrawals` | Create a withdrawal notice. Body: `{"productId":1,"amount":5000.00}` |
| GET | `/api/withdrawals/investor/{investorId}` | Full withdrawal history |
| GET | `/api/withdrawals/investor/{investorId}/export?status=APPROVED` | CSV download (status filter optional) |

## Business rules enforced (WithdrawalService)
1. Retirement withdrawals (Retirement Annuity / Living Annuity) only allowed if investor age > 65
2. Withdrawal amount must not exceed the product balance
3. Withdrawal amount must not exceed 90% of the product balance

## Login
Investors log in with email + password (`POST /api/auth/login`). Passwords
are hashed with BCrypt before being stored, and never returned in any API
response. There is no session token — a successful login tells the
frontend which investor it is, and the frontend remembers that for the
current browser tab (`sessionStorage`).

## Advanced requirements implemented (3 of 5 required — chose 3)
- [x] **Global exception handling** — `GlobalExceptionHandler` catches every
      exception thrown anywhere in the app and returns one consistent JSON
      error shape (`ApiError`), instead of raw stack traces.
- [x] **Input validation** — `WithdrawalRequest`/`LoginRequest` use
      `@NotNull`/`@NotBlank`/`@DecimalMin`/`@Email` so bad requests are
      rejected automatically before any business logic runs.
- [x] **DTO layer** — `WithdrawalRequest`, `LoginRequest`, and
      `LoginResponse` are dedicated classes for what the API sends and
      receives, kept separate from the JPA entities. `LoginResponse` in
      particular exists so a login response can never include the
      investor's password hash, even by accident.

## AI usage disclosure
Parts of this project's boilerplate (entity/controller scaffolding, the
login feature, and this README) were drafted with AI assistance and then
reviewed. All business logic and validation rules were verified manually
against the assessment requirements.
