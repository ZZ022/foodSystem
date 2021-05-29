$('#login').click(function(){
    let username = $('#username').val();
    let password = $('#password').val();
    $.ajax({
        url:'login/submit',
        type:'post',
        data:{"username":username,"password":password},
        success:function(res){
            console.log(res);
            switch (res){
                case "password":
                    alert("密码错误");
                    break;
                case "login":
                    alert("用户已登录");
                    break;
                case "userName":
                    alert("用户名不存在");
                    break;
                default:
                    location.href = "index?"+"uid=" + Number(res);
                    break;
            }
        }
    })
})

