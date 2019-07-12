/**
 * 使用jQuery实现Ajax局部刷新
 * @param username 用户名
 */

function uniqueCheck(username) {
    $.ajax({
        url: "check?username=" + username.value,
        type: "GET",
        dataType: "json",

        // 回调函数
        success: function (serverResponse) {
            if (serverResponse["FLAG"] === "UNIQUE" || username.value === "") {
                // 无输入或用户唯一
                $("p#unique").hide();
            } else {
                $("p#unique").show();
            }
        },

        // 异常处理函数
        error: function () {
            alert("错误：请求页面异常！");
        }
    })
}

/**
 * 密码匹配校验
 */
function confirmCheck(password, confirm) {
    /**
     * jQuery选择器在每一次进行选择的时候
     * 都会在整个页面重新搜索元素
     * 官方API文档建议用变量存储
     */
    if (password.value === "" || confirm.value === "" ||
        password.value === confirm.value) {
        // 未输入密码或密码相同
        $("p#difference").hide();
    } else {
        // 密码存在差异
        $("p#difference").show();
    }
}
