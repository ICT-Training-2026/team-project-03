// URLパラメータから取得して表に表示
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

// 登録ボタン：fetchでサーバーに送信
document.getElementById("submit-form").addEventListener("submit", function (e) {
  e.preventDefault();

  const rows = document.querySelectorAll("#confirmTable tbody tr");
  const users = [];
  rows.forEach(row => {
    const cols = row.querySelectorAll("td");
    users.push({
      id: cols[1].textContent,
      name: cols[2].textContent,
      pass: cols[3].textContent, // ⚠ ハッシュ化はサーバー側で
      mail: cols[4].textContent,
      dept: cols[5].textContent,
      date: cols[6].textContent
    });
  });

  fetch("/api/user/register", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(users)
  })
  .then(res => {
    if (!res.ok) {
      throw new Error("サーバーエラー");
    }
    return res.json();
  })
  .then(() => {
    window.location.href = "complete.html";
  })
  .catch(err => {
    alert("登録に失敗しました: " + err.message);
  });
});