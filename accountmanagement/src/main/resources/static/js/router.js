var pagepermission_urlMap = new Map()
pagepermission_urlMap.set('../html/operationlog.html', '../api/operationlog/display')

var routerTo = function (url) {
    var $ = layui.jquery;
    var pagepermission_url = pagepermission_urlMap.get(url)
    if (pagepermission_url != undefined) {
        $.ajax({
            url: pagepermission_url,
            type: 'get',
            headers: {token_jwt: localStorage.getItem(jwt_localStorageName)},
            success: function (reData) {
                loadpage(url)
            },
            error: function (reData) {
                $("#content-container").html("<h1>NO PERMISSION</h1>");
            }
        })
    } else {
        loadpage(url)
    }
}

function loadpage(url) {
    var $ = layui.jquery;
    $.ajax({
        url: url,
        type: 'get',
        dataType: 'html',
        success: function (rsData) {
            $("#content-container").html(rsData);
        },
        error: function () {
            return layui.layer.msg('无法获取页面：' + url);
        }
    });
}