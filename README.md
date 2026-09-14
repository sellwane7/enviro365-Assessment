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

## Quick start
1. Start the backend first:
   ```bash
   cd backend
   mvn clean install
   mvn spring-boot:run
   ```
   Confirm it's running at `http://localhost:8080`.

2. Open `frontend/login.html` directly in a browser.

3. Log in with any seeded investor — all use password `password123` (full
   list of emails in `backend/README.md`). You'll land on the dashboard
   showing only that investor's own portfolio.

## What's included

**Backend**
- Investor login (email + BCrypt-hashed password)
- Retrieves an investor's own portfolio (details + products)
- Creates withdrawal notices with full balance calculations
- Exports CSV statements with optional status filtering
- Enforces all four required business rules

**Frontend**
- Login page (`login.html`), matching the dashboard's theme via a shared
  `styles.css`
- Portfolio dashboard, withdrawal form, withdrawal history table, and a
  CSV download button — all scoped to the logged-in investor
- Log-out button

**Advanced requirements chosen (3 of 5)**: global exception handling,
input validation, DTO layer. See `backend/README.md` for detail.

## Screenshots
Add screenshots of the running app here before submitting — e.g. the
login page, the portfolio dashboard, a successful withdrawal, and a
rejected withdrawal showing the error message.

## AI usage disclosure
AI assistance was used to help draft boilerplate (entity/controller
scaffolding, the login feature, and this documentation), and to review
the project against the assessment brief. All business logic and
validation rules were reviewed and verified manually.
