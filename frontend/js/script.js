/* =========================================================
   TaskFlow — Shared JavaScript (Vanilla JS, no dependencies)
   ========================================================= */

/* ---------------- 1. Dummy Data ---------------- */
const AVATARS = [
  "https://i.pravatar.cc/64?img=12",
  "https://i.pravatar.cc/64?img=32",
  "https://i.pravatar.cc/64?img=47",
  "https://i.pravatar.cc/64?img=5",
  "https://i.pravatar.cc/64?img=25",
];

const PROJECTS = [
  { id: 1, name: "TaskFlow Website", desc: "Official website redesign and development", progress: 75, due: "Jun 15, 2024", status: "Active", team: [0,1,2] },
  { id: 2, name: "Mobile App", desc: "Cross-platform mobile application", progress: 40, due: "Jul 30, 2024", status: "Active", team: [1,2,3] },
  { id: 3, name: "Marketing Campaign", desc: "Q2 marketing and branding campaign", progress: 60, due: "May 30, 2024", status: "Active", team: [0,3] },
  { id: 4, name: "Design System", desc: "Create reusable design components", progress: 80, due: "Jun 10, 2024", status: "Active", team: [2,4] },
  { id: 5, name: "Admin Dashboard", desc: "Internal admin dashboard", progress: 60, due: "Jun 25, 2024", status: "On Hold", team: [0,1] },
  { id: 6, name: "SEO Optimization", desc: "Improve website SEO and performance", progress: 30, due: "Jul 5, 2024", status: "Active", team: [3,4] },
];

const TASKS = [
  { id: 1, name: "Design Login Page", project: "TaskFlow Website", assignee: "Jane Smith", avatar: 0, due: "May 26, 2024", status: "In Progress", priority: "High" },
  { id: 2, name: "Setup Database", project: "Mobile App", assignee: "Mike Johnson", avatar: 1, due: "May 30, 2024", status: "To Do", priority: "Medium" },
  { id: 3, name: "Create Dashboard", project: "Admin Dashboard", assignee: "Emily Davis", avatar: 2, due: "May 25, 2024", status: "Review", priority: "High" },
  { id: 4, name: "Write Documentation", project: "TaskFlow Website", assignee: "Chris Brown", avatar: 3, due: "Jun 1, 2024", status: "To Do", priority: "Low" },
  { id: 5, name: "Fix UI Bugs", project: "Mobile App", assignee: "Jane Smith", avatar: 0, due: "May 22, 2024", status: "Completed", priority: "High" },
  { id: 6, name: "Build API Endpoints", project: "Mobile App", assignee: "Mike Johnson", avatar: 1, due: "May 28, 2024", status: "In Progress", priority: "High" },
  { id: 7, name: "Design Wireframes", project: "TaskFlow Website", assignee: "Emily Davis", avatar: 2, due: "May 27, 2024", status: "To Do", priority: "Low" },
  { id: 8, name: "User Authentication", project: "Mobile App", assignee: "Chris Brown", avatar: 3, due: "Jun 2, 2024", status: "To Do", priority: "High" },
  { id: 9, name: "UI/UX Review", project: "TaskFlow Website", assignee: "Jane Smith", avatar: 0, due: "May 29, 2024", status: "Review", priority: "Medium" },
  { id: 10, name: "Setup CI/CD", project: "Admin Dashboard", assignee: "Mike Johnson", avatar: 1, due: "Jun 3, 2024", status: "Review", priority: "Low" },
];

const ACTIVITY = [
  { icon: "🔄", title: "Design system updated", time: "2 mins ago" },
  { icon: "✅", title: 'Task "Login Page" completed', time: "15 mins ago" },
  { icon: "➕", title: "New task created: Dashboard UI", time: "1 hour ago" },
  { icon: "📁", title: 'Project "TaskFlow" updated', time: "2 hours ago" },
];

const DEADLINES = [
  { title: "Project Proposal", due: "Due in 1 day", priority: "High" },
  { title: "Design System", due: "Due in 3 days", priority: "Medium" },
  { title: "User Research", due: "Due in 5 days", priority: "Low" },
];

