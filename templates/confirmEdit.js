// ✅ 获取 URL 参数
const params = new URLSearchParams(location.search);
const date = params.get("date");
const status = params.get("status");
const start = params.get("startTime");
const end = params.get("endTime");

// ✅ 显示在确认页面上
document.getElementById("confirm-date").textContent = date;
document.getElementById("confirm-status").textContent = status;
document.getElementById("confirm-start").textContent = start;
document.getElementById("confirm-end").textContent = end;

// ✅ 设置隐藏表单字段（用于提交 POST）
const form = document.getElementById("submit-form");
form.date.value = date;
form.status.value = status;
form.startTime.value = start;
form.endTime.value = end;

// ❌ 不要加 e.preventDefault() —— 让浏览器发送真正的 POST 请求

