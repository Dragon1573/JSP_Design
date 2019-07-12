/**
 * jQuery Ajax实现无刷新实时评论
 * @author Dragon1573
 */

$(function () {
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


    fetchComments();
    // 每秒刷新一次评论区
    setInterval(fetchComments, 15000);
});

/**
 * 发送评论
 */
function sendComments() {
    let comments = $("")
}