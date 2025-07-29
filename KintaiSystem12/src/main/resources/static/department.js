// department.js

// --- セレクタ取得 ---
const yearSelect             = document.getElementById("yearSelect");
const monthSelect            = document.getElementById("monthSelect");
const departmentSelect       = document.getElementById("departmentSelect");
const selectedDepartmentName = document.getElementById("selectedDepartmentName");
const exportBtn              = document.getElementById("exportCSV");

// --- ユーザーマップ & 最新取得データ保持用 ---
const userMap     = {};   // userId → userName
let currentList   = [];   // 現在表示中の勤怠データ配列

// 1) 全ユーザー取得
async function loadUsers() {
  const res = await fetch("/api/user/all");
  if (!res.ok) throw new Error("ユーザー情報の取得に失敗しました");
  (await res.json()).forEach(u => userMap[u.userId] = u.userName);
}

// 2) 全部署取得
async function loadDepartments() {
  const res = await fetch("/api/department/all");
  if (!res.ok) throw new Error("部署情報の取得に失敗しました");
  // 「人事部」を除外
  const allDepts = await res.json();
  const depts = allDepts.filter(d => d.departName !== '人事部');

  departmentSelect.innerHTML = "";
  depts.forEach(d => {
    const opt = new Option(d.departName, d.departId);
    departmentSelect.append(opt);
  });
  // 初期表示
  if (depts.length) {
    departmentSelect.value = depts[0].departId;
    selectedDepartmentName.textContent = `${depts[0].departName}勤怠情報`;
    fetchKintaiData();
  }
}

// 3) 年月セレクト初期化
;(function initYearMonth() {
  const today = new Date();
  for (let y = today.getFullYear() - 5; y <= today.getFullYear() + 5; y++) {
    const o = new Option(y, y);
    if (y === today.getFullYear()) o.selected = true;
    yearSelect.append(o);
  }
  for (let m = 1; m <= 12; m++) {
    const o = new Option(m.toString().padStart(2,"0"), m);
    if (m === today.getMonth() + 1) o.selected = true;
    monthSelect.append(o);
  }
})();

// 4) 検索フォーム制御
departmentSelect.addEventListener("change", () => {
  selectedDepartmentName.textContent =
    departmentSelect.selectedOptions[0].textContent + "勤怠情報";
});
document.getElementById("searchForm").addEventListener("submit", e => {
  e.preventDefault();
  fetchKintaiData();
});

// 5) 勤怠データ取得＋描画
async function fetchKintaiData() {
  const dept  = departmentSelect.value;
  const year  = yearSelect.value;
  const month = monthSelect.value.padStart(2,"0");
  const start = `${year}-${month}-01`;
  const end   = `${year}-${month}-${new Date(year, month, 0).getDate()}`;

  const res = await fetch(
    `/api/kintai/department?departmentId=${dept}` +
    `&startDate=${start}&endDate=${end}`
  );
  if (!res.ok) throw new Error("勤怠データ取得失敗");
  const list = await res.json();
  currentList = list;  // CSV 用に保持

  const lastDay = new Date(year, month, 0).getDate();
  renderHeader(lastDay, year, month);
  renderTableWithCheckboxes(list, lastDay);
}

// 6) ヘッダー（二段）描画
function renderHeader(lastDay, year, month) {
  const thead = document.querySelector("#attendanceTable thead");
  thead.innerHTML = "";
  const wdMap = ['日','月','火','水','木','金','土'];

  const tr1 = document.createElement("tr");
  tr1.innerHTML = `<th></th><th></th>`
    + Array.from({length: lastDay}, (_, i) => `<th>${i+1}</th>`).join("");
  const tr2 = document.createElement("tr");
  tr2.innerHTML = `<th></th><th>曜日</th>`
    + Array.from({length: lastDay}, (_, i) => {
        const wd = new Date(year, month-1, i+1).getDay();
        return `<th>${wdMap[wd]}</th>`;
      }).join("");
  thead.append(tr1, tr2);
}

// 7) 本文＋色分け描画
function renderTableWithCheckboxes(list, lastDay) {
  const tbody = document.getElementById("tableBody");
  tbody.innerHTML = "";
  const map = {};
  list.forEach(k => {
    map[k.userId] = map[k.userId] || {};
    map[k.userId][k.day] = k;
  });

  Object.keys(map).forEach(uid => {
    const tr = document.createElement("tr");
    const name = userMap[uid] || uid;
    tr.innerHTML =
      `<td><input type="checkbox" class="chk-user" data-userid="${uid}"></td>` +
      `<td>${name}</td>`;
    for (let d = 1; d <= lastDay; d++) {
      const rec = map[uid][d];
      const td  = document.createElement("td");
      if (rec) {
        if (rec.attId === "1" || rec.attId === "A001") {
          td.className = "green-cell";
        } else if (rec.attId === "2" || rec.attId === "A002" ||
                   rec.attId === "6" || rec.attId === "A006") {
          td.className = "red-cell";
        }
        const inTime  = rec.inTimeH ? `${rec.inTimeH}:${rec.inTimeM}` : "--:--";
        const outTime = rec.outTimeH ? `${rec.outTimeH}:${rec.outTimeM}` : "--:--";
        const statusName = {"1":"出勤","2":"欠勤","6":"休日"}[rec.attId] || "";
        td.title = `${statusName}\n出勤: ${inTime}\n退勤: ${outTime}`;
      }
      tr.append(td);
    }
    tbody.append(tr);
  });
}

// 8) CSV出力（既存のコードそのまま）
exportBtn.addEventListener("click", () => {
  /* (省略) */
});

// 9) 初期ロード
document.addEventListener("DOMContentLoaded", async () => {
  try {
    await loadUsers();
    await loadDepartments();
  } catch (e) {
    alert(e.message);
  }
});