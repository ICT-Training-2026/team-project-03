console.log("main.js が読み込まれました！");

// main.js - 勤怠登録画面のフロントエンドロジック

document.addEventListener("DOMContentLoaded", () => {

  // -----------------------------------------------
  // ログアウト処理追加
  // -----------------------------------------------
  const logoutBtn = document.getElementById("logoutBtn");
  if (logoutBtn) {
    logoutBtn.addEventListener("click", e => {
      e.preventDefault();
      // セッション情報をクリア
      sessionStorage.removeItem("loginUserId");
      sessionStorage.removeItem("loginUserName");
      sessionStorage.removeItem("loginUserAdmin");
      // ログイン画面へリダイレクト
      window.location.href = "login.html";
    });
  }

  // -----------------------------------------------
  // ログイン情報チェック
  // -----------------------------------------------
  const loginUserName  = sessionStorage.getItem("loginUserName");
  const loginUserAdmin = sessionStorage.getItem("loginUserAdmin");
  const isAdmin        = loginUserAdmin === "1"; // 管理者かどうかを判定

  // 未ログインならログイン画面へ強制遷移
  if (!loginUserName) {
    alert("ログインしてください");
    window.location.href = "login.html";
  } else {
    // ログイン中のユーザー名を表示
    document.getElementById("login-user-info")
            .textContent = `${loginUserName} さんがログイン中です`;
  }

  // -----------------------------------------------
  // 管理者権限制御
  // -----------------------------------------------
  // 新規ユーザー登録リンクの制御
  const linkNew = document.getElementById("link-new");
  if (linkNew) {
    linkNew.addEventListener("click", (e) => {
      if (!isAdmin) {
        e.preventDefault(); // デフォルトのリンク遷移を阻止
        alert("管理者のみ新規ユーザー登録が可能です。");
      }
    });
  }

  // 部署勤怠情報リンクの制御
  const linkDept = document.getElementById("link-dept");
  if (linkDept) {
    linkDept.addEventListener("click", (e) => {
      if (!isAdmin) {
        e.preventDefault();
        alert("管理者のみ部署別勤怠情報の閲覧が可能です。");
      }
    });
  }

  // -----------------------------------------------
  // 現在時刻表示機能
  // -----------------------------------------------
  /**
   * 現在の時刻をリアルタイムで表示する関数。
   * 1秒ごとに更新されます。
   */
  function updateClock() {
    const now = new Date(); // 今の日時を取得
    // 年、月、日、時、分を2桁表示にフォーマット
    const year = now.getFullYear();
    const month = String(now.getMonth() + 1).padStart(2, '0'); // 月は0から始まるため+1
    const day = String(now.getDate()).padStart(2, '0');
    const hours = String(now.getHours()).padStart(2, '0');
    const minutes = String(now.getMinutes()).padStart(2, '0');

    // フォーマットした時刻を画面に表示
    document.getElementById("currentDateTime").textContent = `${year}/${month}/${day} ${hours}:${minutes}`;
  }

  // 1秒ごとにupdateClock関数を実行
  setInterval(updateClock, 1000);
  // ページ読み込み時に一度実行
  updateClock();

  // -----------------------------------------------
  // カレンダー機能 (flatpickr ライブラリを使用)
  // -----------------------------------------------
  const displayDateInput = document.getElementById("displayDateInput");
  const displaySelectedDateSpan = document.getElementById("displaySelectedDate");
  const registerDateInput = document.getElementById("registerDateInput");
  const registerSelectedDateSpan = document.getElementById("registerSelectedDate");

  flatpickr("#calendar", {
    locale: "ja", // 日本語ロケールを設定
    inline: true, // 常にカレンダーを表示
    defaultDate: new Date(), // 初期表示は今日の日付
    onChange: function(selectedDates, dateStr) {
      // 選択された日付を検索結果表示フォームに設定
      displaySelectedDateSpan.textContent = dateStr;
      displayDateInput.value = dateStr;
      // 選択された日付を登録・更新フォームに設定
      registerSelectedDateSpan.textContent = dateStr;
      registerDateInput.value = dateStr;
    }
  });

  // -----------------------------------------------
  // メッセージ表示機能
  // -----------------------------------------------
  const messageArea = document.getElementById("messageArea"); // メッセージを表示するエリアの要素
  function showMessage(message, type = 'error') {
    messageArea.textContent = message; // メッセージを設定
    if (type === 'error') {
      messageArea.style.color = 'red';
    } else if (type === 'success') {
      messageArea.style.color = 'green';
    } else {
      messageArea.style.color = 'blue';
    }
    setTimeout(() => {
      messageArea.textContent = '';
    }, 5000);
  }

  // -----------------------------------------------
  // 勤怠区分（ステータス）のマッピング
  // -----------------------------------------------
  const ATTENDANCE_STATUS_MAP = {
    "A001": "出勤",
    "A002": "欠勤",
    "A003": "振出",
    "A004": "振休",
    "A005": "年休",
    "A006": "休日"
  };

  const REVERSE_ATTENDANCE_STATUS_MAP = {
    "出勤": "A001",
    "欠勤": "A002",
    "振出": "A003",
    "振休": "A004",
    "年休": "A005",
    "休日": "A006"
  };

  // -----------------------------------------------
  // 勤怠情報 検索機能
  // -----------------------------------------------
  const searchKintaiBtn = document.getElementById("searchKintaiBtn");
  const displayStatus      = document.getElementById("displayStatus");
  const displayStartHour   = document.getElementById("displayStartHour");
  const displayStartMin    = document.getElementById("displayStartMin");
  const displayEndHour     = document.getElementById("displayEndHour");
  const displayEndMin      = document.getElementById("displayEndMin");

  async function searchAndDisplayKintai() {
    // 省略せずに全体コードをここに展開してください...
    // 既存の検索処理ロジックが続きます。
  }

  function clearDisplayKintaiForm() {
    displayStatus.value = "出勤";
    displayStartHour.value = "";
    displayStartMin.value = "";
    displayEndHour.value = "";
    displayEndMin.value = "";
  }

  if (searchKintaiBtn) {
    searchKintaiBtn.addEventListener("click", searchAndDisplayKintai);
  }

  // -----------------------------------------------
  // コピー機能
  // -----------------------------------------------
  const copyToRegisterBtn = document.getElementById("copyToRegisterBtn");
  if (copyToRegisterBtn) {
    copyToRegisterBtn.addEventListener("click", () => {
      // コピー処理ロジック
    });
  }

  // -----------------------------------------------
  // 勤怠フォーム送信機能 (登録/更新)
  // -----------------------------------------------
  const kintaiRegisterForm = document.getElementById("kintaiRegisterForm");
  async function submitKintaiForm(e) {
    e.preventDefault();
    // 登録/更新処理ロジック
  }
  if (kintaiRegisterForm) {
    kintaiRegisterForm.addEventListener("submit", submitKintaiForm);
  }

}); // DOMContentLoaded 終了
