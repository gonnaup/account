/**
 * 填充账号信息
 * @param account
 */
function fillAccountInformation(account) {
    var $ = layui.jquery;
    $("#login_entrance").css('display', 'none')
    $("#account_information").css('display', 'inline-block')
    // $("#account_avatar").attr('src', account.accountAvatar)
    $("#account_nickname").html(account.accountNickname)
    $("#login_entrance_logo").html("请登录")
}