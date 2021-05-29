$('#signUpSubmit').click(function(){
    let username = $('#username').val();
    let password = $('#password').val();
    $.ajax({
       url:"/register/submit",
       type: "post",
       data:{"username":username,"password":password},
       success:function(res){
          console.log(res);
             switch(res){
                case -1:
                   window.alert("该用户名已被注册");
                   break;
                default:
                   window.location.href = "index?uid=" + res;
                   break;
             }
       }
    }
    )
 }
 )