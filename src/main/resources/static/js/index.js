const searchParams = new URLSearchParams(new URL(location.href).search);
const uid = Number(searchParams.get('uid'));
const btnAddTag = $('#addTag');
const selectTag = "selectTag"
var isAddTag;
var isAddPos;
var mediaNum=0;
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
    // console.log({"uid":userId, "content":content, "tag":tag, "lat":lat, "lng": lng, "medias": medias});
    // console.log(JSON.stringify({"uid":userId, "content":content, "tag":tag, "lat":lat, "lng": lng, "medias": medias}))
    var formData = new FormData()
    formData.append("uid", userId);
    formData.append("content",content);
    formData.append("tag",tag);
    formData.append("lat",lat);
    formData.append("lng",lng);
    medias.forEach(element=>{formData.append('medias[]', element)})
    // formData.append("medias",medias[0]);
    for (var [a, b] of formData.entries()) {
        console.log(a, b);
    }
    // console.log(formData);
    $.ajax({
        url:'data/submitPost',
        type: 'POST',
        // data: JSON.stringify({"uid":userId, "content":content, "tag":tag, "lat":lat, "lng": lng, "medias": medias}),
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

//-------------------------主程序区-------------------
login();
window.onbeforeunload = logout();
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
