const searchParams = new URLSearchParams(new URL(location.href).search);
const uid = Number(searchParams.get('uid'));
console.log(uid);
if(uid==0){
    location.href="login";
}
const btnAddTag = $('#addTag');
const selectTag = "selectTag"
var postNum = 100;
var postSet = [];
var isPostEnd;
var maxPostNum=3;
var postIdx = 0;
var imgSrc = [];
var imgFile = [];
const maxImgNum =3;
var tagid = 0;
var moreId = 0;
var tag = '';
const imagefileTypes = [
    'image/jpeg',
    'image/pjpeg',
    'image/png'
];
var photoTemplate = "<img src='{0}' width='160px' height='90px' style='vertical-align: middle'>";
var videoTemplate = "<video src='{0}' width='160px' height='90px' style='vertical-align: middle' controls ></video>"

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
    if(medias.length!=0) {
        medias.forEach(element => {
            formData.append('medias[]', element);
        })
        formData.append('hasMedia', true);
    }
    else {
        let img = new File([], "", undefined);
        medias.push(img);
        medias.forEach(element => {
            formData.append('medias[]', element);
        })
        formData.append('hasMedia', false);
    }


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
            async:false,
            success:function(res){
                postSet = res.posts;
                // 考虑怎样将帖子动态链接到页面中
            }
        }
    )
}




//向后台获取帖子信息，s为开始位置，n为请求帖子数
function fetchPost(s,n){
    $.ajax({
        url:"/data/fetchPost",
        type:"post",
        data:{"start":s, "num":n},
        async: false,
        success:function(res){
            // console.log(res);
            // res = eval("(" + res + ")");
            // console.log(typeof res);
            postSet = res.posts;
            isPostEnd = res.end;
            postNum = res.posts.length;
        }
    })
}

