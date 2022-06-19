const headerDropdowns = document.querySelectorAll(".header-dropdown");

const navMenu = document.querySelector(".nav");
const burgerMenu = document.querySelector(".burger-menu");

burgerMenu.addEventListener("click", () => {
   console.log('click');
   burgerMenu.classList.toggle('open');
   navMenu.classList.toggle('open');
});

headerDropdowns.forEach(headerDropdown => {
   let headerDropdownMenu = headerDropdown.querySelector(".header-dropdown-menu");

   headerDropdown.addEventListener("click", () => {
      headerDropdownMenu.classList.toggle("hidden");
      let arrow = headerDropdown.querySelector(".arrow");
      arrow.classList.toggle("up");
   });
});
