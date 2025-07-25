// URLパラメータから取得して画面に反映
const params = new URLSearchParams(window.location.search);
document.getElementById("confirm-date").textContent = params.get("date") || "";
document.getElementById("confirm-status").textContent = params.get("status") || "";
document.getElementById("confirm-start").textContent = params.get("startTime") || "";
document.getElementById("confirm-end").textContent = params.get("endTime") || "";

// 登録ボタン：fetchでサーバーに送信
document.getElementById("submit-form").addEventListener("submit", async function (e) {
  e.preventDefault();

  const date = document.getElementById("confirm-date").textContent;
  const statusName = document.getElementById("confirm-status").textContent;
  const startTime = document.getElementById("confirm-start").textContent; // 例: "9:10"
  const endTime = document.getElementById("confirm-end").textContent;     // 例: "18:10"

  // sessionStorage からログインユーザーを取得
  const loginUserId = sessionStorage.getItem("loginUserId");
  if (!loginUserId) {
    alert("ログインしてください");
    window.location.href = "login.html";
    return;
  }

  // 出勤区分名からIDをマッピング
  const attMap = {
    "出勤": "1",
    "欠勤": "2",
    "振出": "3",
    "振休": "4",
    "年休": "5",
    "休日": "6"
  };
  const attId = attMap[statusName] || "1";

  // ✅ DTO に合わせたフィールド名で送信
  const payload = {
    userId: loginUserId,
    status: attId,       // ← ここを status に変更
    date: date,
    startTime: startTime, // ← DTO の startTime に合わせて送信
    endTime: endTime      // ← DTO の endTime に合わせて送信
  };

  console.log("送信データ:", payload);

  try {
    const res = await fetch("/api/kintai/register", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payload)
    });

    if (!res.ok) {
      throw new Error("サーバーエラー");
    }

    // 完了画面へ
    window.location.href = "complete.html";
  } catch (err) {
    alert("登録に失敗しました: " + err.message);
  }
});