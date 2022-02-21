const form = document.querySelector("form");
const passwordInput = document.querySelector("#password");
const repeatPasswordInput = document.querySelector("#repeat-password");

const submitBtn = document.querySelector(".form-submit");

submitBtn.addEventListener("click", () => {
    if(passwordInput !== repeatPasswordInput){
        alert("Password don't match");
    } else {
        form.submit();
    }
});