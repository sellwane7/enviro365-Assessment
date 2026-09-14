# Enviro365 Investments — Frontend

Junior Developer Assessment — plain HTML/CSS/JS frontend (no build step,
no framework, no npm install required).

## Files

| File | Purpose |
|---|---|
| `login.html` | Investor login screen |
| `index.html` | Portfolio dashboard, withdrawal form, withdrawal history table, CSV download |
| `styles.css` | Shared styling for both pages |

## How to run

No build step — just open the file directly in a browser:

1. Make sure the backend is running first (`cd ../backend && mvn spring-boot:run`),
   listening at `http://localhost:8080`.
2. Double-click `login.html` to open it in your default browser.

That's it. There's no local server, no `npm start`, nothing to install —
the browser loads the file straight from disk (`file://`) and talks to
the backend over `fetch()`.

## Getting a login

Passwords are never hardcoded in this project. See the root `README.md`
or `backend/README.md` for the step-by-step on retrieving a valid
email/password pair from the H2 `seed_credential` table.

## How it talks to the backend

Both `login.html` and `index.html` define:
```js
const API_BASE = "http://localhost:8080/api";
```
at the top of their `<script>` block. If you change the backend's port
in `application.properties`, update this constant in both files to match.

The backend allows cross-origin requests from any origin
(`@CrossOrigin(origins = "*")` on every controller), so opening these
files as `file://` pages works without any CORS configuration on your
end.

## Page flow

1. **`login.html`** — investor enters email + password, calls
   `POST /api/auth/login`. On success, the investor's id/name/email/age
   are stored in `sessionStorage` (no server-side session token), then
   the page redirects to `index.html`.
2. **`index.html`** — reads the logged-in investor from `sessionStorage`,
   then:
   - Loads their portfolio via `GET /api/investors/{id}/portfolio`
   - Loads their withdrawal history via `GET /api/withdrawals/investor/{id}`
   - Lets them submit a new withdrawal via `POST /api/withdrawals`
   - Lets them export history as CSV via
     `GET /api/withdrawals/investor/{id}/export`
   - Includes a **Log out** button, which clears `sessionStorage` and
     returns to `login.html`

Because everything is scoped to the investor id stored in
`sessionStorage`, each browser tab only ever sees that one investor's
data — never another investor's portfolio.

## Notes on session handling

`sessionStorage` (not `localStorage`) is used deliberately: it's cleared
automatically when the browser tab is closed, so a logged-in session
doesn't silently persist across unrelated browser sessions. Logging in
again in a new tab requires a fresh login.

## Troubleshooting

- **"Couldn't reach the backend" error on login or portfolio load** —
  confirm the backend is actually running and listening on port 8080
  (check the terminal where you ran `mvn spring-boot:run`).
- **Login succeeds but portfolio doesn't load** — open the browser
  console (F12 → Console) and check for errors; this usually points to
  a mismatched investor id or an expired `sessionStorage` entry from an
  earlier session.
- **Blank page or styling looks broken** — make sure `styles.css` is in
  the same folder as `login.html`/`index.html`; don't move the HTML
  files without moving the stylesheet too.

## AI usage disclosure
Parts of this frontend (page scaffolding and this README) were drafted
with AI assistance and then reviewed. All page logic and API integration
were verified manually against the running backend.
