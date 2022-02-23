const headerDropdowns = document.querySelectorAll(".header-dropdown");

headerDropdowns.forEach(headerDropdown => {
   let headerDropdownMenu = headerDropdown.querySelector(".header-dropdown-menu");

   headerDropdown.addEventListener("click", () => {
      headerDropdownMenu.classList.toggle("hidden");
      let arrow = headerDropdown.querySelector(".arrow");
      arrow.classList.toggle("up");
   });
});
