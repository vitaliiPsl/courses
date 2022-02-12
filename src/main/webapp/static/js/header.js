const accountDropdown = document.querySelector(".account-dropdown");
const accountDropdownMenu = document.querySelector(".account-dropdown-menu");

accountDropdown.addEventListener("click", () => {
   accountDropdownMenu.classList.toggle("hidden");
   let arrow = accountDropdown.querySelector(".arrow");
   arrow.classList.toggle("up");
});
