## Purpose
Provide repository-specific guidance for AI coding agents working on this Create React App-based quiz project.

## Big picture
- Frontend: a single-page React app bootstrapped with Create React App (`react-scripts`).
- Routing: client-side routing via `react-router-dom` v6. Main router is `src/Components/Router/AppRouter.jsx`.
- UI: Material UI (`@mui/material`, `@mui/icons-material`) is used for components and layout.
- Backend integration: the `Auth` component calls a backend at `http://localhost:8080` (e.g. `/signup`). The frontend currently expects a simple JSON API.

## How to run (developer workflows)
- Start dev server: `npm start` (runs `react-scripts start`, opens at `http://localhost:3000`).
- Run tests/watch: `npm test`.
- Build for production: `npm run build`.

## Project layout & key files
- `src/App.js` — top-level app; wraps `AppRouter` in `BrowserRouter` and passes `data` props used by `Auth`.
- `src/Components/Router/AppRouter.jsx` — defines routes. Add new pages by creating a component under `src/Components` and adding a `<Route path=... element={<YourComp/>}/>`.
- `src/Components/Authentication/Auth.js` — signup/login form and the only network call discovered (`fetch` to `http://localhost:8080/signup`).
- `src/Components/CreateQuiz.js` — UI to choose manual vs AI generation. Uses `ManualCreation` for manual flow.
- `src/Components/ManualCreation.js` — collects question objects in local state and renders them in a table. Important implementation details are present in this file.

## Conventions & patterns (discoverable)
- Components: function components with default exports. Place component files under `src/Components/` and related CSS alongside (e.g., `Auth.css`).
- Routing: uses `Routes`/`Route` from `react-router-dom@6`; routes use the `element` prop (not `component`).
- State & props: local component state via `useState`; small prop objects are passed from parent to child (see `App` -> `AppRouter` -> `Auth`).
- UI library: prefer Material UI primitives (e.g., `Button`, `TextField`, `Table`) for new UI elements.
- Networking: the `Auth` component uses `fetch` directly and expects a JSON response with a boolean `status` and `message` string.

## Small but important code patterns to follow
- When mapping lists to JSX (for example the `questions.map(...)` in `ManualCreation.js`), ensure each iterated element has a stable `key` prop (the current code omits it).
- `Auth.js` prevents form submission using `handleSubmit(e) { e.preventDefault(); }` but also triggers network calls from `validateSignUp`. Preserve that separation when refactoring.
- When adding routes, import components using the existing folder structure. Example: `import NewPage from '../NewPage'` and then add to `AppRouter.jsx`.

## Integration points & external dependencies
- Backend: `http://localhost:8080` is used by `Auth`. Expect CORS and JSON-based responses. No other external services discovered.
- Libraries: MUI (`@mui/material`), Emotion (`@emotion/*`), React Router, testing libraries from Create React App.

## Examples (concrete snippets)
- Add a route (edit `src/Components/Router/AppRouter.jsx`):
  - `import MyPage from '../MyPage'`
  - `<Route path='/my-page' element={<MyPage/>} />`
- Persist questions (where ManualCreation collects them): create a save handler that POSTs `questions` array to backend (use `fetch('http://localhost:8080/quizzes', { method:'POST', headers: {'Content-Type':'application/json'}, body: JSON.stringify({questions}) })`).

## Known gotchas / quick checks
- ManualCreation: table rows currently lack a `key` attribute and inputs are partially uncontrolled (component relies on merging into `currentQuestion`); verify controlled inputs when adding validation.
- Auth expects the backend response to include `status` and `message` — validate and handle network errors and non-JSON responses.
- Imports: some components live under `src/Components` root while others are nested (e.g., `Authentication`, `Home`); double-check relative import paths when moving files.

## What a helpful PR should include
- Short description of the change and affected files.
- When adding routes or network behavior, include: updated `AppRouter.jsx`, the new component, and a note about required backend endpoints (URL and request shape).
- If UI changes, prefer reusing MUI components and include screenshots or short GIFs for visual diffs.

## When in doubt
- Open these files first: `src/App.js`, `src/Components/Router/AppRouter.jsx`, `src/Components/CreateQuiz.js`, `src/Components/ManualCreation.js`, `src/Components/Authentication/Auth.js`.
- Run `npm start` to reproduce and iterate quickly; check the browser console for lint/runtime warnings.

---
If you'd like, I can (a) add a short checklist for PR reviewers, or (b) open a PR that fixes the missing `key` in `ManualCreation.js`. Which do you prefer?
