/**
 * 刷新评论
 */
function fetchComments() {
    $.ajax({
        url: "sync?username=" + $("title#username")[0].text,
        type: "GET",
        dataType: "json",

        success: function (response) {
            let context = "";

            // 遍历数组元素
            $.each(response, function (k) {
                context += "<tr><td class='left'>" + response[k]["SENDER"] + "：";
                if (response[k]["DETAILS"].length > 30) {
                    context += response[k]["DETAILS"].substr(0, 30) + "……";
                } else {
                    context += response[k]["DETAILS"];
                }
                context += "</td><td style='float: right;'>" + response[k]["DATETIME"] + "</td></tr>";
            });

            // 空评论过滤
            if (context === "") {
                context = "<tr><td colspan='3' style='font-size: large'>" +
                    "【系统消息】未获取到任何评论！" +
                    "</td></tr>";
            }

            $("tbody#comments").html(context);
        },

        error: function () {
            alert("错误：服务器连接失败！");
        }
    })
}

/**
 * 发送评论
 */
function sendComments(username) {
    // 获取评论内容
    let content = $("textarea#sender").val();

    if (content === "" || username === "Anonymous") {
        alert("评论内容不能为空！");
        return;
    }

    // 通过jQuery Ajax向服务器发送评论
    $.ajax({
        url: "sync",
        type: "POST",
        dataType: "json",
        data: {
            "username": username,
            "content": content
        },

        success: function (response) {
            alert((response["FLAG"] === true) ? "发送成功！" : "发送失败！");
            fetchComments();
            $("button#reset").click();
        },
        error: function () {
            alert("发送失败，请重试……");
            fetchComments();
        }
    });
}

/**
 * 刷新仓库列表
 */
function fetchRepositories() {
    $.ajax({
        url: "repositories",
        type: "GET",
        dataType: "json",
        data: {
            "username": $("title#username")[0].text
        },

        success: function (response) {
            let context = "";

            $.each(response, function (k) {
                context += "<tr><td style='font-size: larger'>" + response[k]["USERNAME"] + "/" + response[k]["REPOSITORY"] + "</td></tr>";
            });

            if (context === "") {
                context = "<tr><td style='font-size: large'>【系统消息】未找到任何仓库！</td></tr>";
            }
            $("tbody#repositories").html(context);
        },

        error: function () {
            alert("错误：服务器连接失败！");
        }
    });
}


/**
 * jQuery Ajax实时刷新
 */
$(function () {
    // 页面加载时首次刷新
    fetchComments();
    fetchRepositories();
    // 每15秒刷新一次评论区
    setInterval(fetchComments, 15000);
});
