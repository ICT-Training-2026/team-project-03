const yearSelect = document.getElementById("yearSelect");
const monthSelect = document.getElementById("monthSelect");
const departmentSelect = document.getElementById("departmentSelect");
const selectedDepartmentName = document.getElementById("selectedDepartmentName");

// 全部署情報を取得してドロップダウンに設定
async function loadDepartments() {
  try {
    const response = await fetch("/api/department/all");
    if (!response.ok) throw new Error("部署情報の取得に失敗しました");
    const departments = await response.json();

    departmentSelect.innerHTML = ""; // クリア
    departments.forEach(dept => {
      const option = document.createElement("option");
      option.value = dept.departId;
      option.textContent = dept.departName;
      departmentSelect.appendChild(option);
    });

    // ロード後、最初の部署を選択して勤怠情報を表示
    if (departments.length > 0) {
      departmentSelect.value = departments[0].departId;
      selectedDepartmentName.textContent = departments[0].departName + "部署勤怠情報";
      fetchKintaiData();
    }

  } catch (error) {
    alert("部署情報のロードに失敗しました: " + error.message);
  }
}

// 年月セレクト初期化
const currentYear = new Date().getFullYear();
const currentMonth = new Date().getMonth() + 1;

for (let y = currentYear - 5; y <= currentYear + 5; y++) {
  const opt = document.createElement("option");
  opt.value = y;
  opt.textContent = y;
  if (y === currentYear) opt.selected = true;
  yearSelect.appendChild(opt);
}
for (let m = 1; m <= 12; m++) {
  const opt = document.createElement("option");
  opt.value = m;
  opt.textContent = m.toString().padStart(2, "0");
  if (m === currentMonth) opt.selected = true;
  monthSelect.appendChild(opt);
}

// 部署選択時のイベントハンドラ
departmentSelect.addEventListener("change", () => {
  const selectedOption = departmentSelect.options[departmentSelect.selectedIndex];
  selectedDepartmentName.textContent = selectedOption.textContent + "部署勤怠情報";
});

// 検索処理：APIに接続
document.getElementById("searchForm").addEventListener("submit", function (e) {
  e.preventDefault();
  fetchKintaiData();
});

async function fetchKintaiData() {
  const departmentId = departmentSelect.value;
  const year = yearSelect.value;
  const month = monthSelect.value;

  if (!departmentId) {
    alert("部署を選択してください");
    return;
  }

  const startDate = `${year}-${String(month).padStart(2, '0')}-01`;
  const endDate = `${year}-${String(month).padStart(2, '0')}-${new Date(year, month, 0).getDate()}`;

  const api = `/api/kintai/department?departmentId=${encodeURIComponent(departmentId)}&startDate=${encodeURIComponent(startDate)}&endDate=${encodeURIComponent(endDate)}`;

  try {
    const res = await fetch(api);
    if (!res.ok) throw new Error("データ取得エラー");
    const data = await res.json();
    renderTable(data);
  } catch (err) {
    alert("取得に失敗しました: " + err.message);
  }
}

// 表描画
function renderTable(kintaiList) {
  const body = document.getElementById("tableBody");
  body.innerHTML = "";

  if (kintaiList.length === 0) {
    const row = document.createElement("tr");
    row.innerHTML = `<td colspan="5">この部署の勤怠情報はありません</td>`;
    body.appendChild(row);
    return;
  }

  // ユーザーごとに勤怠情報をまとめる (必要であれば)
  // 現状は日付ごとに表示するシンプルな形式なので、このままでOK
  kintaiList.forEach(k => {
    const row = document.createElement("tr");
    const dateStr = `${k.month}/${k.day}`; // 日付表示を "月/日" 形式に
    const inTime = (k.inTimeH && k.inTimeM) ? `${k.inTimeH}:${k.inTimeM}` : "--:--";
    const outTime = (k.outTimeH && k.outTimeM) ? `${k.outTimeH}:${k.outTimeM}` : "--:--";

    // 勤怠区分名のマッピング (AttIdを名前に変換)
    let statusName = "不明";
    switch (k.attId) {
      case "A001": statusName = "出勤"; break;
      case "A002": statusName = "欠勤"; break;
      case "A003": statusName = "振出"; break;
      case "A004": statusName = "振休"; break;
      case "A005": statusName = "年休"; break;
      case "A006": statusName = "休日"; break;
      default: statusName = k.attId; break;
    }

    row.innerHTML = `
      <td>${k.userId}</td>
      <td>${dateStr}</td>
      <td>${inTime}</td>
      <td>${outTime}</td>
      <td>${statusName}</td>
    `;
    body.appendChild(row);
  });
}

// CSV出力処理はそのまま (変更なし)
document.getElementById("exportCSV").addEventListener("click", function () {
  const rows = document.querySelectorAll("table tr");
  let csv = "";
  rows.forEach(row => {
    const cols = row.querySelectorAll("th, td");
    const line = Array.from(cols).map(c => c.innerText).join(",");
    csv += line + "\n";
  });

  const blob = new Blob([csv], { type: "text/csv;charset=utf-8," });
  const link = document.createElement("a");
  link.href = URL.createObjectURL(blob);
  link.download = "attendance.csv";
  link.click();
});

// ページロード時に部署をロード
document.addEventListener("DOMContentLoaded", loadDepartments);