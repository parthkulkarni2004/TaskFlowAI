# TaskFlowAI — Backend

Java 21 + Spring Boot 3.3 + MySQL REST API built to match the existing
`frontend/` (vanilla HTML/CSS/JS) pages exactly — same fields, same
statuses, same dummy data as `frontend/js/script.js`.

## Stack
- Java 21, Spring Boot 3.3
- Spring Web, Spring Data JPA, Spring Security (JWT, stateless)
- MySQL 8
- Lombok
- jjwt 0.12.x for JWT

## 1. Setup

Create the database (or let Hibernate auto-create it — `createDatabaseIfNotExist=true`
is already set):

```sql
CREATE DATABASE IF NOT EXISTS taskflowai_db;
```

Edit `src/main/resources/application.properties` if your MySQL username/password
differ from `root` / `root`.

## 2. Run

```bash
cd backend
mvn spring-boot:run
```

API starts on **http://localhost:8080**.

On first run, `DataSeeder` inserts the same sample data used in `script.js`
(6 projects, 10 tasks, 5 users, notifications, calendar events, activity log).

**Seeded login:**
```
email:    john.doe@example.com
password: password123
```

## 3. Serve the frontend against it

```bash
cd frontend
python3 -m http.server 5501
```

Visit `http://localhost:5501/html/login.html`. CORS is already open for
`localhost:5501` / `5500` / `3000` in `application.properties`
(`app.cors.allowed-origins`) — the frontend JS currently uses hardcoded
dummy arrays, so wiring `fetch()` calls to these endpoints is the next step
if you want it fully live instead of static dummy data.

## 4. API Reference

All endpoints are under `/api`. Every route except `/api/auth/**` and
`/api/health` requires header `Authorization: Bearer <token>` (returned by
login/register).

### Auth
| Method | Endpoint | Body |
|---|---|---|
| POST | `/api/auth/register` | `{ fullname, email, password, role }` |
| POST | `/api/auth/login` | `{ email, password }` |

### Users
| Method | Endpoint | Notes |
|---|---|---|
| GET | `/api/users/me` | current profile |
| PUT | `/api/users/me` | `{ fullname, email, phone, location, bio }` |
| PUT | `/api/users/me/password` | `{ current, newPassword }` |
| GET | `/api/users` | list (for assignee dropdowns) |

### Projects
| Method | Endpoint |
|---|---|
| GET | `/api/projects` |
| GET | `/api/projects/{id}` |
| POST | `/api/projects` — `{ name, desc, due, status, progress, teamUserIds[] }` |
| PUT | `/api/projects/{id}` |
| DELETE | `/api/projects/{id}` |
| POST | `/api/projects/{id}/members/{userId}` |

### Tasks
| Method | Endpoint |
|---|---|
| GET | `/api/tasks?projectId=&status=&priority=&assigneeId=&search=` |
| GET | `/api/tasks/kanban` — grouped by status (`To Do` / `In Progress` / `Review` / `Completed`) |
| GET | `/api/tasks/{id}` |
| POST | `/api/tasks` — `{ name, desc, projectId, assigneeId, due, status, priority }` |
| PUT | `/api/tasks/{id}` |
| PATCH | `/api/tasks/{id}/status` — `{ status }` (kanban drag-drop) |
| DELETE | `/api/tasks/{id}` |
| GET/POST | `/api/tasks/{id}/subtasks` |
| PATCH | `/api/subtasks/{id}/toggle` |
| DELETE | `/api/subtasks/{id}` |
| GET/POST | `/api/tasks/{id}/comments` |

### Notifications
| Method | Endpoint |
|---|---|
| GET | `/api/notifications` |
| GET | `/api/notifications/unread-count` |
| PUT | `/api/notifications/{id}/read` |
| PUT | `/api/notifications/read-all` |

### Calendar
| Method | Endpoint |
|---|---|
| GET | `/api/calendar/events?year=&month=` |
| POST | `/api/calendar/events` — `{ label, date, color, projectId }` |
| DELETE | `/api/calendar/events/{id}` |

### Dashboard & Reports
| Method | Endpoint |
|---|---|
| GET | `/api/dashboard/summary` — stat cards, activity feed, deadlines, top projects, weekly chart |
| GET | `/api/reports` — totals, team productivity, tasks by status/priority |

## Notes
- Task `status` values: `To Do`, `In Progress`, `Review`, `Completed`
  (stored as `TODO/IN_PROGRESS/REVIEW/COMPLETED` internally, converted at the DTO layer).
- Task `priority`: `Low`, `Medium`, `High`.
- Project `status`: `Active`, `On Hold`, `Completed`.
- Assigning a task or completing it auto-creates a `Notification` for the
  assignee (matches the notifications page's dummy data pattern).
- `mvn compile` could not be run in the sandbox that generated this code
  (no network access to Maven Central) — run `mvn clean install` locally
  first; if anything doesn't compile, tell me the error and I'll fix it
  immediately.
