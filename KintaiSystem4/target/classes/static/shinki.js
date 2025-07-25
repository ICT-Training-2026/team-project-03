let currentRow = 4;

document.getElementById("addRow").addEventListener("click", () => {
  currentRow++;
  const tbody = document.querySelector("#entryTable tbody");
  const tr = document.createElement("tr");
  tr.innerHTML = `
  <td>${currentRow}</td>
      <td><input name="id${currentRow}"></td>
      <td><input name="name${currentRow}"></td>
      <td><input type="password" name="pass${currentRow}"></td>
      <td><input type="email" name="mail${currentRow}"></td>
      <td><input name="dept${currentRow}"></td>
      <td><input type="date" name="date${currentRow}"></td>
  	<td><input name="admin${currentRow}"></td>
  `;
  tbody.appendChild(tr);
});

document.getElementById("removeRow").addEventListener("click", () => {
  if (currentRow > 1) {
    const tbody = document.querySelector("#entryTable tbody");
    tbody.removeChild(tbody.lastElementChild);
    currentRow--;
  }
});
