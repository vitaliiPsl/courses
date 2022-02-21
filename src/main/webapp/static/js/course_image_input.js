const input = document.querySelector('#image-input');
const label = document.querySelector('#image-input-label');

function changeLabelText(){
    let labelVal = label.innerHTML;

    input.addEventListener('change', function(e){
        let fileName = e.target.value;
        if(fileName)
            label.innerHTML = fileName;
        else
            label.innerHTML = labelVal;
    });
}

changeLabelText();