"use strict";


// 사진 등록

const file_up = document.getElementById("file");
const filebox = document.getElementsByClassName("filebox");
const blob_img = document.getElementById("blob_img");
const previous_img = document.getElementById("previous_img");

if( file_up != null) {
    file_up.addEventListener("change", () => {
        let file = file_up.files[0];
        let ext = file.type;
        let result = (/^image\//gi).test(ext);

        if (result) {
            if(previous_img != null) {
                previous_img.style.display = "none";
                delete_pre_img.style.display="none";
            }
            filebox[0].firstElementChild.style.display = "none";
            blob_img.src = window.URL.createObjectURL(file);
            blob_img.style.width = "200px";
        } else {
            blob_img.src = "";
            filebox[0].firstElementChild.style.display = "block";
            filebox[0].firstElementChild.innerHTML = "다른 파일 선택";

            // 사진 이외의 파일이면 등록버튼 잠궈주세요
        }
    });
}


// 이전 사진 삭제
const delete_pre_img = document.getElementById("delete_pre_img");
if (delete_pre_img != null) {
    delete_pre_img.addEventListener("click", () => {
        previous_img.style.display="none";
        delete_pre_img.style.display="none";
    });
}

// 버튼

const save_btn = document.getElementById("btn-save");
const delete_btn = document.getElementById("btn-delete");
const update_btn = document.getElementById("btn-update");
const answer_btn = document.getElementById("btn-answer-save");
const answer_update_btn = document.getElementById("btn-answer-update");
const answer_edit_btn = document.getElementById("btn-answer-edit");

if (save_btn!=null)save_btn.addEventListener("click", save);
if (update_btn!=null)update_btn.addEventListener("click", update);
if (delete_btn!=null)delete_btn.addEventListener("click", deleteById);
if (answer_btn!=null)answer_btn.addEventListener("click", saveAnswer);
if (answer_update_btn!=null)answer_update_btn.addEventListener("click", updateAnswer);
if (answer_edit_btn!=null)answer_edit_btn.addEventListener("click", saveEditAnswer);


// 저장

function save() {

    let data = {
        title: $("#title").val(),
        content: $("#content_text").val()
    };

    // 사진 없을 때
    if (file_up.files[0] === undefined) {
        fetch("/api/inquiry/save", {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(data),
        })
            .then(() => {
                alert("등록되었습니다.")
                location.href = "/inquiry"
            })
            .catch(e => alert(e))
    } else {    // 사진 있을 때
        let form = document.getElementById("form");
        let formData = new FormData(form);

        formData.append('file', file_up.files[0]);
        formData.append("title", data.title);
        formData.append("content", data.content);

        fetch("/api/inquiry/saveFile", {
            method: 'POST',
            body: formData
        })
            .then(() => {
                alert("등록되었습니다.")
                location.href = "/inquiry"
            })
            .catch(e => alert(e + " 이미지"))
    }
}


// 수정
function update() {

    let id = $("#id").text();
    let data = {
        title: $("#title").val(),
        content: $("#content_text").val(),
        img : ""
    };

    // 사진을 지웠으면
    if (previous_img != null) {
        if(previous_img.style.display === "none") {
            data.img="delete";
        }
    }

    // 사진 없을 때
    if (file_up.files[0] === undefined) {
        fetch("/api/inquiry/update/" + id, {
            method: 'PUT',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(data),
        })
            .then(() => {
                alert("수정되었습니다.")
                location.href = "/inquiry"
            })
            .catch(e => alert(e))
    } else {    // 사진 있을 때
        let form = document.getElementById("form");
        let formData = new FormData(form);

        formData.append('file', file_up.files[0]);
        formData.append("title", data.title);
        formData.append("content", data.content);

        fetch("/api/inquiry/updateFile/" + id, {
            method: 'PUT',
            body: formData
        })
            .then(() => {
                alert("수정되었습니다.")
                location.href = "/inquiry"
            })
            .catch(e => alert(e + " 이미지"))
    }
}


// 삭제
function deleteById() {
    let id = $("#inquiryId").text();

    fetch("/api/inquiry/delete/" + id, {
        method: 'DELETE'
    })
        .then(() => {
            alert("삭제되었습니다.")
            location.href = "/inquiry"
        })
        .catch(e => alert(e))
}


// 답변
function saveAnswer() {
    let id = $("#inquiryId").text();
    let data = {
        answer : $("#answer-content").val()
    }

    fetch("/api/inquiry/answer/" + id, {
        method: 'PUT',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(data),
    })
        .then(() => {
            alert("등록되었습니다.")
            location.reload();
        })
        .catch(e => alert(e))
}

// 답변 수정 버튼
function updateAnswer() {
    const answer = document.getElementById("answer");
    const answer_edit = document.getElementById("answer-edit");
    answer_update_btn.style.display="none";
    answer_edit_btn.style.display="inline";
    answer.style.display="none";
    answer_edit.style.display="inline";
}

// 답변 수정
function saveEditAnswer() {
    let id = $("#inquiryId").text();
    let data = {
        answer : $("#answer-edit").val()
    }

    fetch("/api/inquiry/answer/" + id, {
        method: 'PUT',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(data),
    })
        .then(() => {
            alert("등록되었습니다.")
            location.reload();
        })
        .catch(e => alert(e))
}


// 필터
const filter = document.getElementById("filter_select");
if(filter != null) {
    filter.addEventListener("change", () => {
        location.href = "/inquiry/confirm/" + filter.value;
    })
}