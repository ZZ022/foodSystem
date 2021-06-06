const searchParams = new URLSearchParams(new URL(location.href).search);
const uid = Number(searchParams.get('uid'));
const btnAddTag = $('#addTag');
const selectTag = "selectTag"
var isAddTag;
var isAddPos;
var postIdx = 0;
var imgSrc = [];
var imgFile = [];
const maxImgNum =3;

function getObjectURL(file) {
    var url = null ;
    if (window.createObjectURL!=undefined) { // basic
        url = window.createObjectURL(file) ;
    } else if (window.URL!=undefined) { // mozilla(firefox)
        url = window.URL.createObjectURL(file) ;
    } else if (window.webkitURL!=undefined) { // webkit or chrome
        url = window.webkitURL.createObjectURL(file) ;
    }
    return url ;
}

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

//显示标签
function showTag(){
    $.ajax(
        {
            url:"data/addTag",
            type:"post",
            data:{},
            success:function(res){
                console.log(res);
                var html = "<select id=\"{0}\">".format(selectTag);
                for(var i=0; i<res.length; i++){
                    html += "<option value=\"{0}\">{0}</option>".format(res[i]);
                }
                html += "</select>"
                console.log(html);
                btnAddTag.append(html);
            }
        }
    )
}

//向后台提交帖子的接口
function submitPost(userId,content, tag, lat, lng, medias){
    var formData = new FormData()
    formData.append("uid", userId);
    formData.append("content",content);
    formData.append("tag",tag);
    formData.append("lat",lat);
    formData.append("lng",lng);
    medias.forEach(element=>{formData.append('medias[]', element)})
    for (var [a, b] of formData.entries()) {
        console.log(a, b);
    }
    $.ajax({
        url:'data/submitPost',
        type: 'POST',
        data: formData,
        async: true,
        cache: false,
        contentType: false,
        processData: false,
        traditional:true,
        success:function (res){
            imgFile = [];
            imgSrc = [];
        },
        error: function (res){
            console.log(res);
        }
    })
}

// 搜索框，实现根据content搜索用户表-->美食tag表，并返回相应的帖子显示
function searchByUserOrTag(content) {
    $.ajax(
        {
            url:"data/search",
            type:"post",
            data:{"content":content},
            success:function(res){
                console.log(res);
                // 考虑怎样将帖子动态链接到页面中
            }
        }
    )
}

// 点赞操作
function likedAdd() {
    $.ajax({
        url:"data/likedAdd",
        type:"post",

    })

}


//向后台获取帖子信息，s为开始位置，n为请求帖子数
function fetchPost(s,n){
    var posts
    $.ajax({
        url:"/data/fetchPost",
        type:"post",
        data:{"start":s, "num":n},
        success:function(res){
            $('#postArea').append(
                showPostHtml(res.posts[0])
            )
        }
    })
}

//返回显示帖子的html
function showPostHtml(post){
    var htmlTemplate = "<div class=\"box shadow-sm border rounded bg-white mb-3 osahan-post\">\n" +
        "                     <div class=\"p-3 d-flex align-items-center border-bottom osahan-post-header\">\n" +
        "                        <div class=\"dropdown-list-image mr-3\">\n" +
        "                           <img class=\"rounded-circle\" src=\"img/p5.png\" alt=\"\">\n" +
        "                           <div class=\"status-indicator bg-success\"></div>\n" +
        "                        </div>\n" +
        "                        <div class=\"font-weight-bold\">\n" +
        "                           <div class=\"text-truncate\">{0}</div>\n" +
        "                           <div class=\"small text-gray-500\">立志吃遍全国面食</div>\n" +
        "                        </div>\n" +
        "                        <span class=\"ml-auto small\">{1}</span>\n" +
        "                     </div>\n" +
        "                     <div class=\"p-3 border-bottom osahan-post-body\">\n" +
        "                        <p class=\"mb-0\">{2}\n" +
        "                        <a href=\"#\">#{3}</a></p>\n" +
        "                     </div>\n" +
        "{4}\n" +
        "                     <div class=\"p-3 border-bottom osahan-post-footer\">\n" +
        "                        <a href=\"#\" class=\"mr-3 text-secondary\"><i class=\"feather-heart text-danger\"></i> 16</a>\n" +
        "                        <a href=\"#\" class=\"mr-3 text-secondary\"><i class=\"feather-message-square\"></i>0</a>\n" +
        "                        <a href=\"#\" class=\"mr-3 text-secondary\"><i class=\"feather-share-2\"></i>2</a>\n" +
        "                     </div>"
    console.log(htmlTemplate.format(post.username, post.date, post.content, post.tag,  showMediaHtml(post.medias)));
    return htmlTemplate.format(post.username, post.date, post.content, post.tag,  showMediaHtml(post.medias));
}

