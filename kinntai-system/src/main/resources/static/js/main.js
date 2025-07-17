function setStatusAndSubmit(status) {
    const input = document.getElementById('statusInput');
    input.value = status;
    input.form.submit();
}
