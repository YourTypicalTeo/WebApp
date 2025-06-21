document.addEventListener("DOMContentLoaded", () => {
  const form = document.getElementById("changePassForm");
  const clientError = document.getElementById("clientError");
  const serverMessage = document.getElementById("serverMessage");

  form.addEventListener("submit", function (e) {
    clientError.textContent = "";

    const oldPass = document.getElementById("oldpass").value.trim();
    const newPass1 = document.getElementById("newpass1").value.trim();
    const newPass2 = document.getElementById("newpass2").value.trim();
    const pattern = /^[A-Za-z0-9]{6,}$/;

    if (!oldPass || !newPass1 || !newPass2) {
      clientError.textContent = "All fields are required.";
      e.preventDefault();
      return;
    }
    if (newPass1 !== newPass2) {
      clientError.textContent = "New passwords do not match.";
      e.preventDefault();
      return;
    }
    if (newPass1 === oldPass) {
      clientError.textContent = "New password must be different from the old password.";
      e.preventDefault();
      return;
    }
    if (!pattern.test(newPass1)) {
      clientError.textContent = "Password must be at least 6 characters and contain only letters and digits.";
      e.preventDefault();
      return;
    }
  });

  // Display server message from URL parameters
  const params = new URLSearchParams(window.location.search);
  const message = params.get("message");
  const type = params.get("type");

  if (message) {
    serverMessage.textContent = decodeURIComponent(message);
    serverMessage.style.color = type === "success" ? "green" : "red";
  }
});
