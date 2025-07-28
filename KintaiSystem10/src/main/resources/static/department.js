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
  const depts = await res.json();
  departmentSelect.innerHTML = "";
  depts.forEach(d => {
    const opt = new Option(d.departName, d.departId);
    departmentSelect.append(opt);
  });
  // 初期表示
  if (depts.length) {
    departmentSelect.value = depts[0].departId;
    selectedDepartmentName.textContent = `${depts[0].departName}部署勤怠情報`;
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
    departmentSelect.selectedOptions[0].textContent + "部署勤怠情報";
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

  // 日付行（チェック列＋氏名列＋日付）
  const tr1 = document.createElement("tr");
  tr1.innerHTML = `<th></th><th></th>`  // 空セル×2
    + Array.from({length: lastDay}, (_, i) => `<th>${i+1}</th>`).join("");

  // 曜日行（チェック列＋曜日ヘッダ＋曜日）
  const tr2 = document.createElement("tr");
  tr2.innerHTML = `<th></th><th>曜日</th>`
    + Array.from({length: lastDay}, (_, i) => {
        const wd = new Date(year, month-1, i+1).getDay();
        return `<th>${wdMap[wd]}</th>`;
      }).join("");

  thead.append(tr1, tr2);
}

// 7) 本文＋チェックボックス＋ツールチップ描画
function renderTableWithCheckboxes(list, lastDay) {
  const tbody = document.getElementById("tableBody");
  tbody.innerHTML = "";

  // userId→日付→レコード の二重マップ
  const map = {};
  list.forEach(k => {
    map[k.userId] = map[k.userId] || {};
    map[k.userId][k.day] = k;
  });

  Object.keys(map).forEach(uid => {
    const tr = document.createElement("tr");
    const name = userMap[uid] || uid;
    // チェックボックス列 ＋ 氏名列
    tr.innerHTML = 
      `<td><input type="checkbox" class="chk-user" data-userid="${uid}"></td>` +
      `<td>${name}</td>`;

    // 日付セル
    for (let d = 1; d <= lastDay; d++) {
      const rec = map[uid][d];
      const td  = document.createElement("td");
      if (rec) {
        const statusName = {
          "1":"出勤","2":"欠勤","3":"振出",
          "4":"振休","5":"年休","6":"休日"
        }[rec.attId] || rec.attId;
        const inTime  = rec.inTimeH!=null  ? `${rec.inTimeH}:${rec.inTimeM}`  : "--:--";
        const outTime = rec.outTimeH!=null ? `${rec.outTimeH}:${rec.outTimeM}` : "--:--";
        
        // 勤怠区分に基づいてセルのクラスを設定
        if (rec.attId === "1" || rec.attId === "A001") {
          td.className = "green-cell"; // 通常出勤は緑
        } else if (rec.attId === "2" || rec.attId === "A002" || // 欠勤
                   rec.attId === "6" || rec.attId === "A006") { // 休日
          td.className = "red-cell"; // 欠勤・休日は赤
        } else {
          td.className = ""; // その他の勤怠区分はスタイルなし (必要に応じて追加)
        }

        td.title = `${statusName}\n出勤: ${inTime}\n退勤: ${outTime}`;
      }
      tr.append(td);
    }
    tbody.append(tr);
  });
}

// 8) CSV出力（チェックされたユーザーのみ、BOM付き）
exportBtn.addEventListener("click", () => {
  // 選択されたユーザーIDを取得
  const selectedIds = Array.from(
    document.querySelectorAll(".chk-user:checked")
  ).map(cb => cb.dataset.userid);

  if (selectedIds.length === 0) {
    alert("CSV出力する社員を選択してください");
    return;
  }

  // 選択ユーザーのレコードに絞り込み
  const filtered = currentList.filter(rec => selectedIds.includes(rec.userId));

  if (filtered.length === 0) {
    alert("選択ユーザーに勤怠データがありません");
    return;
  }

  // 先方フォーマットのヘッダー
  let csv = [
    "社員コード",
    "年月",
    "始業時刻(時)",
    "始業時刻(分)",
    "就業時刻(時)",
    "終業時刻(分)",
    "労働時間",
    "休憩時間",
    "超過時間"
  ].join(",") + "\n";

  // データ行
  filtered.forEach(rec => {
    const emp    = rec.userId;
    const ym     = `${rec.year}-${String(rec.month).padStart(2,"0")}`;
    const inH    = rec.inTimeH  || "0";
    const inM    = rec.inTimeM  || "0";
    const outH   = rec.outTimeH || "0";
    const outM   = rec.outTimeM || "0";
    const startMin = parseInt(inH)*60 + parseInt(inM);
    const endMin   = parseInt(outH)*60 + parseInt(outM);
    let totalMin   = Math.max(endMin - startMin, 0);
    const breakMin = totalMin > 0 ? 60 : 0;
    const workMin  = Math.max(totalMin - breakMin, 0);
    const overtime = Math.max(Math.floor(workMin/60) - 8, 0);
    const workH    = Math.floor(workMin/60);
    const breakH   = Math.floor(breakMin/60);

    csv += [
      emp,
      ym,
      inH,
      inM,
      outH,
      outM,
      workH,
      breakH,
      overtime
    ].join(",") + "\n";
  });

  // BOM を付与
  const bom  = "\uFEFF";
  const blob = new Blob([bom + csv], { type: "text/csv;charset=utf-8;" });
  const link = document.createElement("a");
  link.href     = URL.createObjectURL(blob);
  link.download = `attendance_${departmentSelect.value}_${yearSelect.value}_${monthSelect.value}.csv`;
  link.click();
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