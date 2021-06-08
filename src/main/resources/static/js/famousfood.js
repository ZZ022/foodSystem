//初始化地图
var map = new ol.Map({
    target: 'map',
    layers: [
      new ol.layer.Tile({
        source: new ol.source.OSM()
      })
    ],
    view: new ol.View({
      center: new ol.proj.fromLonLat([117.191166,34.289749],'EPSG:3857'),
      zoom: 5
    })
});


var image_urls=[];

$.ajax({
    url:'data/getImages',
    data:{},
    async: false,
    success:function (res) {
        // 获取到需要的image数组和相应的city(后续加上经纬度
        res = res.slice(1,-1);
        arr = res.split(",");
        var arrSize = arr.length;
        for(var i=0;i<arrSize;i+=3){
            image_urls.push(arr[i+2])
        }
        console.log(image_urls);
    }
})


//创建标签,需要美食的经纬度，
var lon=105.191166;
var lat=25.289749;
var name1="beijing";
var romeArr = [];
for(var i=0;i<image_urls.length;i++){
    lon=lon+Math.random()*2;
    lat=lat+Math.random()*2;
    var rome = new ol.Feature({
        geometry:new ol.geom.Point(new ol.proj.fromLonLat([lon,lat],'EPSG:3857')),
        name:name1,
    });
    rome._id=i;
    //标注样式设置
    rome.setStyle(new ol.style.Style({
        image: new ol.style.Icon(({
            crossOrigin: 'anonymous',
            scale:0.5, //标注图标大小
            src: 'img/fav.png'
        }))
    }));
    romeArr.push(rome);

}


//给标签添加动画
var select= new ol.interaction.Select();
//map加载该控件，默认是激活可用的
map.addInteraction(select);
select.on('select', function(e) {
    /*恢复其他图标样式*/
    romeArr.forEach(function(ele){
        ele.setStyle(new ol.style.Style({
            image: new ol.style.Icon(({
                crossOrigin: 'anonymous',
                scale:0.5,
                src: 'img/fav.png'
            }))
        }));
    })

    change(listI[e.selected[0]._id]);

    var currentRome = e.selected[0];
    currentRome.setStyle(new ol.style.Style({
        image: new ol.style.Icon(({
            crossOrigin: 'anonymous',
            scale:0.8,
            src: 'img/fav.png'
        }))
    }));
});

vectorSource = new ol.source.Vector({
    features: romeArr
});
var vectorLayer = new ol.layer.Vector({
    source: vectorSource
});

map.addLayer(vectorLayer);

//图片部分
var list = document.getElementById('list');


//读取图片
for(let j=0;j<image_urls.length;j++){
  const imgg = document.createElement('img');
  imgg.src=image_urls[j];
  console.log(imgg.src);

  imgg.setAttribute("onclick","javascript:location.href='food.html'");
  imgg.setAttribute("style","width: 180px;height: 180px;object-fit: cover;");
  list.appendChild(imgg);
  
}



var listI = list.getElementsByTagName('img');
//图片联动 图片带动图标联动
window.onload = function () {
  //给每一张图片添加事件绑定
  for (let i = 0; i < listI.length; i++) {
      listI[i].addEventListener("mouseover", function () {
          change(this);
          select.getFeatures().clear(); //因为我的需求是单选,所以先清除所有选择
          select.getFeatures().push(romeArr[i]);//将要选择的要素push进去
          select.dispatchEvent({type: "select",selected: [romeArr[i]]})//以上并不会触发'select'事件,所以需要手动触发一下
      }, false);
      
  }
  //鼠标移出list区域，图片回到初始时的宽度200
  list.addEventListener("mouseout", function () {
      for (var i = 0; i < list.children.length; i++) {
          startMove(list.children[i], {  
              "opacity": "100"
          }, 50);
      }
  }, false);

}

//当鼠标移过图片时，让焦点没在其他图片上的图片宽度变为100px,透明度变为0.7，而鼠标移动到的那张图片宽度变为600
function change(obj) {
  var children = obj.parentNode.children; //为了获得其他img的兄弟节点   

  for (var j = 0; j < children.length; j++) {
      if (obj != children[j]) {
          startMove(children[j], {
              "opacity": "50"
          }, 50, startMove(obj, {
              "opacity": "100"
          }, 50));
      }
  }; 
}
//进行js运动效果的函数
function startMove(obj, json, interval, fn) {
  clearInterval(obj.timer); //该对象每次开始动画，都先停止掉正在进行的计数器,以免发生计数器运动速度会不断增快的效果。
  var flag; //用来表示所有运动是否到达目标值
  //开启定时器，每隔Interval时间段执行相应动作
  
  obj.timer = setInterval(function () {
      flag = true; //进入定时器时,现将flag设置为所有的属性都已达到目标值
      //获取传过来的Json值（需要变化的属性，因为要同时执行多属性，所以这里使用了json传值）。
      for (var attr in json) {

          var curr = 0;//用来获得当下的属性值
          //判断所传递的属性是否为透明度
          if (attr == 'opacity') { //如果是透明度，则获取该对象此刻的透明度值

              curr = Math.round(parseFloat(getStyle(obj, attr)) * 100);
          } else { //否则，获取该属性的当下其他属性值
              curr = parseInt(getStyle(obj, attr));
          }
          //进行运动的速度处理
          var speed = 0;
          speed = (json[attr] - curr) / 10; //每次速度变化的增量，每次实时的获得，可以达到变速运动
          speed = speed > 0 ? Math.ceil(speed) : Math.floor(speed); //速度增量大于0，向上取整，速度增量小于0,向下取整
          if (curr != json[attr]) { //当随着属性值还不等于要达到的目标值，就将flag设置为false;
              flag = false;
          }
          //进行运动变化
          if (attr == 'opacity') {
              obj.style.filter = 'alpha(opacity:' + (curr + speed) + ")";
              obj.style.opacity = (curr + speed) / 100;
          } else {
              obj.style[attr] = curr + speed + 'px';
          }
      }


      if (flag) { //如果flag值为true,说明传来的属性值，都已经变化到目标值，就可以清除计数器，
          //同时在检查是否有回调函数传入,若有就继续执行回调函数。
          clearInterval(obj.timer);
          if (fn) {
              fn();
          }
      }
  }, interval);
}
//获得样式函数 
function getStyle(element, attr) {
  var value;
  if (typeof window.getComputedStyle != 'undefined') { //非ＩＥ下获得属性的方法
      value = window.getComputedStyle(element, null)[attr];
  } else if (typeof element.currentStyle != 'undefined') { //IE下获得属性的方法
      value = element.currentStyle[attr];
  }
  return value;

}

// <-----------------主程序区-------------------->
