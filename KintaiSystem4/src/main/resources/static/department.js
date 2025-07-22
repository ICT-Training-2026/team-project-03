const yearSelect = document.getElementById("yearSelect");
const monthSelect = document.getElementById("monthSelect");

// 年月セレクト初期化
for (let y = 1900; y <= 2100; y++) {
  const opt = document.createElement("option");
  opt.value = y;
  opt.textContent = y;
  if (y === 2025) opt.selected = true;
  yearSelect.appendChild(opt);
}
for (let m = 1; m <= 12; m++) {
  const opt = document.createElement("option");
  opt.value = m;
  opt.textContent = m.toString().padStart(2, "0");
  if (m === 7) opt.selected = true;
  monthSelect.appendChild(opt);
}

// 検索処理：APIに接続
document.getElementById("searchForm").addEventListener("submit", function (e) {
  e.preventDefault();
  const deptId = document.getElementById("departmentId").value;
  const personId = document.getElementById("personId").value;
  const year = parseInt(yearSelect.value);
  const month = parseInt(monthSelect.value);

  let api = "";
  if (personId) {
    api = `/api/attendance?personId=${personId}&year=${year}&month=${month}`;
  } else if (deptId) {
    api = `/api/attendance?departmentId=${deptId}&year=${year}&month=${month}`;
  } else {
    alert("部署IDまたは個人IDを入力してください");
    return;
  }

  fetch(api)
    .then(res => {
      if (!res.ok) throw new Error("データ取得エラー");
      return res.json();
    })
    .then(data => {
      renderTable(data, year, month);
    })
    .catch(err => {
      alert("取得に失敗しました: " + err.message);
    });
});

// 表描画
function renderTable(data, year, month) {
  const dayRow = document.getElementById("dayRow");
  const weekRow = document.getElementById("weekRow");
  const body = document.getElementById("tableBody");

  dayRow.innerHTML = "<th rowspan='2'>曜日</th><th rowspan='2'>氏名</th>";
  weekRow.innerHTML = "";

  const daysInMonth = new Date(year, month, 0).getDate();
  for (let d = 1; d <= daysInMonth; d++) {
    const date = new Date(year, month - 1, d);
    const weekday = ["日", "月", "火", "水", "木", "金", "土"][date.getDay()];
    const gray = (weekday === "土" || weekday === "日") ? " style='background:#eee'" : "";
    dayRow.innerHTML += `<th${gray}>${d}</th>`;
    weekRow.innerHTML += `<th${gray}>${weekday}</th>`;
  }

  body.innerHTML = "";
  for (const person of data) {
    const row = document.createElement("tr");
    const nameCell = `<td>${person.name}</td>`;
    let tdList = "";
    for (let i = 0; i < daysInMonth; i++) {
      const status = person.days[i];
      const cls = status ? "class='green-cell'" : "class='red-cell'";
      tdList += `<td ${cls}></td>`;
    }
    row.innerHTML = `<td></td>${nameCell}${tdList}`;
    body.appendChild(row);
  }
}

// CSV出力処理はそのまま
document.getElementById("exportCSV").addEventListener("click", function () {
  const rows = document.querySelectorAll("table tr");
  let csv = "";
  rows.forEach(row => {
    const cols = row.querySelectorAll("th, td");
    const line = Array.from(cols).map(c => c.innerText).join(",");
    csv += line + "\n";
  });

  const blob = new Blob([csv], { type: "text/csv" });
  const link = document.createElement("a");
  link.href = URL.createObjectURL(blob);
  link.download = "attendance.csv";
  link.click();
});