# AutoMatch — Car Buyer Shortlist Assistant

A full-stack car research platform that helps overwhelmed buyers go from *"I don't know what to buy"* to a **confident shortlist of 3–5 cars** in under 2 minutes.

## What I Built (and Why)

**The core problem:** Too many cars, too many specs — buyers freeze.

**The solution:** A 4-step preference wizard that feeds a backend **scoring engine**, which ranks 30 real Indian-market cars and explains *why* each one fits. Buyers can then curate a shortlist (max 5) and compare side-by-side.

| Feature | Why it matters |
|---------|----------------|
| Preference wizard (budget, body, fuel, priorities, use case) | Reduces paralysis — structured questions instead of 50 filters |
| Weighted recommendation engine | Non-trivial backend computation, not just SQL filters |
| Match score + human-readable reasons | Builds confidence — "why this car?" not just "here's a list" |
| Persistent shortlist (cookie session + H2 DB) | Full-stack persistence; buyers can compare finalists |
| Side-by-side comparison table | The "confident shortlist" moment |

### Deliberately Cut

- User accounts / OAuth — cookie sessions are enough for a 2-hour MVP
- Real-time dealer inventory or pricing APIs — seeded dataset
- NLP on reviews — aggregated `userRating` + `reviewCount` instead
- Image galleries — text-first to ship fast
- Separate React build pipeline — vanilla JS served by Spring Boot for single-command setup
- Deployment config — runs locally in one command; deploy to Railway/Render in ~10 min if needed

## Tech Stack

| Layer | Choice | Why |
|-------|--------|-----|
| Backend | **Spring Boot 4.1** (Java 21) | Already scaffolded; fast REST + JPA setup |
| Database | **H2 in-memory** | Zero config, auto-seeds on startup |
| Frontend | **Vanilla HTML/CSS/JS** | No npm install, no build step, ships with the JAR |
| API | REST JSON | Clean separation; easy to swap frontend later |

## Run Locally

**Prerequisites:** Java 21+

```bash
./mvnw spring-boot:run
```

Open **http://localhost:8080** — the wizard loads immediately.

First run downloads Maven dependencies (~1 min). Subsequent runs start in ~10 seconds.

### Run Tests

```bash
./mvnw test
```

## API Endpoints

| Method | Path | Description |
|--------|------|-------------|
| `POST` | `/api/recommendations` | Submit preferences, get ranked cars |
| `GET` | `/api/shortlist` | Get current session shortlist |
| `POST` | `/api/shortlist/{carId}` | Add car to shortlist |
| `DELETE` | `/api/shortlist/{carId}` | Remove from shortlist |
| `GET` | `/api/cars` | List all cars (debug/explore) |

## Architecture

```
Browser (wizard UI)
    │
    ▼
REST Controllers
    │
    ├── RecommendationService  → weighted scoring over Car entities
    └── ShortlistService       → session-persisted shortlist (H2)
            │
            ▼
        CarRepository / ShortlistItemRepository
            │
            ▼
        H2 Database (seeded with 30 cars on startup)
```

### Scoring Logic (simplified)

1. **Hard filters:** budget range, fuel type, body type, minimum seats
2. **Weighted scores:** fuel economy, safety, performance, value-for-money, owner ratings
3. **Use-case bonus:** city → mileage, highway → power, family → seats/safety
4. **Output:** top 8 cars with match % and 2–4 plain-English reasons

## AI Tools vs. Manual Work

| Delegated to AI | Done manually |
|-----------------|---------------|
| Boilerplate (entities, repos, controllers, seed data) | Product scoping — wizard flow, what to cut |
| CSS layout and responsive design | Scoring algorithm weights and business rules |
| Frontend JS (wizard state, API calls) | Dataset curation (30 realistic cars) |
| README structure | Architecture decisions (H2 vs Postgres, no React) |

**Where AI helped most:** Speed on repetitive Spring Boot scaffolding and polished UI CSS — saved ~45 min.

**Where AI got in the way:** Initial scoring lambda had a compile error (effectively-final variable). Needed a manual fix. Over-generated abstractions were trimmed to keep the codebase small.

## Project Structure

```
src/main/java/.../
├── config/       DataLoader (seed), WebConfig
├── controller/   REST endpoints
├── dto/          Request/response records
├── model/        Car, ShortlistItem entities
├── repository/   JPA repositories
└── service/      RecommendationService, ShortlistService

src/main/resources/
├── static/       index.html, styles.css, app.js
└── application.properties
```
