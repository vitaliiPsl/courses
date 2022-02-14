const dropdownElements = document.querySelectorAll(".dropdown-element");
dropdownElements.forEach(dropdownElement =>{
    let dropdownControl = dropdownElement.querySelector(".dropdown-control");

    dropdownControl.addEventListener("click", () => {
        toggleDropdown(dropdownElement);
    });
});

function toggleDropdown(dropdownElement) {
    let dropdownMenu = dropdownElement.querySelector(".dropdown-menu");
    let arrow = dropdownElement.querySelector(".arrow");

    if (dropdownMenu != null) {
        dropdownMenu.classList.toggle("hidden");
    }

    arrow.classList.toggle("up");
}