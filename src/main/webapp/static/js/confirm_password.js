const form = document.querySelector("form");
const passwordInput = document.querySelector("#password");
const repeatPasswordInput = document.querySelector("#repeat-password");
const repeatPasswordErrorMessage = document.querySelector(".password-message");

repeatPasswordInput.addEventListener("keyup", () => {
    if(repeatPasswordInput.value !== passwordInput.value){
        repeatPasswordInput.style.borderColor = "red";
    } else {
        repeatPasswordInput.style.borderColor = "green";
        repeatPasswordErrorMessage.classList.add("hidden");
    }
});

form.onsubmit = function(event) {
    event.preventDefault();

    if (passwordInput.value !== repeatPasswordInput.value) {
        repeatPasswordErrorMessage.classList.remove("hidden");
    } else {
        form.submit();
    }
}