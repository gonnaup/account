var routerTo = function (url) {
    axios.get(url)
        .then(function (result) {
            var $ = layui.jquery;
            $("#content-container").html(result.data);
        })
        .catch(function (err) {
            return layui.layer.msg('无法获取页面：' + url);
        });
}