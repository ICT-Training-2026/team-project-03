// main.js - 勤怠登録画面のフロントエンドロジック

// ページ全体のコンテンツが読み込まれた後に実行されるように設定
document.addEventListener("DOMContentLoaded", () => {

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
  const dateInput = document.getElementById("dateInput");
  const selectedDateSpan = document.getElementById("selectedDate");

  /**
   * カレンダーの初期設定と日付選択時の処理。
   * flatpickr ライブラリを使ってカレンダーを表示し、
   * 日付が選択されたら、その日付をフォームに設定します。
   */
  flatpickr("#calendar", {
    locale: "ja", // 日本語ロケールを設定
    inline: true, // 常にカレンダーを表示
    defaultDate: new Date(), // 初期表示は今日の日付
    // 日付が選択されたときの処理
    onChange: function(selectedDates, dateStr) {
      // 選択された日付を画面に表示する要素と、フォームの隠しフィールドに設定
      selectedDateSpan.textContent = dateStr;
      dateInput.value = dateStr;
    }
  });

  // -----------------------------------------------
  // メッセージ表示機能
  // -----------------------------------------------
  const messageArea = document.getElementById("messageArea"); // メッセージを表示するエリアの要素

  /**
   * 画面上にメッセージを表示する関数。
   * @param {string} message - 表示するメッセージ内容。
   * @param {string} type - メッセージのタイプ ('error', 'success', 'info')。
   */
  function showMessage(message, type = 'error') {
    console.log(`[showMessage] Type: ${type}, Message: ${message}`); // デバッグ用ログ
    messageArea.textContent = message; // メッセージを設定
    // メッセージタイプに応じて文字色を変更
    if (type === 'error') {
      messageArea.style.color = 'red';
    } else if (type === 'success') {
      messageArea.style.color = 'green';
    } else {
      messageArea.style.color = 'blue'; // infoタイプなど、その他の色
    }
    // 5秒後にメッセージを自動的にクリア
    setTimeout(() => {
      messageArea.textContent = '';
    }, 5000);
  }

  // -----------------------------------------------
  // 勤怠区分（ステータス）のマッピング
  // -----------------------------------------------
  // バックエンドから来る勤怠区分IDと、画面に表示する名前の対応表
  const ATTENDANCE_STATUS_MAP = {
    "A001": "出勤",
    "A002": "欠勤",
    "A003": "振出",
    "A004": "振休",
    "A005": "年休",
    "A006": "休日"
  };

  // 画面の選択肢からバックエンドに送る勤怠区分IDに変換するための対応表
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

  /**
   * 勤怠情報をAPIから取得し、フォームに表示する関数。
   * 検索ボタンがクリックされたときに実行されます。
   */
  async function searchAndDisplayKintai() {
    console.log("[searchAndDisplayKintai] 関数が開始されました。"); // デバッグ用ログ

    // 選択されている日付とログイン中のユーザーIDを取得
    const selectedDate = dateInput.value;
    const currentUserId = sessionStorage.getItem("loginUserId");

    console.log(`[searchAndDisplayKintai] 選択日付: ${selectedDate}, ユーザーID: ${currentUserId}`); // デバッグ用ログ

    // 必須項目が入力されているかチェック
    if (!selectedDate) {
      showMessage("日付を選択してください。", 'error');
      return;
    }
    if (!currentUserId) {
      showMessage("ユーザー情報が取得できません。再度ログインしてください。", 'error');
      return;
    }

    try {
      // APIを呼び出して、選択された日付の勤怠情報を取得
      const apiUrl = `/api/kintai/user?userId=${encodeURIComponent(currentUserId)}&startDate=${encodeURIComponent(selectedDate)}&endDate=${encodeURIComponent(selectedDate)}`;
      console.log(`[searchAndDisplayKintai] APIリクエストURL: ${apiUrl}`); // デバッグ用ログ
      const response = await fetch(apiUrl);

      if (!response.ok) {
        throw new Error(`勤怠情報の取得に失敗しました。サーバーエラー: ${response.status}`);
      }
      const kintaiDataList = await response.json(); // JSON形式でレスポンスをパース
      console.log("[searchAndDisplayKintai] APIレスポンスデータ:", kintaiDataList); // デバッグ用ログ

      // 勤怠情報が存在するかどうかをチェック
      if (Array.isArray(kintaiDataList) && kintaiDataList.length > 0) {
        const kintaiRecord = kintaiDataList[0]; // その日の勤怠レコード（通常は1件）

        // 取得した勤怠情報をフォームの各フィールドにセット
        document.getElementById("status").value = ATTENDANCE_STATUS_MAP[kintaiRecord.attId] || "出勤";
        document.getElementById("startHour").value = kintaiRecord.inTimeH || "";
        document.getElementById("startMin").value = kintaiRecord.inTimeM || "";
        document.getElementById("endHour").value = kintaiRecord.outTimeH || "";
        document.getElementById("endMin").value = kintaiRecord.outTimeM || "";

        showMessage("勤怠情報を表示しました。", 'success');
      } else {
        // 勤怠情報がない場合、フォームの入力欄をクリア
        clearKintaiForm();
        showMessage("この日の勤怠情報はありません。", 'info');
      }
    } catch (error) {
      console.error("勤怠情報の取得中にエラーが発生しました:", error); // 開発者向けのエラーログ
      showMessage("勤怠情報の取得に失敗しました。エラー: " + error.message, 'error');
    }
  }

  /**
   * 勤怠入力フォームのフィールドをクリアする関数。
   */
  function clearKintaiForm() {
    document.getElementById("status").value = "出勤"; // デフォルト値
    document.getElementById("startHour").value = "";
    document.getElementById("startMin").value = "";
    document.getElementById("endHour").value = "";
    document.getElementById("endMin").value = "";
  }

  // 検索ボタンにクリックイベントリスナーを設定
  if (searchKintaiBtn) { // 要素が存在するかチェック
    searchKintaiBtn.addEventListener("click", searchAndDisplayKintai);
  }

  // -----------------------------------------------
  // 勤怠フォーム送信機能 (登録/更新)
  // -----------------------------------------------
  const kintaiForm = document.getElementById("kintaiForm");

  /**
   * 勤怠フォームのデータをバックエンドに送信し、登録または更新を行う関数。
   * 「登録」ボタンがクリックされたときに実行されます。
   */
  async function submitKintaiForm() {
    // フォームから各入力値を取得
    const selectedDate = dateInput.value;
    const statusDisplayName = document.getElementById("status").value;
    // 表示名から勤怠区分IDに変換
    const statusId = REVERSE_ATTENDANCE_STATUS_MAP[statusDisplayName] || "A001";

    const startHour = document.getElementById("startHour").value;
    const startMin = document.getElementById("startMin").value;
    const endHour = document.getElementById("endHour").value;
    const endMin = document.getElementById("endMin").value;
    const currentUserId = sessionStorage.getItem("loginUserId");

    // 必須項目が全て入力されているかチェック
    if (!selectedDate || !statusId || !currentUserId) {
      showMessage("日付、勤怠区分、ユーザーIDは必須です。", 'error');
      return;
    }
    // 時間の入力チェック (簡易的)
    if (!startHour || !startMin || !endHour || !endMin) {
      showMessage("出勤時間と退勤時間を全て入力してください。", 'error');
      return;
    }

    // 送信する勤怠データオブジェクトを作成
    const kintaiData = {
      userId: currentUserId,
      date: selectedDate,
      status: statusId, // IDを送信
      startTime: `${startHour}:${startMin}`,
      endTime: `${endHour}:${endMin}`
    };

    try {
      // バックエンドの勤怠登録/更新APIを呼び出し
      const response = await fetch('/api/kintai/register', {
        method: 'POST', // POSTメソッドでデータを送信
        headers: {
          'Content-Type': 'application/json' // JSON形式でデータを送ることを指定
        },
        body: JSON.stringify(kintaiData) // JavaScriptオブジェクトをJSON文字列に変換して送信
      });

      // HTTPレスポンスが成功かどうかを確認
      if (!response.ok) {
        throw new Error(`勤怠情報の保存に失敗しました。サーバーエラー: ${response.status}`);
      }

      const resultText = await response.text(); // サーバーからのテキストレスポンスを待つ (例: 'OK')
      if (resultText === "OK") {
        showMessage("勤怠情報が正常に保存されました。", 'success');
      } else {
        // 'OK' 以外のレスポンスがあった場合
        showMessage("勤怠情報の保存に失敗しました。予期せぬレスポンス: " + resultText, 'error');
      }
    } catch (error) {
      console.error("勤怠情報の保存中にエラーが発生しました:", error); // 開発者向けのエラーログ
      showMessage("勤怠情報の保存中にエラーが発生しました: " + error.message, 'error');
    }
  }

  // 勤怠フォームに送信イベントリスナーを設定
  if (kintaiForm) { // 要素が存在するかチェック
    kintaiForm.addEventListener("submit", submitKintaiForm);
  }

}); // DOMContentLoaded 終了
