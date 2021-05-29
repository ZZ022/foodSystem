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