const filters = document.querySelectorAll(".filter");

filters.forEach(filter => {
    let filterControl = filter.querySelector(".filter-control");

    filterControl.addEventListener("click", () => {
        toggleFilterMenu(filter);

        let arrow = filterControl.querySelector(".arrow");
        arrow.classList.toggle("up");
    })
});

function toggleFilterMenu(filter){
    let filterMenu = filter.querySelector(".filter-menu");
    filterMenu.classList.toggle("hidden");
}