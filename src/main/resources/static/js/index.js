const searchParams = new URLSearchParams(new URL(location.href).search);
const uid = Number(searchParams.get('uid'));
const btnAddTag = $('#addTag');
const selectTag = "selectTag"
var isAddTag;

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

$.ajax({
    url:"/data/login",
    type:"post",
    data:{"uid":uid},
    success:function(res){
    }
    })
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
