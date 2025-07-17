// 年月セレクト生成
const yearSelect = document.getElementById("yearSelect");
const monthSelect = document.getElementById("monthSelect");
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

// 🔁 模拟“数据库”中的 JSON 数据（后端返回值）
const mockData = [
  {
    departmentId: "A001",
    name: "石井",
    personId: "001",
    days: Array(31).fill(true).map((v, i) => i % 6 !== 0)
  },
  {
    departmentId: "A001",
    name: "倪",
    personId: "002",
    days: Array(31).fill(true).map((v, i) => i % 5 !== 0)
  },
  {
    departmentId: "B002",
    name: "拝藤",
    personId: "003",
    days: Array(31).fill(false).map((v, i) => i % 2 === 0)
  }
];

// 🔍 検索処理（APIの代わりにmockDataから抽出）
document.getElementById("searchForm").addEventListener("submit", function (e) {
  e.preventDefault();
  const deptId = document.getElementById("departmentId").value;
  const personId = document.getElementById("personId").value;
  const year = parseInt(yearSelect.value);
  const month = parseInt(monthSelect.value);

  let filtered = [];

  if (personId) {
    filtered = mockData.filter(p => p.personId === personId);
  } else if (deptId) {
    filtered = mockData.filter(p => p.departmentId === deptId);
  } else {
    alert("部署IDまたは個人IDを入力してください");
    return;
  }

  if (filtered.length === 0) {
    alert("該当データが見つかりませんでした");
    return;
  }

  renderTable(filtered, year, month);
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

// CSV出力
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
