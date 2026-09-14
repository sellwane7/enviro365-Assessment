# Enviro365 Investments — Withdrawal Notice System

Junior Developer Assessment (eTalente, June 2026) — full-stack submission.

Investors log in, view their own portfolio, submit withdrawal notices
against the business rules below, review their withdrawal history, and
download a CSV statement.

## Folder structure
```
enviro365-project/
├── backend/          Spring Boot API (Java 17, Maven)
└── frontend/         Plain HTML/CSS/JS, no build step
    ├── login.html    Investor login
    ├── index.html    Portfolio dashboard, withdrawal form, history, CSV
    └── styles.css    Shared styles for both pages
```

See `backend/README.md` for backend-specific setup and API detail, and
`frontend/README.md` for frontend-specific detail (page flow, session
handling, troubleshooting).

## Quick start

1. Start the backend first:
   ```bash
   cd backend
   mvn clean install
   mvn spring-boot:run
   ```
   Confirm it's running at `http://localhost:8080`. On startup, the
   console will print a confirmation that 7 seed investors were loaded.

2. Open `frontend/login.html` directly in a browser (just double-click
   the file — no build step or local server needed).

3. Get a login (see below), then log in. You'll land on the dashboard
   showing only that investor's own portfolio.

## Getting a login — seeded investors and passwords

For security, seed passwords are **not hardcoded** anywhere in the
source code. Each investor gets a random password generated fresh
every time the app starts, and it's saved (in plain text, for local
testing only) to an H2 table called `seed_credential`.

To retrieve one:

1. With the backend running, open a browser to:
   ```
   http://localhost:8080/h2-console
   ```

2. On the login screen, set:
   - **Driver Class:** `org.h2.Driver` (default)
   - **JDBC URL:** `jdbc:h2:mem:enviro365db`
   - **User Name:** `sa`
   - **Password:** *(leave blank)*

   (Confirm these match the values in `backend/src/main/resources/application.properties`
   if you've changed anything.)

3. Click **Connect**.

4. In the SQL query box, run:
   ```sql
   SELECT * FROM SEED_CREDENTIAL;
   ```

5. Copy any row's `EMAIL` and `PLAINTEXT_PASSWORD` — that's a valid
   login for `frontend/login.html`.

**Note:** because the H2 database is in-memory, every time you restart
the backend it reseeds with brand-new random passwords. Re-run the
query above after each restart to get current credentials.

### Investor test data at a glance

| Investor | Age | Product mix | What it tests |
|---|---|---|---|
| Sellwane Mosia | 71 | Retirement Annuity, Unit Trust | Retirement withdrawal allowed |
| Matshepo Mthembu | 36 | Living Annuity, Tax-Free Savings | Retirement withdrawal rejected |
| Sipho Mokoena | 27 | Unit Trust, Living Annuity | Retirement withdrawal rejected |
| Annelize van der Merwe | 68 | Retirement Annuity, Tax-Free Savings | Retirement withdrawal allowed |
| Naledi Khumalo | 41 | Unit Trust only | No retirement product present |
| Johan Pretorius | 65 (exact) | Living Annuity, Unit Trust | Boundary case — still rejected at exactly 65 |
| Grace Adeyemi | 76 | Retirement Annuity, Tax-Free Savings | Retirement withdrawal allowed |

## Demo walkthrough (for someone reviewing this for the first time)

1. Start the backend (`mvn spring-boot:run` in `backend/`) and confirm
   the console shows the seed message.
2. Open `http://localhost:8080/h2-console`, connect using the steps
   above, and copy a login — **Sellwane Mosia's** email/password is a
   good first pick, since she's over 65 and holds a Retirement Annuity,
   so you can see a withdrawal succeed end-to-end.
3. Open `frontend/login.html` and log in with that email/password.
4. On the dashboard, review the portfolio — you should see the
   investor's products and balances only (not any other investor's).
5. Submit a withdrawal notice against one of the listed products.
   - If the product is a Retirement Annuity/Living Annuity and the
     investor is 65 or under, the request should be rejected with a
     clear error message.
   - Otherwise, it should succeed and appear in the withdrawal
     history table below.
6. Use the CSV download button to export the withdrawal history —
   optionally filter by status first.
7. Log out and try logging back in with a different seeded investor
   (e.g. **Johan Pretorius**, exactly 65) to see the rejection case.

## What's included

**Backend**
- Investor login (email + BCrypt-hashed password)
- Retrieves an investor's own portfolio (details + products)
- Creates withdrawal notices with full balance calculations
- Exports CSV statements with optional status filtering
- Enforces all four required business rules

See `backend/README.md` for full detail.

**Frontend**
- Login page (`login.html`), matching the dashboard's theme via a shared
  `styles.css`
- Portfolio dashboard, withdrawal form, withdrawal history table, and a
  CSV download button — all scoped to the logged-in investor
- Log-out button

See `frontend/README.md` for page flow, session handling, and
troubleshooting.

**Advanced requirements chosen (3 of 5)**: global exception handling,
input validation, DTO layer. See `backend/README.md` for detail.

## API overview

See `backend/README.md` for the full endpoint list and request/response
shapes. In brief:
- `POST /api/auth/login` — authenticate and receive investor session info
- `GET /api/investors/{id}/portfolio` — fetch an investor's products
- `GET /api/withdrawals/investor/{id}` — fetch withdrawal history
- `POST /api/withdrawals` — submit a new withdrawal notice

## Screenshots

Add screenshots of the running app here before submitting:
- Login page
- Portfolio dashboard (showing an investor's products)
- A successful withdrawal (e.g. Sellwane Mosia, Retirement Annuity)
- A rejected withdrawal (e.g. Johan Pretorius, exactly 65)
- The `SEED_CREDENTIAL` table in H2 console (optional, shows password
  retrieval working)

## AI usage disclosure
AI assistance was used to help draft boilerplate (entity/controller
scaffolding, the login feature, and this documentation), and to review
the project against the assessment brief. All business logic and
validation rules were reviewed and verified manually.