//显示媒体的html
function showMediaHtml(medias){
    console.log(medias)
    var html = "<div>{0}</div>";
    var medianHtml = ""
    var photoTemplate = "<img src='{0}' width='100px' height='100px'>";
    var videoTemplate = "<video src='{0}' width='100px' height='100px' autoplay='autoplay'>"
    for(var i=0;i<medias.length;i++){
        let media = medias[i];
        if(media.photo){
            medianHtml += photoTemplate.format(media.path);
            medianHtml += '\n';
        }
        else {
            medianHtml += videoTemplate.format(media.path);
            medianHtml += '\n';
        }
    }
    return html.format(medianHtml)

}



// 搜索框，实现根据content搜索用户表-->美食tag表，并返回相应的帖子显示
function searchByUserOrTag(content) {
    $.ajax(
        {
            url:"data/search",
            type:"post",
            data:{"content":content},
            success:function(res){
                console.log(res);
                // 考虑怎样将帖子动态链接到页面中
            }
        }
    )
}

// 实现将该点赞数据存入数据库的动作
function saveLikedToDB(postId,userId){
    $.ajax({
        url:"api/likedAdd",
        type:"post",
        data:{
            "postId":postId,
            "userId":userId
        },
        success:function (res) {
            console.log("点赞数据存入数据库"+res);
            return res;
        }
    })
}

// 实现根据postid获取更新后的点赞数
function updatedCount(postId){
    $.ajax({
        url:"api/likedUpdated",
        type:"post",
        data:{
            "postId":postId
        },
        success:function (res) {
            console.log("获取的更新值"+res);
            return res;
        }
    })
}

//-------------------------主程序区-------------------
window.onbeforeunload = logout();
login();
showTag();
$('#btnSendPost').click(function (){
    let tag = $('#'+selectTag).val();
    let lat = 40;
    let lng = 35.5;
    let content = $('#sendArea').val();
    submitPost(uid, content, tag, lat, lng, imgFile)
})
$('#iptAddImage').on('change', function(){
    var fileList = this.files;
    console.log(fileList)
    for(var i = 0; i < fileList.length; i++) {
        if(imgSrc.length==maxImgNum){
            alert("最多添加{0}张图片".format(maxImgNum))
            break;
        }
        var imgSrcI = getObjectURL(fileList[i]);
        imgSrc.push(imgSrcI);
        imgFile.push(fileList[i]);
    }
})
fetchPost(0,1);

// 搜索按钮
$('#search').click(function () {
    var content = document.getElementById("search_input").value;
    // 用户优先
    postsRel = searchByUserOrTag(content);

})

//点赞,获取postId，userId和目前postId的点赞个数
$('#like').click(function () {
    // var likeNums = document.getElementById("like").innerText;
    // 模拟从前端元素获取到的用户和帖子编号
    var postId = 1;
    var userId = uid;
    // 实现将该点赞数据存入数据库的动作
    var flag = saveLikedToDB(postId,userId);
    if(flag !== false){
        // 实现根据postid获取更新后的点赞数
        var numUpdated = updatedCount(postId);
    }
    else {
        alert("服务器异常，点赞失败！");
    }
    document.getElementById("like").innerText = numUpdated.toString();

})