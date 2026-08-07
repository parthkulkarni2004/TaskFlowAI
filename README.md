<<<<<<< HEAD
# TaskFlowAI
A collaborative Task Management System built with React, Spring Boot, and MySQL
=======
# TaskFlow — Task Management System (Frontend Only)

A complete, static frontend built with **HTML5, CSS3, and Vanilla JavaScript only** —
no React, no Bootstrap, no jQuery, no frameworks.

## How to run
Just open `login.html` (or any page) directly in a browser, or serve the folder with any
static server, e.g.:
```
python3 -m http.server 8000
```
then visit `http://localhost:8000/login.html`.

## Files
- `style.css` — every style in the app: CSS variables/tokens, layout (Flexbox + Grid),
  components (cards, badges, buttons, tables, modals, kanban, calendar, charts, forms),
  and responsive breakpoints for desktop / tablet / mobile.
- `script.js` — shared logic: sidebar toggle, dropdown menus, modal open/close,
  form validation, dark/light theme toggle (persisted in `localStorage`), dummy data,
  and per-page rendering (dashboard stats, task tables, kanban drag-and-drop,
  calendar generation, notifications, reports charts).

## Pages
| # | File | Description |
|---|------|-------------|
| 1 | `login.html` | Sign in form with validation |
| 2 | `register.html` | Account creation form with validation |
| 3 | `dashboard.html` | Sidebar + topbar shell, summary cards, charts, activity & deadlines |
| 4 | `projects.html` | Project cards grid, filters, Create Project modal |
| 5 | `project-details.html` | Single project overview, team, task list |
| 6 | `tasks.html` | Task table, search/filters, Create/Edit/Delete modals |
| 7 | `task-details.html` | Single task detail, subtasks, comments |
| 8 | `kanban.html` | 4-column kanban board with drag-and-drop |
| 9 | `calendar.html` | Monthly calendar grid with color-coded events |
| 10 | `notifications.html` | Notification list with unread states |
| 11 | `reports.html` | Stat cards, donut & bar charts, team productivity |
| 12 | `profile.html` | User info, edit profile modal, change password form |
| 13 | `settings.html` | Theme, notification, security & language settings |

## Notes
- Charts (donut, line, bar) are built with pure CSS `conic-gradient`, inline SVG, and
  flex/grid bars — no chart library — matching the "placeholder container" requirement
  while still looking polished.
- All navigation between pages is real (`<a href="...">`) static linking; all data is
  dummy/in-memory (no backend).
- Kanban drag-and-drop uses the native HTML5 Drag and Drop API.
- Dark mode is toggled from Settings and persists across pages via `localStorage`.
>>>>>>> 516a2ce (Design Frontend)
