const filters = document.querySelectorAll(".filter");

const sortingOptions = document.querySelectorAll(".sort-option");
const orderOptions = document.querySelectorAll(".order-option");

addSortingClickListeners(sortingOptions);
addSortingClickListeners(orderOptions);

function addSortingClickListeners(options) {
    options.forEach(option => {
            option.addEventListener("click", () => {
                unselectSortingOptions(options);

                option.classList.add("selected")
                let checkbox = option.querySelector("input");
                checkbox.toggleAttribute("checked");
            });
        });
}

function unselectSortingOptions(options) {
    options.forEach(
            option => {
                option.classList.remove("selected");
                let checkbox = option.querySelector("input");
                checkbox.removeAttribute("checked");
            }
        );
}

filters.forEach(filter => {
    let filterControl = filter.querySelector(".filter-control");

    filterControl.addEventListener("click", () => {
        toggleFilterMenu(filter);

        let arrow = filterControl.querySelector(".arrow");
        arrow.classList.toggle("up");
    });
});

function toggleFilterMenu(filter) {
    let filterMenu = filter.querySelector(".filter-menu");
    if (filterMenu != null) {
        filterMenu.classList.toggle("hidden");
    }

    let sortMenu = filter.querySelector(".sort-menu");
    if (sortMenu != null) {
        sortMenu.classList.toggle("hidden");
    }
}