function viewMore(){
    moreId += 1;
    if(postIdx<postNum){
        for(var i=0;i<maxPostNum;i++){
            if(i+postIdx>=postSet.length){
                break;
            }
            $('#{0}'.format('postArea2')).append(showPostHtml(postSet[i+postIdx]));
        }
        postIdx += maxPostNum;
        if(postIdx<postNum){
            $('#postArea2').append(
                '<button id="btnMorePost{0}" class="btn btn-primary btn-sm" onclick="viewMore()">浏览更多</button>'.format(moreId)
            );
        }
    }
    $('#btnMorePost{0}'.format(moreId-1)).remove();
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
        "                        <a  href = \"javascript:void(0)\" class=\"mr-3 text-secondary\"><i class=\"feather-heart text-danger\" onclick=\"Like({5})\" id='like{5}'>{6}</i></a>\n" +
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
    var html = "<div align=\"absbottom\">{0}</div>";
    var medianHtml = "";
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

function Like(postId){
    var flag = saveLikedToDB(postId,uid);
    var numUpdated;
    if(flag != false){
        // 实现根据postid获取更新后的点赞数
        numUpdated = updatedCount(postId);
        console.log(numUpdated);
        // document.getElementById("like"+postId).innerText = numUpdated.toString()+"已赞";
        $('#like{0}'.format(postId)).html(numUpdated.toString()+"已赞");
    }
    else {
        alert("您已点赞");
    }
}


// 搜索框，实现根据content搜索用户表-->美食tag表，并返回相应的帖子显示
function searchByUserOrTag2(content) {
    $.ajax(
        {
            url:"data/isTag",
            type:"post",
            data:{"content":content},
            async:false,
            success:function(res){
                postSet = res.posts;
                // 考虑怎样将帖子动态链接到页面中
            }
        }
    )
}

function search(content) {
    $.ajax(
        {
            url:"data/findSearchType",
            type:"post",
            data:{"content":content},
            async:false,
            success:function(res){
                console.log("res={0}".format(res));
                switch (res){
                    case 1:
                        searchByUserOrTag(content);
                        console.log("rendering");
                        renderPost();
                        break;
                    case 0:
                        $.ajax({
                            url:'data/getUserIdByName',
                            data:{"username":content},
                            success(res){
                                location.href='profile?uid={0}&vid={1}'.format(res, uid);
                            }
                        });
                        break;
                    default:
                        alert('查找失败');
                }
            }
        }
    )
}

// 实现将该点赞数据存入数据库的动作
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

function renderPost(){
    $('#postArea1').html('');
    $('#postArea2').html('');
    if(postSet.length!=0){
        $('#{0}'.format('postArea1')).append(showPostHtml(postSet[0]));
        postIdx = 1;
        if(postIdx<postNum){
            for(var i=0;i<maxPostNum;i++){
                if(i+postIdx>=postSet.length){
                    break;
                }
                $('#{0}'.format('postArea2')).append(showPostHtml(postSet[i+postIdx]));
            }
            postIdx += maxPostNum;
            if(postIdx<postNum){
                $('#postArea2').append(
                    '<button id="btnMorePost{0}" class="btn btn-primary btn-sm" onclick="viewMore()">浏览更多</button>'.format(moreId)
                );
            }
        }
    }
}

function addTag(id){
    tag = $('#tag{0}'.format(id)).html();
    $('#tagChoose').html("#"+tag);
    closeDialog();
}

function  openDialog(){
    html = "";
    document.getElementById('light').style.display='block';
    $.ajax(
        {
            url:"data/addTag",
            type:"post",
            data:{},
            success:function(res){
                $('#tagArea').html('');
                tagid =0;
                console.log(res);
                var html ="";
                for(var i=0; i<res.length; i++){
                    html += "<li class=\"mui-table-view-divider mui-indexed-list-group\"><i id=\"tag{0}\">{1}</i><button onclick='addTag({0})'>添加</button></li>".format(tagid,res[i]);
                    tagid+=1;
                }
                // console.log(html);
                btnAddTag.append(html);
                $('#tagArea').html(html);
            }
        }
    )
}

function closeDialog(){
    document.getElementById('light').style.display='none';
    //window.location.replace("index.html");
}
function closeDialog1(){
    document.getElementById('light1').style.display='none';
    //window.location.replace("index.html");
}
function createtag(){
    document.getElementById('light1').style.display='block';
    closeDialog();
}

function createtag1(){
    const name=document.getElementById('input1').value;
    const city=document.getElementById('input2').value;
    const favor=document.getElementById('input3').value;
    const intro=document.getElementById('input4').value;
    $.ajax({
        url:'data/saveTag',
        data:{
            "name":name,"city":city,"favor":favor,"intro":intro
        },
        success:function (res) {
            if(res){
                closeDialog1();
            }
            else {
                alert("该城市不存在");
            }
        }
    })

}

function getDomByXpath(xpath) {
    var result = document.evaluate(xpath, document, null, XPathResult.ANY_TYPE, null);
    return result.iterateNext()
}

//-------------------------主程序区-------------------
window.onbeforeunload = logout();
login();
showTag();
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
    }
})
$.ajax({
    url:'data/username',
    data:{"uid":uid},
    success:function (res) {
        $('#textUser').html(res);
        $('#textUser1').html(res);
    }
})

$.ajax({
    url:'data/likeRankedPosts',
    data:{"uid":uid},
    success:function (res) {
        // 获取到前五个点赞最多的list
        console.log(res)
        res = res.slice(1,-1);
        arr = res.split(",");
        var arrSize = arr.length;
        for (var i=0; i<arrSize; i+=2){
            var rankedHtml ="<div class=\"d-flex align-items-center osahan-post-header mb-3 people-list\">\n" +
                "                           <div class=\"dropdown-list-image mr-3\">\n" +
                "                              <img class=\"rounded-circle\" src=\"img/p8.png\" alt=\"\">\n" +
                "                              <div class=\"status-indicator bg-success\"></div>\n" +
                "                           </div>\n" +
                "                           <div class=\"font-weight-bold mr-2\">\n" +
                "                              <div class=\"text-truncate\">" + arr[i] + "</div>\n" +
                "                              <div class=\"small text-gray-500\">" + arr[i+1] + "</div>\n" +
                "                           </div>\n" +
                "                           <span class=\"ml-auto\"><button type=\"button\" class=\"btn btn-light btn-sm\"><i class=\"feather-chevron-right\"></i></button>\n" +
                "                           </span>\n" +
                "                        </div>"

            $('#rankedLike').append(rankedHtml);
        }
    }
})

