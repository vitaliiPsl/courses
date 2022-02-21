const profileImage = document.querySelector("#avatar-image");
const profileImageInput = document.querySelector("#avatar-input");

profileImage.addEventListener("click", () => {
    profileImageInput.click();
});

function previewProfileImage(uploader) {
    console.log("here");
    if (uploader.files && uploader.files[0]) {
        let imageFile = uploader.files[0];
        let reader = new FileReader();

        reader.onload = function (e) {
            profileImage.setAttribute('src', e.target.result);
        }
        reader.readAsDataURL(imageFile);
    }
}

profileImageInput.addEventListener("change", () => {
    previewProfileImage(profileImageInput);
});
