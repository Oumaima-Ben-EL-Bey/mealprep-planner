# MealPrep Planner

Full-stack meal planning app: search recipes via TheMealDB, manage your own collection, plan your week and generate a shopping list that checks your pantry. Spring Boot, PostgreSQL, React with TypeScript, Docker.

## Getting started

You need: Git, Java 21, Node.js, Docker Desktop, IntelliJ.

1. **Clone the repo** (accept the GitHub invitation first)
   ```
   git clone https://github.com/Oumaima-Ben-EL-Bey/mealprep-planner.git
   cd mealprep-planner
   ```
2. **Create your `.env`**: copy `.env.example` to `.env` in the top folder. `.env` is never committed.
3. **Start the database** (Docker Desktop must be running)
   ```
   docker compose up -d db
   docker ps
   ```
   `mealprep-db` should show `(healthy)`. For development we only start the database in Docker, the backend runs in IntelliJ and the frontend with `npm run dev`.
4. **Open the project in IntelliJ**
   - Open the **`mealprep-planner`** folder (the top folder, not `backend`).
   - Right-click `backend/pom.xml` → **Add as Maven Project** and wait for the download to finish.
   - If code is red although Maven works: **File → Project Structure → Modules**. If you see modules called `main` or `test`, remove them, keep `mealprep`, then reload Maven. IntelliJ sometimes creates them by itself.
   - Start `MealprepApplication`. The console must end with `Started MealprepApplication`.
5. **Start the frontend**
   ```
   cd frontend
   npm install
   npm run dev
   ```
   Open http://localhost:5173. Check the connection: http://localhost:5173/api/test must show Spring's *Whitelabel Error Page* (a 404 from the backend means the proxy works).

### Run the whole app in Docker

**When:** only to check that everything runs together, for example before a demo, after a big merge or before handing in.
**Not for daily work:** while developing, use IntelliJ and `npm run dev` (step 4 and 5). Changes show up right away there, and you have the debugger and the logs. A Docker build takes several minutes every time.

Start all three services in containers:
```
docker compose up --build -d
```
Open http://localhost:8081.

- `--build` copies the code as it is on your disk right now into the containers (the branch you have open, including changes you have not committed).
- Without `--build`, Docker starts the containers from the last build. If you changed code since then, you see the **old** version.
- The backend container has no port of its own, so it does not clash with the backend in IntelliJ. Both use the same database, so you see the same data in both.

When you go back to developing, stop the two app containers (the database keeps running):
```
docker compose stop backend frontend
```
To stop everything: `docker compose down`. Never add `-v`, that deletes the database with all data.

## How we work with Git

### Branches

| Branch | Who writes to it | How |
|---|---|---|
| `main` | nobody directly | **protected**: only through a pull request from `develop`, with 1 approval. A direct push is rejected. |
| `develop` | everyone, through pull requests | the team's shared state. Default branch. |
| `feature/us-xx-short-name` | one person | one branch per user story or step, e.g. `feature/us-01-external-recipe-search` |

### Every time you start something

```
git checkout develop
git pull
git checkout -b feature/us-03-recipe-form
```

### While you work

- Commit small and often (rules below).
- Never commit on `main` or `develop` directly.
- Never commit `.env`.

### Before you open a pull request

Bring in what the others merged in the meantime, so conflicts show up on your machine and not on GitHub:
```
git checkout develop
git pull
git checkout feature/us-03-recipe-form
git merge develop
```
If there are conflicts: fix them, start the app, check it still works, then commit. Then:
```
git push -u origin feature/us-03-recipe-form
```

### Pull request on GitHub

1. GitHub shows **Compare & pull request**. Check: **base = `develop`**, compare = your branch.
2. Title like a commit message. Description: what it does and how you tested it.
3. One other team member reviews it: **Files changed → Review changes → Approve** (or Comment with questions).
4. After the approval: **Merge pull request**, then **Delete branch**.
5. Back on your machine:
   ```
   git checkout develop
   git pull
   git branch -d feature/us-03-recipe-form
   ```

Keep pull requests small and merge at least once or twice a day, so nobody ends up with a huge conflict on the last day.

### At the end of the week

One pull request from `develop` into `main`, approved by one team member. `main` is what we hand in and present.

## Commit messages

```
type: short description in the imperative
```

- Imperative: completes "If applied, this commit will ___". `add parser`, not `added parser`.
- Around 50 characters, lowercase after the colon, no full stop.
- An optional body after a blank line explains *why*, not *what*.

| Type | Use for | Example |
|---|---|---|
| `feat` | something a user can use | `feat: show search results as cards` |
| `fix` | wrong behaviour corrected | `fix: show message when search has no hits` |
| `refactor` | restructured, same behaviour | `refactor: move mapping into MealDbMapper` |
| `style` | formatting only | `style: format RecipeService` |
| `docs` | documentation only | `docs: describe week plan endpoints` |
| `test` | tests only | `test: cover duplicate save of a recipe` |
| `build` | dependencies, build files | `build: add react-router-dom` |
| `chore` | housekeeping | `chore: ignore IntelliJ files` |

There is no `add:` type. The type says what kind of change it is; adding a file can be `feat`, `docs` or `chore`.
