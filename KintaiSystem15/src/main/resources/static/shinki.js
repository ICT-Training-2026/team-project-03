// shinki.js

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
    // 「人事部」を除外
    const allDepts = await res.json();
    deptOptions = allDepts.filter(d => d.departName !== '人事部');
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
  document.getElementById("shinkiForm").addEventListener("submit", async function (e) {
    e.preventDefault();

    const tbody = document.getElementById("userTableBody");
    
    // パスワードのバリデーション用正規表現
    const passwordPattern = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{7,}$/;

    // 既存ユーザーID一覧を取得
    const existing = await fetch("/api/user/all")
                            .then(res => res.json())
                            .then(list => list.map(u => u.userId));

    for (let tr of tbody.children) {
      const newId = tr.querySelector('[name="userId"]').value.trim();
      const password = tr.querySelector('[name="pass"]').value;

      // ユーザーID重複チェック
      if (existing.includes(newId)) {
        alert(`ユーザーID「${newId}」は既に存在しています。別のIDを入力してください。`);
        return; // 処理中断
      }

      // パスワードチェック
      if (!passwordPattern.test(password)) {
        alert(`ユーザーID「${newId}」のパスワードは7桁以上で英字と数字を含む必要があります。`);
        return; // 処理中断
      }
    }

    // 以降の処理は変わらず…
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

    window.location.href = `confirmShinki.html?${params.toString()}`;
  });

});