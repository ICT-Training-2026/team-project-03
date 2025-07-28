// global に保持しておいて、どの行にも使い回す配列
let deptOptions = [];

// 部署プルダウンを生成するヘルパー
function populateDeptSelect(selectEl) {
  selectEl.innerHTML = '<option value="">部署を選択</option>';
  deptOptions.forEach(d => {
    const opt = document.createElement("option");
    opt.value = d.departId;
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
    document.querySelectorAll('select[name="departId"]').forEach(populateDeptSelect);
  } catch (err) {
    alert(err.message);
  }
}

// DOM読み込み後の初期化
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
    newRow.querySelector(".row-index").textContent = rowCount + 1;
    newRow.querySelectorAll("input, select").forEach(el => {
      if (el.type === "checkbox") el.checked = false;
      else el.value = "";
    });
    newRow.querySelectorAll('select[name="departId"]').forEach(populateDeptSelect);
    tbody.appendChild(newRow);
  });

  // 行削除
  document.getElementById("removeRow").addEventListener("click", () => {
    if (tbody.children.length > 1) {
      tbody.removeChild(tbody.lastElementChild);
    }
  });

  // フォーム送信: 確認画面へ遷移
  document.getElementById("shinkiForm").addEventListener("submit", function (e) {
    e.preventDefault();
    // 各行からパラメータを組み立て
    const params = new URLSearchParams();
    Array.from(tbody.children).forEach((tr, index) => {
      const i = index + 1;
      const id   = tr.querySelector('[name="userId"]').value;
      const name = tr.querySelector('[name="userName"]').value;
      const pass = tr.querySelector('[name="pass"]').value;
      const mail = tr.querySelector('[name="mail"]').value;
      const dept = tr.querySelector('[name="departId"]').value;
      const date = tr.querySelector('[name="hireDate"]').value;
      const adminChecked = tr.querySelector('[name="admin"]').checked;
      params.append(`id${i}`, id);
      params.append(`name${i}`, name);
      params.append(`pass${i}`, pass);
      params.append(`mail${i}`, mail);
      params.append(`dept${i}`, dept);
      params.append(`date${i}`, date);
      params.append(`admin${i}`, adminChecked ? "1" : "0");
    });

    // 確認画面へ遷移
    window.location.href = `confirmShinki.html?${params.toString()}`;
  });
});
