const filters = document.querySelectorAll(".filter");

filters.forEach(filter => {
    addFilterOptionSelectClickListener(filter);
});

function addFilterOptionSelectClickListener(filter) {
    // let url = filter.getAttribute("data-url");
    let options = filter.querySelectorAll(".option");

    options.forEach(option => {
        let optionForm = option.querySelector("form");
        option.addEventListener("click", () => {

            let optionInput = option.querySelector("input");
            optionInput.setAttribute("checked", "true");

            optionForm.submit();
        });

        // let optionInput = option.querySelector("input");
        // let filterKey = optionInput.getAttribute("name");
        // let value = optionInput.getAttribute("value");
        // let data = {"filter" : filterKey, "filter_option" : value};
        //
        // option.addEventListener("click", () => {
        //     fetch(url, {
        //         method: 'POST',
        //         mode: 'cors',
        //         cache: 'no-cache',
        //         credentials: 'same-origin',
        //         headers: {
        //             'Content-Type': 'application/json'
        //         },
        //         redirect: 'follow',
        //         referrerPolicy: 'no-referrer',
        //         body: JSON.stringify(data)
        //     }).then(r => console.log(r));
        // });
    });
}
