const statusFilters = document.querySelectorAll(".status-filter");
const courses = document.querySelectorAll(".course");

statusFilters.forEach(statusFilter => {
    statusFilter.addEventListener("click", () => {
        unselectStatusFilter();
        statusFilter.classList.add("selected");

        let status = statusFilter.getAttribute("data-status");
        filterByStatus(status);
    });
});

function unselectStatusFilter(){
    statusFilters.forEach(statusFilters => {
        statusFilters.classList.remove("selected");
    });
}

function filterByStatus(status){
    if(status == ""){
        courses.forEach(course => {
            course.classList.remove("hidden");
        });
    } else {
        courses.forEach(course => {
            let courseStatus = course.getAttribute("data-status");
            if(courseStatus == status){
                course.classList.remove("hidden");
            } else{
                course.classList.add("hidden");
            }
        });
    }
}
