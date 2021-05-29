String.prototype.format = function(args) {
    if (arguments.length > 0) {
        var result = this;
        if (arguments.length == 1 && typeof(args) == "object") {
        for (var key in args) {
            var reg = new RegExp("({" + key + "})", "g");
            result = result.replace(reg, args[key]);
        }
        } else {
        for (var i = 0; i < arguments.length; i++) {
            if (arguments[i] == undefined) {
            return "";
            } else {
            var reg = new RegExp("({[" + i + "]})", "g");
            result = result.replace(reg, arguments[i]);
            }
        }
        }
        return result;
    } else {
        return this;
    }
}


function approve(postId){

}

class PostArea{
    constructor(divPostArea){
        this.template =  " \
                        <div id = \"{0}Id\"> \
                            {1} \
                            <br> \
                            date:{2}&nbsp;userId:{3}\
                            <br>点赞数:{4}\
                            <button id=\"btn{0}\" onclick = approve({0})>点赞</button>\
                        </div>\
                        "
        this.Posts = [];
        this.html = "";
    }

    addPost(post){
        this.Posts.push(post);
    }

    render(){
        for(var i=0;i<this.Posts.length;i++) {
            this.html += this.template.format(i, this.Posts[i].content, this.Posts[i].date, this.Posts[i].userId, this.Posts[i].likedInfos.length);
        }
        return this.html;
    }

}

var searchParams = new URLSearchParams(new URL(location.href).search);
const uid = Number(searchParams.get('uid'));
const divPostArea = $("#postArea");
$.ajax({
    url:"/data/login",
    type:"post",
    data:{"uid":uid},
    success:function(res){
    }
});

window.onbeforeunload =  function (){
    $.ajax({
        url:"/data/logout",
        type:"post",
        data:{"uid":uid},
        success:function(res){
        }
    })
};

$.ajax(
    {
        url:"/post/surf",
        type:"post",
        data:{},
        success:surf
    }
)

function approve(postId){
    $.ajax(
        {
            url:'food/liked',
            type:"post",
            data:{"postId":postId, "uid": uid},
            success: function (res){
                console.log("success");
            }
        }
    )
}

function getLikeNum(postId){
    var num;
    $.ajax(
        {
            url:'post/LikeNum',
            data:{"postId":postId},
            success: function(res){
                num = res;
            }
        }
    )
    return num;
}

function surf(res){
    postArea = new PostArea(divPostArea);
    for(var i=0;i<res.length;i++){
        postArea.addPost(res[i]);
    }
    divPostArea.append(postArea.render());
}