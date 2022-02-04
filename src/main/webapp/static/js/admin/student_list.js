const studentsSearchInput = document.querySelector("#search-input");
const studentList = document.querySelectorAll(".student-row");
const students = [...studentList];

studentsSearchInput.addEventListener("change", displayStudentsMatches);
studentsSearchInput.addEventListener("keyup", displayStudentsMatches);

function findStudentsMatches(wordToMatch){
    return students.filter(student => {
        const regex = new RegExp(wordToMatch, 'i');

        const firstName = student.querySelector(".first-name").innerText;
        const lastName = student.querySelector(".last-name").innerText;
        const email = student.querySelector(".email").innerText;
        return !(firstName.match(regex) || lastName.match(regex) || email.match(regex));
    });
}

function displayStudentsMatches(){
    const matches = findStudentsMatches(this.value);
    students.forEach(element => element.classList.remove('hidden'));
    matches.forEach(element => element.classList.add("hidden"));
}