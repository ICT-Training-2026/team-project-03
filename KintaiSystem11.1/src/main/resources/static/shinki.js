// global に保持しておいて、どの行にも使い回す配列
let deptOptions = [];

// 部署プルダウンを生成するヘルパー
function populateDeptSelect(selectEl) {
  selectEl.innerHTML = '<option value="">部署を選択</option>';
  deptOptions.forEach(d => {
    const opt = document.createElement("option");
    opt.value = d.departId;
    // 表示は「部署名 (ID)」形式
    opt.textContent = `${d.departName} (${d.departId})`;
    selectEl.appendChild(opt);
  });
}

// API から部署一覧を取得して deptOptions にセット、既存行にも反映
async function loadDepartments() {
  try {
    const res = await fetch("/api/department/all");
    if (!res.ok) throw new Error("部署情報の取得に失敗しました");
    deptOptions = await res.json();
    // 既存の <select name="departId"> 全てにプルダウンを流し込む
    document.querySelectorAll('select[name="departId"]').forEach(populateDeptSelect);
  } catch (err) {
    alert(err.message);
  }
}

// flatpickr の初期化と、初期データ読み込み
document.addEventListener("DOMContentLoaded", () => {
  // 日付ピッカー
  flatpickr(".flatpickr", {
    locale: "ja",
    dateFormat: "Y-m-d",
    allowInput: true
  });

  // 部署一覧ロード
  loadDepartments();

  const tbody = document.getElementById("userTableBody");

  // 行追加
  document.getElementById("addRow").addEventListener("click", () => {
    const rowCount = tbody.children.length;
    const newRow = tbody.children[0].cloneNode(true);
    // 行番号更新
    newRow.querySelector(".row-index").textContent = rowCount + 1;
    // 各入力欄クリア
    newRow.querySelectorAll("input, select").forEach(el => {
      if (el.type === "checkbox") el.checked = false;
      else el.value = "";
    });
    // 部署 <select> は再度プルダウン埋め
    newRow.querySelectorAll('select[name="departId"]').forEach(populateDeptSelect);
    tbody.appendChild(newRow);
  });

  // 行削除
  document.getElementById("removeRow").addEventListener("click", () => {
    if (tbody.children.length > 1) {
      tbody.removeChild(tbody.lastElementChild);
    }
  });

  // フォーム送信
  document.getElementById("shinkiForm").addEventListener("submit", async function(e) {
    e.preventDefault();
    // 各行から DTO を組み立て
    const dtos = Array.from(tbody.children).map(tr => {
      const getVal = name => tr.querySelector(`[name="${name}"]`).value;
      const isAdmin = tr.querySelector(`[name="admin"]`).checked;
      return {
        userId:   getVal("userId"),
        userName: getVal("userName"),
        pass:     getVal("pass"),
        mail:     getVal("mail"),
        departId: getVal("departId"),    // プルダウンから選ばれたID
        hireDate: getVal("hireDate"),
        admin:    isAdmin ? 1 : 0
      };
    });

    try {
      const res = await fetch("/api/user/register", {
        method:  "POST",
        headers: { "Content-Type": "application/json" },
        body:    JSON.stringify(dtos)
      });
      if (!res.ok) throw new Error(res.statusText);
      alert("登録完了");
      window.location.href = "main.html";
    } catch (err) {
      alert("登録に失敗しました: " + err.message);
    }
  });
});