
let index = {
    init:function () {
        $("#btn-join").on("click",() =>{
            this.save();
        });
        $("#btn-update").on("click",() =>{
            this.update();
        });

    },
    save: function () {
        // alert('호출');

        // html 에 있는 id 들 값 바인드
        let data = {
            username: $("#username").val(),
            password: $("#password").val(),
            email: $("#email").val()
        };

        // console.log(data);
        // ajax 통신을 이용해 3개의 데이터를 json 으로 변경하여 insert 요청
        // ajax 호출 시 default 가 비동기 호출
        $.ajax({
            type: "POST",
            url: "/auth/joinProc",
            data: JSON.stringify(data), // javascript 의 data 를 JSON 로 변환하여 JAVA 로 전달
            contentType: "application/json; charset=utf-8", // body 데이터가 어떤 type 인지
            dataType: "json" // 응답된 데이터가 json 이라면 javascript 오브젝트로 받음
        }).done(function (res) {
            console.log(res);
            alert("successful!");
            location.href = "/";
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },
    update: function () {

        let data = {
            username : $("#username").val(),
            password: $("#password").val(),
            id: $("#id").val(),
            passwordConfirm: $("#passwordConfirm").val(),
            email: $("#email").val()
        };

        $.ajax({
            type: "PUT",
            url: "/user",
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            dataType: "json"
        }).done(function (res) {
            console.log(res);
            alert("회원수정이 완료되었습니다");
            location.href = "/";
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    }


};

index.init()