const NOTIFICATIONS = [
  { icon: "📝", text: 'Task "Design Login Page" has been assigned to you.', time: "2 min ago", unread: true },
  { icon: "⏰", text: 'Task "Setup Database" is due tomorrow.', time: "1 hour ago", unread: true },
  { icon: "💬", text: "Mike Johnson mentioned you in a comment.", time: "3 hours ago", unread: true },
  { icon: "📁", text: 'Project "Mobile App" has been updated.', time: "3 hours ago", unread: false },
  { icon: "✅", text: 'Task "Fix UI Bugs" has been completed.', time: "5 hours ago", unread: false },
  { icon: "💬", text: "Emily Davis commented on your task.", time: "1 day ago", unread: false },
];

const CAL_EVENTS = {
  3: [{ label: "Design Review", color: "blue" }],
  7: [{ label: "Project Deadline", color: "red" }],
  12: [{ label: "Team Meeting", color: "purple" }],
  14: [{ label: "Team Meeting", color: "purple" }],
  23: [{ label: "Release v1.2", color: "green" }],
};

/* ---------------- 2. Helpers ---------------- */
const $ = (sel, ctx = document) => ctx.querySelector(sel);
const $$ = (sel, ctx = document) => Array.from(ctx.querySelectorAll(sel));

function priorityBadge(p) {
  return `<span class="badge badge-${p.toLowerCase()}"><span class="badge-dot" style="background:currentColor"></span>${p}</span>`;
}
function statusBadge(s) {
  const map = { "To Do": "todo", "In Progress": "progress", "Review": "review", "Completed": "completed" };
  const cls = map[s] || "todo";
  return `<span class="badge badge-${cls}">${s}</span>`;
}
function avatarGroup(indices, extra = 0) {
  let html = '<div class="avatar-group">';
  indices.forEach(i => html += `<img src="${AVATARS[i % AVATARS.length]}" alt="member">`);
  if (extra > 0) html += `<div class="avatar-more">+${extra}</div>`;
  html += "</div>";
  return html;
}
function progressColor(p) {
  if (p >= 70) return "green";
  if (p >= 40) return "";
  return "orange";
}
function showToast(message, type = "success") {
  let stack = $(".toast-stack");
  if (!stack) {
    stack = document.createElement("div");
    stack.className = "toast-stack";
    document.body.appendChild(stack);
  }
  const toast = document.createElement("div");
  toast.className = `toast ${type}`;
  toast.textContent = message;
  stack.appendChild(toast);
  setTimeout(() => toast.remove(), 3200);
}

/* ---------------- 3. Sidebar / Topbar interactivity ---------------- */
function initShell() {
  const shell = $(".app-shell");
  if (!shell) return;

  const toggleBtn = $(".sidebar-toggle");
  const overlay = $(".sidebar-overlay");

  toggleBtn && toggleBtn.addEventListener("click", () => {
    if (window.innerWidth <= 860) {
      shell.classList.toggle("sidebar-open");
      overlay && overlay.classList.toggle("open", shell.classList.contains("sidebar-open"));
    } else {
      shell.classList.toggle("sidebar-collapsed");
    }
  });

  overlay && overlay.addEventListener("click", () => {
    shell.classList.remove("sidebar-open");
    overlay.classList.remove("open");
  });

  // Dropdowns (user menu, notification bell)
  $$("[data-dropdown-trigger]").forEach(trigger => {
    const menu = document.getElementById(trigger.dataset.dropdownTrigger);
    trigger.addEventListener("click", e => {
      e.stopPropagation();
      $$(".dropdown-menu.open").forEach(m => { if (m !== menu) m.classList.remove("open"); });
      menu && menu.classList.toggle("open");
    });
  });
  document.addEventListener("click", () => $$(".dropdown-menu.open").forEach(m => m.classList.remove("open")));

  // Highlight active nav link based on current file name
  const current = location.pathname.split("/").pop() || "dashboard.html";
  $$(".sidebar-nav a").forEach(a => {
    if (a.getAttribute("href") === current) a.classList.add("active");
  });

  // Logout links
  $$("[data-logout]").forEach(el => el.addEventListener("click", e => {
    e.preventDefault();
    showToast("Logged out successfully");
    setTimeout(() => location.href = "login.html", 600);
  }));
}

