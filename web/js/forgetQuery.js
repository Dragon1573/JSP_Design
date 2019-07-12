/**
 * 对用户进行检查
 * @param username
 */
function checkUser(username) {
    // 设置ajax
    $.ajax({
        /* 开始：以下内容用于建立服务器请求 */
        // 服务器地址
        url: "check?username=" + username.value,
        // 数据传输方法
        type: "GET",
        dataType: "json",
        /* 结束：以上内容用于建立服务器请求 */

        // 回调函数
        success: function (serverResponse) {
            if (username.value === "") {
                // 无输入
                $("#errorMessage").hide();
                $("#question").hide();
                $("#answer").hide();
            } else if (serverResponse["FLAG"] !== "DUPLICATE") {
                // 存在异常
                if (serverResponse["FLAG"] === "UNIQUE") {
                    // 用户不存在
                    $("#errorMessage").html("错误：用户不存在").show();
                } else {
                    // 无密保问题
                    $("#errorMessage").html(
                        "错误：未设置密保，请联系管理员！").show();
                }
                $("#question").hide();
                $("#answer").hide();
            } else {
                // 没有异常
                $("#question").html("密保问题：" +
                    serverResponse["QUESTION"]).show();
                $("#answer").show();
                $("#errorMessage").hide();
            }
        },

        // 异常处理函数
        error: function () {
            alert("错误：请求页面异常！");
        }
    })
}
