function updateClock() {
  const now = new Date();
  const y = now.getFullYear();
  const m = String(now.getMonth() + 1).padStart(2, '0');
  const d = String(now.getDate()).padStart(2, '0');
  const h = String(now.getHours()).padStart(2, '0');
  const min = String(now.getMinutes()).padStart(2, '0');
  document.getElementById("currentDateTime").textContent = `${y}/${m}/${d} ${h}:${min}`;
}
setInterval(updateClock, 1000);
updateClock();

flatpickr("#calendar", {
  locale: "ja",
  inline: true,
  defaultDate: new Date(),
  onChange: function(selectedDates, dateStr) {
    document.getElementById("selectedDate").textContent = dateStr;
    document.getElementById("dateInput").value = dateStr;

    // 勤怠情報取得処理を追加
    const userId = sessionStorage.getItem("loginUserId");
    if (!userId) {
      document.getElementById("kintai-info").textContent = "ユーザー情報が取得できません";
      return;
    }
    fetch(`/api/kintai/user?userId=${encodeURIComponent(userId)}&startDate=${encodeURIComponent(dateStr)}&endDate=${encodeURIComponent(dateStr)}`)
      .then(res => res.json())
      .then(data => {
        if (Array.isArray(data) && data.length > 0) {
          const k = data[0];
          let inTime = (k.inTimeH && k.inTimeM) ? `${k.inTimeH}:${k.inTimeM}` : "--:--";
          let outTime = (k.outTimeH && k.outTimeM) ? `${k.outTimeH}:${k.outTimeM}` : "--:--";
          document.getElementById("kintai-info").textContent = `出勤: ${inTime}　退勤: ${outTime}`;
        } else {
          document.getElementById("kintai-info").textContent = "この日の勤怠情報はありません";
        }
      })
      .catch(() => {
        document.getElementById("kintai-info").textContent = "勤怠情報の取得に失敗しました";
      });
  }
});

// ✅ form に登録イベントのカスタム送信処理（GETでconfirmKintai.htmlへ）
document.getElementById("kintaiForm").addEventListener("submit", function (e) {
  e.preventDefault(); // デフォルト送信停止

  const date = document.getElementById("dateInput").value;
  const status = document.getElementById("status").value;
  const startHour = document.getElementById("startHour").value;
  const startMin = document.getElementById("startMin").value;
  const endHour = document.getElementById("endHour").value;
  const endMin = document.getElementById("endMin").value;

  // 📦 URLを構築してデータ渡す
  const query = `confirmKintai.html?date=${encodeURIComponent(date)}&status=${encodeURIComponent(status)}&startTime=${startHour}:${startMin}&endTime=${endHour}:${endMin}`;
  window.location.href = query;
});
