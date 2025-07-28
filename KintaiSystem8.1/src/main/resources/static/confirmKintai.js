// URLパラメータから取得して画面に反映
const params = new URLSearchParams(window.location.search);
document.getElementById("confirm-date").textContent = params.get("date") || "";

// 勤怠区分ID (status) を表示名に変換して表示
const ATTENDANCE_STATUS_MAP = {
  "A001": "出勤",
  "A002": "欠勤",
  "A003": "振出",
  "A004": "振休",
  "A005": "年休",
  "A006": "休日"
};
const statusId = params.get("status") || "A001"; // URLパラメータからIDを取得
document.getElementById("confirm-status").textContent = ATTENDANCE_STATUS_MAP[statusId] || "";

document.getElementById("confirm-start").textContent = params.get("startTime") || "";
document.getElementById("confirm-end").textContent = params.get("endTime") || "";

// 登録ボタン：fetchでサーバーに送信
document.getElementById("submit-form").addEventListener("submit", async function (e) {
  e.preventDefault();

  const date = params.get("date") || ""; // クエリパラメータから直接取得
  const status = params.get("status") || "A001"; // クエリパラメータから直接取得
  const startTime = params.get("startTime") || "";
  const endTime = params.get("endTime") || "";

  // sessionStorage からログインユーザーを取得
  const loginUserId = sessionStorage.getItem("loginUserId");
  if (!loginUserId) {
    alert("ログインしてください");
    window.location.href = "login.html";
    return;
  }

  const payload = {
    userId: loginUserId,
    status: status, // IDを送信
    date: date,
    inTimeH: parseInt(startTime.split(":")[0]),
    inTimeM: parseInt(startTime.split(":")[1]),
    outTimeH: parseInt(endTime.split(":")[0]),
    outTimeM: parseInt(endTime.split(":")[1]),
    year: parseInt(date.split("-")[0]),
    month: parseInt(date.split("-")[1]),
    day: parseInt(date.split("-")[2])
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