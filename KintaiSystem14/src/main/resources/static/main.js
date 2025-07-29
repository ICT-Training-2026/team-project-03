console.log("main.js が読み込まれました！");

// main.js - 勤怠登録画面のフロントエンドロジック

document.addEventListener("DOMContentLoaded", () => {

  // -----------------------------------------------
  // ログアウト処理
  // -----------------------------------------------
  const logoutBtn = document.getElementById("logoutBtn");
  if (logoutBtn) {
    logoutBtn.addEventListener("click", e => {
      e.preventDefault();
      sessionStorage.removeItem("loginUserId");
      sessionStorage.removeItem("loginUserName");
      sessionStorage.removeItem("loginUserAdmin");
      window.location.href = "login.html";
    });
  }

  // -----------------------------------------------
  // ログイン情報チェック
  // -----------------------------------------------
  const loginUserName  = sessionStorage.getItem("loginUserName");
  const loginUserAdmin = sessionStorage.getItem("loginUserAdmin");
  const isAdmin        = loginUserAdmin === "1";
  if (!loginUserName) {
    alert("ログインしてください");
    return window.location.href = "login.html";
  } else {
    document.getElementById("login-user-info").textContent = `${loginUserName} さんがログイン中です`;
  }

  // -----------------------------------------------
  // 管理者権限制御
  // -----------------------------------------------
  const linkNew  = document.getElementById("link-new");
  const linkDept = document.getElementById("link-dept");
  if (linkNew) {
    linkNew.addEventListener("click", e => {
      if (!isAdmin) {
        e.preventDefault();
        alert("管理者のみ新規ユーザー登録が可能です。");
      }
    });
  }
  if (linkDept) {
    linkDept.addEventListener("click", e => {
      if (!isAdmin) {
        e.preventDefault();
        alert("管理者のみ部署別勤怠情報の閲覧が可能です。");
      }
    });
  }

  // -----------------------------------------------
  // 現在時刻表示
  // -----------------------------------------------
  function updateClock() {
    const now     = new Date();
    const year    = now.getFullYear();
    const month   = String(now.getMonth() + 1).padStart(2, '0');
    const day     = String(now.getDate()).padStart(2, '0');
    const hours   = String(now.getHours()).padStart(2, '0');
    const minutes = String(now.getMinutes()).padStart(2, '0');
    document.getElementById("currentDateTime").textContent =
      `${year}/${month}/${day} ${hours}:${minutes}`;
  }
  setInterval(updateClock, 1000);
  updateClock();

  // -----------------------------------------------
  // カレンダー機能
  // -----------------------------------------------
  const displayDateInput         = document.getElementById("displayDateInput");
  const displaySelectedDateSpan  = document.getElementById("displaySelectedDate");
  const registerDateInput        = document.getElementById("registerDateInput");
  const registerSelectedDateSpan = document.getElementById("registerSelectedDate");

  flatpickr("#calendar", {
    locale: "ja",
    inline: true,
    defaultDate: new Date(),
    onChange: function(selectedDates, dateStr) {
      displaySelectedDateSpan.textContent  = dateStr;
      displayDateInput.value               = dateStr;
      registerSelectedDateSpan.textContent = dateStr;
      registerDateInput.value              = dateStr;
    }
  });

  // -----------------------------------------------
  // メッセージ表示
  // -----------------------------------------------
  const messageArea = document.getElementById("messageArea");
  function showMessage(message, type = 'error') {
    messageArea.textContent = message;
    messageArea.style.color = type === 'error'
      ? 'red'
      : (type === 'success' ? 'green' : 'blue');
    setTimeout(() => messageArea.textContent = '', 5000);
  }

  // -----------------------------------------------
  // 勤怠区分マッピング
  // -----------------------------------------------
  const ATTENDANCE_STATUS_MAP = {
    "A001": "出勤", "A002": "欠勤", "A003": "振出",
    "A004": "振休", "A005": "年休", "A006": "休日"
  };
  const REVERSE_ATTENDANCE_STATUS_MAP = {
    "出勤": "A001", "欠勤": "A002", "振出": "A003",
    "振休": "A004", "年休": "A005", "休日": "A006"
  };

  // -----------------------------------------------
  // 勤怠情報 検索機能
  // -----------------------------------------------
  const searchKintaiBtn   = document.getElementById("searchKintaiBtn");
  const displayStatus     = document.getElementById("displayStatus");
  const displayStartHour  = document.getElementById("displayStartHour");
  const displayStartMin   = document.getElementById("displayStartMin");
  const displayBreakHour  = document.getElementById("displayBreakHour");
  const displayBreakMin   = document.getElementById("displayBreakMin");
  const displayEndHour    = document.getElementById("displayEndHour");
  const displayEndMin     = document.getElementById("displayEndMin");

  async function searchAndDisplayKintai() {
    const date   = displayDateInput.value;
    const userId = sessionStorage.getItem("loginUserId");
    if (!userId) {
      showMessage("ログイン情報がありません");
      return;
    }
    if (!date) {
      showMessage("日付を選択してください");
      return;
    }

    try {
      const res = await fetch(
        `/api/kintai/user?userId=${encodeURIComponent(userId)}` +
        `&startDate=${date}&endDate=${date}`
      );
      if (!res.ok) throw new Error(`サーバーエラー(${res.status})`);

      const list = await res.json();
      if (!Array.isArray(list) || list.length === 0) {
        displayStatus.value     = "出勤";
        displayStartHour.value  = "";
        displayStartMin.value   = "";
        displayBreakHour.value  = "";
        displayBreakMin.value   = "";
        displayEndHour.value    = "";
        displayEndMin.value     = "";
        showMessage("該当する勤怠情報がありません", 'info');
      } else {
        const k = list[0];
        displayStatus.value     = ATTENDANCE_STATUS_MAP[k.attId] || k.attId;
        displayStartHour.value  = k.inTimeH    || "";
        displayStartMin.value   = k.inTimeM    || "";
        displayBreakHour.value  = k.breakTimeH != null ? k.breakTimeH : "";
        displayBreakMin.value   = k.breakTimeM != null ? k.breakTimeM : "";
        displayEndHour.value    = k.outTimeH   || "";
        displayEndMin.value     = k.outTimeM   || "";
      }
    } catch (err) {
      showMessage(`勤怠情報の取得に失敗しました: ${err.message}`);
    }
  }

  if (searchKintaiBtn) {
    searchKintaiBtn.addEventListener("click", searchAndDisplayKintai);
  }

  // -----------------------------------------------
  // コピー機能（検索結果を登録フォームへコピー）
  // -----------------------------------------------
  const copyToRegisterBtn = document.getElementById("copyToRegisterBtn");
  if (copyToRegisterBtn) {
    copyToRegisterBtn.addEventListener("click", () => {
      // 日付コピー
      registerDateInput.value              = displayDateInput.value;
      registerSelectedDateSpan.textContent = displaySelectedDateSpan.textContent;
      // ステータスコピー
      document.getElementById("registerStatus").value    = displayStatus.value;
      // 時刻コピー
      document.getElementById("registerStartHour").value = displayStartHour.value;
      document.getElementById("registerStartMin").value  = displayStartMin.value;
      // 休憩時間コピー
      document.getElementById("registerBreakHour").value = displayBreakHour.value;
      document.getElementById("registerBreakMin").value  = displayBreakMin.value;
      // 退勤時間コピー
      document.getElementById("registerEndHour").value   = displayEndHour.value;
      document.getElementById("registerEndMin").value    = displayEndMin.value;
      showMessage("登録フォームにコピーしました", 'success');
    });
  }

  // -----------------------------------------------
  // 登録フォーム送信（確認ページへ遷移）
  // -----------------------------------------------
  const kintaiRegisterForm = document.getElementById("kintaiRegisterForm");
  if (kintaiRegisterForm) {
    kintaiRegisterForm.addEventListener("submit", function(e) {
      e.preventDefault();
      const date       = registerDateInput.value;
      const statusText = document.getElementById("registerStatus").value;
      const status     = REVERSE_ATTENDANCE_STATUS_MAP[statusText] || "A001";
      const sh         = document.getElementById("registerStartHour").value.padStart(2,'0');
      const sm         = document.getElementById("registerStartMin").value.padStart(2,'0');
      const bh         = document.getElementById("registerBreakHour").value.padStart(2,'0');
      const bm         = document.getElementById("registerBreakMin").value.padStart(2,'0');
      const eh         = document.getElementById("registerEndHour").value.padStart(2,'0');
      const em         = document.getElementById("registerEndMin").value.padStart(2,'0');
      const startTime  = `${sh}:${sm}`;
      const endTime    = `${eh}:${em}`;
      const params     = new URLSearchParams({ date, status, startTime, endTime });
      // 休憩時間パラメータ追加
      params.append("breakTimeH", bh);
      params.append("breakTimeM", bm);
      window.location.href = `confirmKintai.html?${params.toString()}`;
    });
  }

});