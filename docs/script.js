async function compressFile() {
    var formData = new FormData(document.getElementById("compress-form"));

    var xhr = new XMLHttpRequest();
    xhr.responseType = 'blob';

    xhr.onreadystatechange = function () {
        if (this.readyState == 4) {
            console.log(this.response);
            var blob = this.response;
            var contentDispo = this.getResponseHeader('Content-Disposition');
            var fileName = contentDispo.match(/filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/)[1].substring(1).slice(0,-1);
            saveBlob(blob, fileName);
            saveBlob(new Blob([this.getResponseHeader('key')], {type: 'application/json'}), "key.txt");
        }
    }

    xhr.open("POST", "http://52.207.227.220:8080/process/compress");
    xhr.send(formData);
}

async function decompressFile() {
    var formData = new FormData(document.getElementById("decompress-form"));

    var xhr = new XMLHttpRequest();
    xhr.responseType = 'blob';

    xhr.onreadystatechange = function () {
        if (this.readyState == 4) {
            console.log(this.response);
            var blob = this.response;
            var contentDispo = this.getResponseHeader('Content-Disposition');
            var fileName = contentDispo.match(/filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/)[1].substring(1).slice(0,-1);
            saveBlob(blob, fileName);
        }
    }

    xhr.open("POST", "http://52.207.227.220:8080/process/decompress");
    xhr.send(formData);
}

function saveBlob(blob, fileName) {
    var a = document.createElement('a');
    a.href = window.URL.createObjectURL(blob);
    a.download = fileName;
    a.dispatchEvent(new MouseEvent('click'));
}