/* ---------------- 4. Theme toggle ---------------- */
function initTheme() {
  const saved = localStorage.getItem("taskflow-theme") || "light";
  document.documentElement.setAttribute("data-theme", saved);
  $$("[data-theme-toggle]").forEach(t => {
    if (t.type === "checkbox") t.checked = saved === "dark";
    t.addEventListener("change", () => {
      const isDark = t.type === "checkbox" ? t.checked : document.documentElement.getAttribute("data-theme") !== "dark";
      const theme = isDark ? "dark" : "light";
      document.documentElement.setAttribute("data-theme", theme);
      localStorage.setItem("taskflow-theme", theme);
      $$("[data-theme-toggle]").forEach(o => { if (o.type === "checkbox") o.checked = isDark; });
    });
    if (t.tagName === "BUTTON") {
      t.addEventListener("click", () => {
        const theme = document.documentElement.getAttribute("data-theme") === "dark" ? "light" : "dark";
        document.documentElement.setAttribute("data-theme", theme);
        localStorage.setItem("taskflow-theme", theme);
      });
    }
  });
}

/* ---------------- 5. Modal handling ---------------- */
function initModals() {
  $$("[data-modal-open]").forEach(btn => {
    btn.addEventListener("click", () => {
      const modal = document.getElementById(btn.dataset.modalOpen);
      modal && modal.classList.add("open");
    });
  });
  $$("[data-modal-close]").forEach(btn => {
    btn.addEventListener("click", () => btn.closest(".modal-overlay").classList.remove("open"));
  });
  $$(".modal-overlay").forEach(overlay => {
    overlay.addEventListener("click", e => { if (e.target === overlay) overlay.classList.remove("open"); });
  });
  document.addEventListener("keydown", e => {
    if (e.key === "Escape") $$(".modal-overlay.open").forEach(m => m.classList.remove("open"));
  });
}

/* ---------------- 6. Form validation ---------------- */
function validateForm(form) {
  let valid = true;
  $$("input[required], select[required], textarea[required]", form).forEach(input => {
    const group = input.closest(".form-group") || input.parentElement;
    let fieldValid = input.value.trim() !== "";
    if (input.type === "email" && fieldValid) {
      fieldValid = /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(input.value);
    }
    if (input.type === "password" && input.dataset.minlength && fieldValid) {
      fieldValid = input.value.length >= Number(input.dataset.minlength);
    }
    if (input.dataset.match && fieldValid) {
      const other = form.querySelector(`[name="${input.dataset.match}"]`);
      fieldValid = other && other.value === input.value;
    }
    group && group.classList.toggle("error", !fieldValid);
    if (!fieldValid) valid = false;
  });
  return valid;
}

function initForms() {
  $$("form[data-validate]").forEach(form => {
    form.addEventListener("submit", e => {
      e.preventDefault();
      if (validateForm(form)) {
        const action = form.dataset.validate;
        if (action === "login") { showToast("Login successful! Redirecting…"); setTimeout(() => location.href = "dashboard.html", 700); }
        else if (action === "register") { showToast("Account created! Redirecting to login…"); setTimeout(() => location.href = "login.html", 700); }
        else { showToast("Saved successfully"); const overlay = form.closest(".modal-overlay"); if (overlay) overlay.classList.remove("open"); }
      } else {
        showToast("Please fix the highlighted fields", "error");
      }
    });
  });

  // Password visibility toggles
  $$(".toggle-visibility").forEach(btn => {
    btn.addEventListener("click", () => {
      const input = btn.previousElementSibling;
      input.type = input.type === "password" ? "text" : "password";
      btn.textContent = input.type === "password" ? "👁" : "🙈";
    });
  });
}

/* ---------------- 7. Page renderers ---------------- */
function renderDashboard() {
  const activityEl = $("#activityList");
  if (activityEl) {
    activityEl.innerHTML = ACTIVITY.map(a => `
      <div class="list-row">
        <div class="list-ic">${a.icon}</div>
        <div><div class="list-title">${a.title}</div><div class="list-sub">${a.time}</div></div>
      </div>`).join("");
  }
  const deadlineEl = $("#deadlineList");
  if (deadlineEl) {
    deadlineEl.innerHTML = DEADLINES.map(d => `
      <div class="list-row">
        <div class="list-ic">📅</div>
        <div style="flex:1"><div class="list-title">${d.title}</div><div class="list-sub">${d.due}</div></div>
        ${priorityBadge(d.priority)}
      </div>`).join("");
  }
  const topProjectsEl = $("#topProjectsList");
  if (topProjectsEl) {
    topProjectsEl.innerHTML = PROJECTS.slice(0, 3).map(p => `
      <div class="list-row">
        <div style="flex:1">
          <div class="list-title mb-2">${p.name}</div>
          <div class="progress-track"><div class="progress-fill" style="width:${p.progress}%"></div></div>
        </div>
        <span class="text-sm text-muted" style="font-weight:700">${p.progress}%</span>
      </div>`).join("");
  }
  drawLineChart();
}

