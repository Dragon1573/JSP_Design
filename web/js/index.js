/**
 * 刷新评论
 */
// 刷新方法
function fetchComments() {
    $.ajax({
        url: "fetch?username=" + $("title#username")[0].text,
        type: "GET",
        dataType: "json",

        success: function (response) {
            let context = "";
            // 将返回值转换为JSON数组
            let array = eval(response);
            $.each(array, function (k) {
                context += "<tr><td class='left'>" + array[k]["SENDER"] + "：";
                if (array[k]["DETAILS"].length > 30) {
                    context += array[k]["DETAILS"].substr(0, 30) + "……";
                } else {
                    context += array[k]["DETAILS"];
                }
                context += "</td><td style='float: right;'>" + array[k]["DATETIME"] + "</td></tr>";
            });
            $("tbody#comments").html(context);
        },

        error: function () {
            alert("错误：服务器连接失败！");
        }
    })
}

/**
 * jQuery Ajax实现无刷新实时评论
 */
$(function () {
    fetchComments();
    // 每秒刷新一次评论区
    setInterval(fetchComments, 15000);
});

/**
 * 发送评论
 */
function sendComments() {
    // 获取评论内容
    let contents = $("textarea#sender").text();

    if (contents === "") {
        alert("评论内容不能为空！");
        return;
    }

    // 通过jQuery Ajax向服务器发送评论
    $.ajax({
        url: "save",
        type: "POST",
        data: {
            "user": $("title#username").val(),
            "content": contents
        },

        success: function () {
            alert("发送成功！");
            fetchComments();
        },
        error: function () {
            alert("发送失败，请重试……");
            fetchComments();
        }
    })
}