$.ajax({
    url:'data/RankedTags',
    data:{"uid":uid},
    success:function (res) {
        // 获取到前五个点赞最多的list
        console.log(res)
        res = res.slice(1,-1);
        arr = res.split(",");
        var arrSize = arr.length;
        var j=1;
        for (var i=0; i<arrSize; i+=3){
                // var name = getDomByXpath('html/body/div/div/div/main/div/div/div[4]/div/div/div/div['+ j+2 +']/a/div/div[1]/div/h6');
                // console.log(name);
                // var city = getDomByXpath('//*[@id="myTabContent"]/div[4]/div/div/div/div['+ j+2 +']/a/div/div[1]/div/div');
                // var likeNum = getDomByXpath('//*[@id="myTabContent"]/div[4]/div/div/div/div['+ j+2 +']/a/div/div[2]/span');
            $('#tagName1').textContent=arr[i];
            $('#tagCity1').textContent = arr[i+1];
            $('#like1').textContent = arr[i+2] + "点赞";

        }
    }
})

$('#btnSendPost').click(function (){
    console.log('sending');
    // let tag = $('#'+selectTag).val();
    let lat = 40;
    let lng = 35.5;
    let content = $('#sendArea').val();
    if(tag!=''){
        submitPost(uid, content, tag, lat, lng, imgFile)
        $('#iptAddImage').val('');
        $('#tagChoose').html('');
        tag = '';
        $('#mediaArea').html('<textarea placeholder="我想分享..." class="form-control border-0 p-0 shadow-none" rows="3" id="sendArea"></textarea>');
    }
    else {
        alert("请选择标签");
    }
})
$('#iptAddImage').on('change', function(){
    var fileList = this.files;
    for(var i = 0; i < fileList.length; i++) {
        if(imgSrc.length==maxImgNum){
            alert("最多添加{0}张图片".format(maxImgNum))
            break;
        }
        var imgSrcI = getObjectURL(fileList[i]);
        imgSrc.push(imgSrcI);
        imgFile.push(fileList[i]);
        if(imagefileTypes.includes(fileList[i].type)){
            $("#mediaArea").append(photoTemplate.format(imgSrcI));
        }
        else {
            $("#mediaArea").append(videoTemplate.format(imgSrcI));
        }
    }
})
fetchPost(0, postNum);
renderPost();
$('#hrefToProfile').attr('href', 'profile?uid={0}&vid={0}'.format(uid));

$('#btnSearch').click(function () {
    var content = $('#search_input').val();
    // console.log(content);
    // 用户优先
    search(content);
})

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
    document.getElementById("like").innerText = numUpdated + "";
})

$.ajax({
    url:'data/recommend',
    success: function (res){
        console.log(res);
        htmlTemplate = '                     <div class="shadow-sm border rounded bg-white job-item mb-3">\n' +
            '                        <div class="p-3 border-bottom">\n' +
            '                        <h6 class="font-weight-bold text-gold" >{0}</h6><button onclick=search("{0}")>相关帖子</button>\n' +
            '                        </div>\n' +
            '                     </div>'
        for(var i=0;i<res.length;i++){
            console.log(htmlTemplate.format(String(res[i])));
            $('#divRecommend').append(htmlTemplate.format(String(res[i])));
        }


    }
})

