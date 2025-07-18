function updateDateTime() {
  const now = new Date();
  const year = now.getFullYear();
  const month = String(now.getMonth() + 1).padStart(2, '0');
  const day = String(now.getDate()).padStart(2, '0');
  const hours = String(now.getHours()).padStart(2, '0');
  const minutes = String(now.getMinutes()).padStart(2, '0');

  document.getElementById('date').textContent = `${year}/${month}/${day}`;
  document.getElementById('time').textContent = `${hours}:${minutes}`;
}

updateDateTime();
setInterval(updateDateTime, 60000);