function drawLineChart() {
  const svg = $("#tasksLineChart");
  if (!svg) return;
  const data = [40, 55, 45, 70, 60, 80, 65];
  const days = ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"];
  const w = 320, h = 150, pad = 10;
  const max = 100;
  const pts = data.map((v, i) => {
    const x = pad + (i * (w - pad * 2)) / (data.length - 1);
    const y = h - pad - (v / max) * (h - pad * 2);
    return `${x},${y}`;
  }).join(" ");
  svg.setAttribute("viewBox", `0 0 ${w} ${h + 20}`);
  svg.innerHTML = `
    <polyline points="${pts}" fill="none" stroke="var(--color-success)" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"/>
    ${data.map((v, i) => {
      const x = pad + (i * (w - pad * 2)) / (data.length - 1);
      const y = h - pad - (v / max) * (h - pad * 2);
      return `<circle cx="${x}" cy="${y}" r="3" fill="var(--color-success)"/>`;
    }).join("")}
    ${days.map((d, i) => {
      const x = pad + (i * (w - pad * 2)) / (data.length - 1);
      return `<text x="${x}" y="${h + 14}" font-size="9" fill="var(--color-text-muted)" text-anchor="middle">${d}</text>`;
    }).join("")}
  `;
}

function renderProjects() {
  const grid = $("#projectsGrid");
  if (!grid) return;
  grid.innerHTML = PROJECTS.map(p => `
    <div class="card project-card">
      <div class="pc-top">
        <div><h4>${p.name}</h4><p>${p.desc}</p></div>
        <button class="btn-ghost btn-icon">⋮</button>
      </div>
      ${avatarGroup(p.team, 0)}
      <div class="due-tag ${p.status === 'On Hold' ? 'overdue' : ''}">📅 Due: ${p.due}</div>
      <div class="pc-progress-row">
        <div class="progress-track"><div class="progress-fill ${progressColor(p.progress)}" style="width:${p.progress}%"></div></div>
        <span>${p.progress}%</span>
      </div>
    </div>`).join("");
}

function renderTaskTable(targetId, tasks) {
  const tbody = $(targetId);
  if (!tbody) return;
  tbody.innerHTML = tasks.map(t => `
    <tr>
      <td><div class="task-name-cell"><strong>${t.name}</strong></div></td>
      <td>${t.project}</td>
      <td class="flex gap-2" style="align-items:center"><img class="avatar avatar-sm" src="${AVATARS[t.avatar]}" alt="${t.assignee}">${t.assignee}</td>
      <td>${t.due}</td>
      <td>${statusBadge(t.status)}</td>
      <td>${priorityBadge(t.priority)}</td>
      <td class="row-actions">
        <a href="task-details.html" class="btn btn-ghost btn-sm">View</a>
        <button class="btn btn-ghost btn-sm" data-modal-open="editTaskModal">Edit</button>
        <button class="btn btn-ghost btn-sm" data-modal-open="deleteTaskModal">Delete</button>
      </td>
    </tr>`).join("");
}

function renderKanban() {
  const cols = { "To Do": "#col-todo", "In Progress": "#col-progress", "Review": "#col-review", "Completed": "#col-completed" };
  Object.entries(cols).forEach(([status, sel]) => {
    const col = $(sel);
    if (!col) return;
    const tasks = TASKS.filter(t => t.status === status);
    col.innerHTML = tasks.map(t => `
      <div class="card task-card" draggable="true" data-id="${t.id}">
        <div class="tc-title">${t.name}</div>
        <div class="tc-project">${t.project}</div>
        <div class="tc-footer">${priorityBadge(t.priority)}<img class="avatar avatar-sm" src="${AVATARS[t.avatar]}" alt="${t.assignee}"></div>
      </div>`).join("");
    const countEl = document.querySelector(`[data-count-for="${status}"]`);
    if (countEl) countEl.textContent = tasks.length;
  });
  initDragDrop();
}

