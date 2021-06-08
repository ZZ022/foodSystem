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
    myChart.showLoading();
    $.getJSON(ROOT_PATH + '/data/asset/data/les-miserables.json', function (graph) {
    myChart.hideLoading();
    graph.nodes.forEach(function (node) {
    node.label = {
    show: node.symbolSize > 30
};
});

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
    data: graph.categories.map(function (a) {
    return a.name;
})
}],
    animationDuration: 1500,
    animationEasingUpdate: 'quinticInOut',
    series: [
{
    name: 'Les Miserables',
    type: 'graph',
    layout: 'none',
    data: graph.nodes,
    links: graph.links,
    categories: graph.categories,
    roam: true,
    label: {
    position: 'right',
    formatter: '{b}'
},
    lineStyle: {
    color: 'source',
    curveness: 0.3
},
    emphasis: {
    focus: 'adjacency',
    lineStyle: {
    width: 10
}
}
}
    ]
};
    myChart.setOption(option);
});

    if (option && typeof option === 'object') {
    myChart.setOption(option);
}