const coursesSearchInput = document.querySelector("#search-input");

const queryUrl = coursesSearchInput.getAttribute("data-request-url");
const courseUrl = coursesSearchInput.getAttribute("data-course-url");

const hintsBlock = document.querySelector(".hints-block");
let courses = [];

coursesSearchInput.addEventListener("keyup", displayHints);
coursesSearchInput.addEventListener("keydown", clearHints);

function displayHints() {
    clearHints();
    fetchData(this.value);
}

function clearHints() {
    courses = [];
    hintsBlock.innerHTML = '';
}

function fetchData(query) {
    let url = queryUrl + query;

    fetch(url)
        .then(response => response.json())
        .then(data => courses.push(...data))
        .then(() => {
            courses.forEach(course => {
                console.log(course);
                addHint(course.id, course.title, course.subject);
            });
        });
}

function addHint(id, title, subject) {
    let hint = document.createElement("div");
    hint.classList.add("hint");

    let link = document.createElement("a");
    link.setAttribute("href", courseUrl + id);
    hint.appendChild(link);

    let h3 = document.createElement("h3");
    h3.innerText = title;
    link.appendChild(h3);

    let subjectSpan = document.createElement("span");
    subjectSpan.innerText = subject;
    link.appendChild(subjectSpan);

    // hint.addEventListener("click", () => {
    //     window.location.href = courseUrl + id;
    // });

    hintsBlock.appendChild(hint);
}