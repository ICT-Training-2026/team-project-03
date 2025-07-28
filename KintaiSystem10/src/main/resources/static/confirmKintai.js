// confirmKintai.js

document.addEventListener("DOMContentLoaded", () => {
  // 1) URLパラメータから値を取得
  const params    = new URLSearchParams(window.location.search);
  const date      = params.get("date")      || "";
  const statusId  = params.get("status")    || "A001";
  const startTime = params.get("startTime") || "";
  const endTime   = params.get("endTime")   || "";

  // 2) 日付フィールドにセット
  document.getElementById("confirm-date").textContent = date;

  // 3) 勤怠区分ID→表示文字列マッピング
  const ATTENDANCE_STATUS_MAP = {
    "A001": "出勤",
    "A002": "欠勤",
    "A003": "振出",
    "A004": "振休",
    "A005": "年休",
    "A006": "休日"
  };
  document.getElementById("confirm-status")
          .textContent = ATTENDANCE_STATUS_MAP[statusId] || statusId;

  // 4) 出退勤時刻フィールドにセット
  document.getElementById("confirm-start").textContent = startTime;
  document.getElementById("confirm-end").textContent   = endTime;

  // 5) 「登録」ボタン押下時の処理
  document.getElementById("submit-form")
          .addEventListener("submit", async (e) => {
    e.preventDefault();

    // ログインチェック
    const loginUserId = sessionStorage.getItem("loginUserId");
    if (!loginUserId) {
      alert("ログインしてください");
      return window.location.href = "login.html";
    }

    // DTO に合わせて payload 構築
    const payload = {
      userId:    loginUserId,
      date:      date,
      status:    statusId,
      startTime: startTime,
      endTime:   endTime
    };

    try {
      const res = await fetch("/api/kintai/register", {
        method:  "POST",
        headers: { "Content-Type": "application/json" },
        body:    JSON.stringify(payload)
      });
      if (!res.ok) throw new Error(`サーバーエラー(${res.status})`);
      window.location.href = "complete.html";
    } catch (err) {
      alert("登録に失敗しました: " + err.message);
    }
  });
});