
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
            if(res.status == 400) {
                alert("회원가입 입력 정보를 다시 확인해주십시오.")

                if(res.data.hasOwnProperty('valid_username')){
                    $('#valid_username').text(res.data.valid_username);
                    $('#valid_username').css('color', 'red');
                }
                else $('#valid_username').text('');

                if(res.data.hasOwnProperty('valid_password')){
                    $('#valid_password').text(res.data.valid_password);
                    $('#valid_password').css('color', 'red');
                }
                else $('#valid_password').text('');

                if(res.data.hasOwnProperty('valid_email')){
                    $('#valid_email').text(res.data.valid_email);
                    $('#valid_email').css('color', 'red');
                }
                else $('#valid_email').text('');
            } else {
                alert("회원가입이 완료되었습니다.");
                location.href="/";
            }
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