function initDragDrop() {
  let dragged = null;
  $$(".task-card[draggable]").forEach(card => {
    card.addEventListener("dragstart", () => { dragged = card; card.classList.add("dragging"); });
    card.addEventListener("dragend", () => { card.classList.remove("dragging"); dragged = null; });
  });
  $$(".kanban-cards").forEach(list => {
    list.addEventListener("dragover", e => { e.preventDefault(); list.classList.add("drag-over"); });
    list.addEventListener("dragleave", () => list.classList.remove("drag-over"));
    list.addEventListener("drop", e => {
      e.preventDefault();
      list.classList.remove("drag-over");
      if (dragged) {
        list.appendChild(dragged);
        updateKanbanCounts();
        showToast("Task moved");
      }
    });
  });
}
function updateKanbanCounts() {
  $$(".kanban-col").forEach(col => {
    const count = $$(".task-card", col).length;
    const countEl = $(".count", col);
    if (countEl) countEl.textContent = count;
  });
}

function renderCalendar() {
  const grid = $("#calendarGrid");
  if (!grid) return;
  const monthLabel = $("#calMonthLabel");
  const year = 2024, month = 4; // May 2024 (0-indexed)
  const first = new Date(year, month, 1);
  const startDow = first.getDay();
  const daysInMonth = new Date(year, month + 1, 0).getDate();
  const daysInPrevMonth = new Date(year, month, 0).getDate();
  if (monthLabel) monthLabel.textContent = "May 2024";

  let cells = "";
  ["Sun","Mon","Tue","Wed","Thu","Fri","Sat"].forEach(d => cells += `<div class="cal-dow">${d}</div>`);

  for (let i = 0; i < startDow; i++) {
    const dayNum = daysInPrevMonth - startDow + i + 1;
    cells += `<div class="cal-day other-month"><span class="day-num">${dayNum}</span></div>`;
  }
  for (let d = 1; d <= daysInMonth; d++) {
    const isToday = d === 14;
    const events = CAL_EVENTS[d] || [];
    cells += `<div class="cal-day ${isToday ? 'today' : ''}">
      <span class="day-num">${d}</span>
      ${events.map(ev => `<div class="cal-event ${ev.color}">${ev.label}</div>`).join("")}
    </div>`;
  }
  const totalCells = startDow + daysInMonth;
  const remaining = (7 - (totalCells % 7)) % 7;
  for (let i = 1; i <= remaining; i++) {
    cells += `<div class="cal-day other-month"><span class="day-num">${i}</span></div>`;
  }
  grid.innerHTML = cells;
}

function renderNotifications() {
  const list = $("#notifList");
  if (!list) return;
  list.innerHTML = NOTIFICATIONS.map(n => `
    <div class="notif-item ${n.unread ? 'unread' : ''}">
      <div class="list-ic">${n.icon}</div>
      <div style="flex:1">
        <div class="notif-text">${n.text}</div>
        <div class="notif-time">${n.time}</div>
      </div>
      ${n.unread ? '<span class="notif-unread-dot"></span>' : ''}
    </div>`).join("");
}

function renderReports() {
  const barChart = $("#teamProductivityChart");
  if (barChart) {
    const days = ["Mon","Tue","Wed","Thu","Fri","Sat","Sun"];
    const values = [65, 80, 55, 90, 70, 40, 30];
    barChart.innerHTML = values.map((v, i) => `
      <div class="bar-col">
        <div class="bar" style="height:${v}%"></div>
        <div class="bar-label">${days[i]}</div>
      </div>`).join("");
  }
}

/* ---------------- 8. Init ---------------- */
document.addEventListener("DOMContentLoaded", () => {
  initTheme();
  initShell();
  initModals();
  initForms();

  const page = document.body.dataset.page;
  if (page === "dashboard") renderDashboard();
  if (page === "projects") renderProjects();
  if (page === "tasks") renderTaskTable("#taskTableBody", TASKS);
  if (page === "kanban") renderKanban();
  if (page === "calendar") renderCalendar();
  if (page === "notifications") renderNotifications();
  if (page === "reports") renderReports();
});
