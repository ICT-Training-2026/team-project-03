// flatpickr 設定（1900〜2100年）
flatpickr("#calendar", {
  locale: "ja",
  dateFormat: "Y-m-d",
  inline: true,
  minDate: "1900-01-01",
  maxDate: "2100-12-31",
  defaultDate: "today",
  onChange: function (selectedDates, dateStr) {
    document.getElementById("selectedDate").textContent = dateStr;
    document.getElementById("dateInput").value = dateStr;
    loadKintaiData(dateStr);
  }
});

const startInput = document.getElementById("start");
const endInput = document.getElementById("end");
const statusSelect = document.getElementById("status");

function loadKintaiData(dateStr) {
  // ★ デモ：本来はここで fetch API でDBから読み込む
  if (dateStr === "2025-07-17") {
    startInput.value = 9;
    endInput.value = 17;
    statusSelect.value = "出勤";
  } else {
    startInput.value = "";
    endInput.value = "";
    statusSelect.value = "出勤";
  }
}


