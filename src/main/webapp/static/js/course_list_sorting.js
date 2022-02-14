const sortingElements = document.querySelectorAll(".sort");

sortingElements.forEach(sorting => {
    addSortingOptionSelectClickListener(sorting);
});

function addSortingOptionSelectClickListener(sorting) {
    let options = sorting.querySelectorAll(".option");

    options.forEach(option => {
        let optionForm = option.querySelector("form");
        option.addEventListener("click", () => {
            let optionInput = option.querySelector("input");
            unselectOptions(options);
            optionInput.setAttribute("checked", "true");

            optionForm.submit();
        });
    });
}

function unselectOptions(options) {
    options.forEach(
        option => {
            option.classList.remove("selected");
            let checkbox = option.querySelector("input");
            checkbox.removeAttribute("checked");
        }
    );
}