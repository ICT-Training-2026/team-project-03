// confirmShinki.js

// 1) URLパラメータから取得して表に表示
const params = new URLSearchParams(window.location.search);
const tableBody = document.querySelector("#confirmTable tbody");
let rowIndex = 1;
while (params.get(`id${rowIndex}`)) {
  const tr = document.createElement("tr");
  tr.innerHTML = `
    <td>${rowIndex}</td>
    <td>${params.get(`id${rowIndex}`) || ""}</td>
    <td>${params.get(`name${rowIndex}`) || ""}</td>
    <td>${params.get(`pass${rowIndex}`) || ""}</td>
    <td>${params.get(`mail${rowIndex}`) || ""}</td>
    <td>${params.get(`dept${rowIndex}`) || ""}</td>
    <td>${params.get(`date${rowIndex}`) || ""}</td>
    <td>${params.get(`admin${rowIndex}`) || ""}</td>
  `;
  tableBody.appendChild(tr);
  rowIndex++;
}

// 2) 「登録」ボタン押下時の処理：サーバへ送信し、完了画面へ遷移
const submitForm = document.getElementById("submit-form");
submitForm.addEventListener("submit", async function (e) {
  e.preventDefault();

  // テーブル行からDTO配列を組み立て
  const users = Array.from(
    document.querySelectorAll("#confirmTable tbody tr")
  ).map(tr => {
    const cols = tr.querySelectorAll("td");
    return {
      userId:   cols[1].textContent.trim(),
      userName: cols[2].textContent.trim(),
      pass:     cols[3].textContent.trim(),
      mail:     cols[4].textContent.trim(),
      departId: cols[5].textContent.trim(),
      hireDate: cols[6].textContent.trim(),  // DTO側の hireDate と一致させる
      admin:    parseInt(cols[7].textContent.trim(), 10)
    };
  });

  try {
    const res = await fetch("/api/user/register", {
      method:  "POST",
      headers: { "Content-Type": "application/json" },
      body:    JSON.stringify(users)
    });
    if (!res.ok) {
      throw new Error(`サーバーエラー(${res.status})`);
    }
    // 完了画面へリダイレクト
    window.location.href = "complete.html";
  } catch (err) {
    alert("登録に失敗しました: " + err.message);
  }
});
