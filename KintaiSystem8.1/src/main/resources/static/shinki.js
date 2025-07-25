// flatpickr を使って、.flatpickr クラスの input に日付ピッカーを適用
flatpickr(".flatpickr", {
  locale: "ja",
  dateFormat: "Y-m-d",
  allowInput: true
});

// 行の追加・削除
const tbody = document.getElementById("userTableBody");

document.getElementById("addRow").addEventListener("click", () => {
  const rowCount = tbody.children.length;
  const newRow = tbody.children[0].cloneNode(true);
  // 行番号を更新
  newRow.querySelector(".row-index").textContent = rowCount + 1;
  // 各入力欄をクリア
  newRow.querySelectorAll("input, select").forEach(el => {
    if (el.type === "checkbox") el.checked = false;
    else el.value = "";
  });
  tbody.appendChild(newRow);
});

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
      departId: getVal("departId"),
      hireDate: getVal("hireDate"),
      admin:    isAdmin ? 1 : 0
    };
  });

  try {
    const res = await fetch("/api/user/register", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(dtos)
    });
    if (!res.ok) throw new Error(res.statusText);
    alert("登録完了");
    window.location.href = "main.html";
  } catch (err) {
    alert("登録に失敗しました: " + err.message);
  }
});