// URLパラメータから取得して画面に反映
const params = new URLSearchParams(window.location.search);
document.getElementById("confirm-date").textContent = params.get("date") || "";
document.getElementById("confirm-status").textContent = params.get("status") || "";
document.getElementById("confirm-start").textContent = params.get("startTime") || "";
document.getElementById("confirm-end").textContent = params.get("endTime") || "";

// 登録ボタン：fetchでサーバーに送信
document.getElementById("submit-form").addEventListener("submit", function (e) {
  e.preventDefault();

  const date = document.getElementById("confirm-date").textContent;
  const status = document.getElementById("confirm-status").textContent;
  const start = document.getElementById("confirm-start").textContent;
  const end = document.getElementById("confirm-end").textContent;

  fetch("/api/kintai/register", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({
      date: date,
      status: status,
      startTime: start,
      endTime: end,
      userId: "ログイン中のユーザーID" // ← ログイン処理実装後に差し込み
    })
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