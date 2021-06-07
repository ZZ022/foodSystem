const searchParams = new URLSearchParams(new URL(location.href).search);
const uid = Number(searchParams.get('uid'));
const vid = Number(searchParams.get('vid'));
var username;
var postNum = 100;
var postSet = [];
var isPostEnd;
var maxPostNum=3;
var postIdx = 0;
var moreId = 0;
//----------------------函数区------------------
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

//登录
function  login(){
    $.ajax({
        url:"/data/login",
        type:"post",
        data:{"uid":uid},
    })
}

//登出
function logout(){
    $.ajax({
        url:"/data/logout",
        type:"post",
        data:{"uid":uid},
    })
};

function Like(postId){
    var flag = saveLikedToDB(postId,uid);
    var numUpdated;
    if(flag != false){
        // 实现根据postid获取更新后的点赞数
        numUpdated = updatedCount(postId);
        console.log(numUpdated);
        document.getElementById("like"+postId).innerText = numUpdated.toString()+"已赞";
    }
    else {
        alert("您已点赞！");
    }
}

function saveLikedToDB(postId,userId){
    console.log('saving');
    var flag=false;
    $.ajax({
        url:"api/likedAdd",
        type:"post",
        data:{
            "postId":postId,
            "userId":userId
        },
        async:false,
        success:function (res) {
            console.log("点赞数据存入数据库"+res);
            flag = true;
        }
    })
    return flag;
}


function viewMore(){
    moreId += 1;
    if(postIdx<postNum){
        for(var i=0;i<maxPostNum;i++){
            $('#{0}'.format('postArea')).append(showPostHtml(postSet[i+postIdx]));
        }
        postIdx += maxPostNum;
        if(postIdx<postNum){
            $('#postArea').append(
                '<button id="btnMorePost{0}" onclick="viewMore()">浏览更多</button>'.format(moreId)
            );
        }
    }
    $('#btnMorePost{0}'.format(moreId-1)).remove();
}

function isLiked(postid){
    var res = "";
    $.ajax(({
        url:"api/isLiked",
        type:"post",
        data:{"uid":uid, "postid":postid},
        async:false,
        success:function(data){
            if(data){
                res+="已赞";
            }
        }
    }))
    return res;
}

// 实现根据postid获取更新后的点赞数
function updatedCount(postId){
    var num;
    $.ajax({
        url:"api/likedUpdated",
        type:"post",
        data:{
            "postId":postId
        },
        async:false,
        success:function (res) {
            console.log("获取的更新值"+res);
            num = res;
        }
    })
    return num;
}

//返回显示帖子的html
function showPostHtml(post){
    console.log(post);
    var htmlTemplate = "<div class=\"box shadow-sm border rounded bg-white mb-3 osahan-post\" id='post{5}'>\n" +
        "                     <div class=\"p-3 d-flex align-items-center border-bottom osahan-post-header\">\n" +
        "                        <div class=\"dropdown-list-image mr-3\">\n" +
        "                           <img class=\"rounded-circle\" src=\"img/p5.png\" alt=\"\">\n" +
        "                           <div class=\"status-indicator bg-success\"></div>\n" +
        "                        </div>\n" +
        "                        <div class=\"font-weight-bold\">\n" +
        "                           <div class=\"text-truncate\">{0}</div>\n" +
        "                           <div class=\"small text-gray-500\">{7}</div>\n" +
        "                        </div>\n" +
        "                        <span class=\"ml-auto small\">{1}</span>\n" +
        "                     </div>\n" +
        "                     <div class=\"p-3 border-bottom osahan-post-body\">\n" +
        "                        <p class=\"mb-0\">{2}\n" +
        "                        <a href=\"#\">#{3}</a></p>\n" +
        "                     </div>\n" +
        "{4}\n" +
        "                     <div class=\"p-3 border-bottom osahan-post-footer\">\n" +
        "                        <a  class=\"mr-3 text-secondary\"><i class=\"feather-heart text-danger\" onclick=\"Like({5})\" id='like{5}'>{6}</i></a>\n" +
        "                        <a  class=\"mr-3 text-secondary\"><i class=\"feather-message-square\"></i>0</a>\n" +
        "                        <a  class=\"mr-3 text-secondary\"><i class=\"feather-share-2\"></i>2</a>\n" +
        "                     </div>"
    // console.log(htmlTemplate.format(post.username, post.date, post.content, post.tag,  showMediaHtml(post.medias), post.id));
    return htmlTemplate.format(post.username, post.date, post.content, post.tag,  showMediaHtml(post.medias), post.id,
        updatedCount(post.id).toString()+isLiked(post.id),post.sign);
}

//显示媒体的html
function showMediaHtml(medias){
    console.log(medias)
    var html = "<div>{0}</div>";
    var medianHtml = ""
    var photoTemplate = "<img src='{0}' width='100px' height='100px'>";
    var videoTemplate = "<video src='{0}' width='100px' height='100px' autoplay='autoplay'></video>"
    for(var i=0;i<medias.length;i++){
        let media = medias[i];
        if(media.photo){
            medianHtml += photoTemplate.format(media.path);
        }
        else {
            medianHtml += videoTemplate.format(media.path);
        }
    }
    return html.format(medianHtml)

}

function searchByUserOrTag(content) {
    $.ajax(
        {
            url:"data/search",
            type:"post",
            data:{"content":content},
            async:false,
            success:function(res){
                console.log(res);
                postSet = res.posts;
                // 考虑怎样将帖子动态链接到页面中
            }
        }
    )
}
function renderPost(){
    if(postIdx<postNum){
        for(var i=0;i<maxPostNum;i++){
            $('#{0}'.format('postArea')).append(showPostHtml(postSet[i+postIdx]));
        }
        postIdx += maxPostNum;
        if(postIdx<postNum){
            $('#postArea').append(
                '<button id="btnMorePost{0}" onclick="viewMore()">浏览更多</button>'.format(moreId)
            );
        }
    }
}

//---------------------------------主程序区-------------------------------
window.onbeforeunload = logout();
login();
if(uid==vid){
    $('#signArea').html(
        '<input id="textSign2">\n' +
        '</input><button id="btnSaveSign">保存</button>'
    );
    $('#btnSaveSign').click(function (){
        $.ajax({
            url:'data/saveSign',
            data:{"uid":uid, "sign":$('#textSign2').val()},
            success:function (res){
                location.reload();
            }
        })

    })
}
$.ajax({
    url:'data/getLike',
    data:{"uid":uid},
    success:function (res) {
        $('#textGetLike').html(res);
    }
})
$.ajax({
    url:'data/giveLike',
    data:{"uid":uid},
    success:function (res) {
        console.log(res);
        $('#textLike').html(res.toString())
    }
})
$.ajax({
    url:'data/sign',
    data:{"uid":uid},
    success:function (res) {
        $('#textSign').html(res);
        $('#textSign1').html(res);
        $('#textSign2').html(res);
        $('#textSign2').val(res);
    }
})
$.ajax({
    url:'data/username',
    data:{"uid":uid},
    success:function (res) {
        $('#textUser').html(res);
        $('#textUser1').html(res);
        searchByUserOrTag(res);
        renderPost();
    }
})
