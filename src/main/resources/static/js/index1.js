
if (option && typeof option === 'object') {
    myChart.setOption(option);
}

var map = new ol.Map({
    target: "map",
    layers: [
    new ol.layer.Tile({
    source: new ol.source.OSM(),
}),
    ],
    view: new ol.View({
    center: ol.proj.fromLonLat([37.41, 8.82]),
    zoom: 4,
}),
});
//cesium
var workEcharts = getWorkEcharts();
Cesium.Ion.defaultAccessToken = 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJqdGkiOiJmMTA3NmNkYS1jZTM4LTQ3ZTctOTFiYi03N2ZiZWI5ZGRiZGIiLCJpZCI6NTcyMTEsImlhdCI6MTYyMjA5Nzk0MH0.hJdsxcTgg8Yi_5I90gNQhIV19xHjUW-VVzGwM0-A4iY';

function initWork() {
    workEcharts.create(viewer)
    workEcharts.activate()
}

initWork()

//网络图
var dom = document.getElementById("graph");
var myChart = echarts.init(dom);
var app = {};
var option;
var ROOT_PATH = 'https://cdn.jsdelivr.net/gh/apache/echarts-website@asf-site/examples';

function  mySum(arr){
    var res = 0;
    console.log(res)
    for(var i = 0;i<=arr.length;i++){
        res += arr[i];
    }
    console.log(res)
    return res;
}




$.ajax({
    url:'data/getGraph',
    success: function (res) {
        console.log(res)
        var x0 = -100
        var y0 = 100
        var flag = [[1,1],[1,-1],[-1,1],[-1,-1]]
        res = JSON.parse(res)
        console.log(res)
        myChart.showLoading();
        myChart.hideLoading();
        var graph = [
            {
                name: '美食网络',
                type: 'graph',
                layout: 'none',
                data: [],
                links: [],
                categories: [],
                roam: true,
                label: {
                    position: 'right',
                    formatter: '{b}'
                },
                lineStyle: {
                    color: 'source',
                    curveness: 0.3,
                    width:5
                },
                emphasis: {
                    focus: 'adjacency',
                    lineStyle: {
                        width: 15
                    }
                }
            }
        ];
        console.log(graph)
        for(var i=0;i<res.size;i++){
            let x = flag[i%4][0]*(parseInt(i/4)+1)*10+x0;
            let y = flag[i%4][1]*(parseInt(i/4)+1)*10+y0;
            let size= 0;
            for(var k=0;k<res.size;k++){
                console.log(res.links[i][k]);
                size += res.links[i][k];
            }
            console.log(x)
            console.log(y)
            // console.log('pushing');
            graph[0].data.push({  "id": String(i),
                "name": res.nodes[i],
                "symbolSize": Math.min(size, 30),
                // "value": 28.685715,
                "x": x,
                "y":y,
                "category": i})
            graph[0].categories.push({
                "name":res.nodes[i]
            })
        }
        for(var i=0;i<res.size;i++){
            for(var j=i+1;j<res.size;j++){
                if(res.links[i][j]!=0){
                    graph[0].links.push({
                        "source": String(i),
                        "target": String(j),
                        "value": res.links[i][j],
                    })
                }
            }
        }
        console.log(JSON.stringify(graph))
        option = {
                title: {
                    text: '美食圈网络图',
                    subtext: 'Default layout',
                    top: 'bottom',
                    left: 'right'
                },
                tooltip: {},
                legend: [{
                    // selectedMode: 'single',
                    data: graph[0].categories.map(function (a) {
                        return a.name;
                    })
                }],
                animationDuration: 1500,
                animationEasingUpdate: 'quinticInOut',
                series:graph,
            };

            if (option && typeof option === 'object') {
                myChart.setOption(option);
            }
    }

})