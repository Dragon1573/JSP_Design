/**
 * @author Dragon1573
 * @date 2019/7/22
 */

/**
 * 计算相对路径
 */

/**
 * 用户改名确认
 */
function acceptWarning() {
    alert("您已确认修改用户名，我们不对修改用户名产生的任何问题负责！");
    $("p#warning").hide();
    $("form#change_username").show();
    $("button#accept_warning").hide();
}

/**
 * 落雪效果
 */
function snow() {
    $("body").append('<canvas id="christmasCanvas"></canvas>');
    let b = $("#christmasCanvas")[0];
    let a = b.getContext("2d");
    let d = window.innerWidth;
    let c = window.innerHeight;
    let e;
    b.width = d;
    b.height = c;
    for (e = [], b = 0; b < 70; b++) {
        e.push({
            x: Math.random() * d,
            y: Math.random() * c,
            r: Math.random() * 4 + 1,
            d: Math.random() * 70
        });
    }
    let h = 0;
    window.intervral4Christmas = setInterval(
        function () {
            let f;
            a.clearRect(0, 0, d, c);
            a.fillStyle = "rgba(255, 255, 255, 0.6)";
            a.shadowBlur = 5;
            a.shadowColor = "rgba(255, 255, 255, 0.9)";
            a.beginPath();
            for (let b = 0; b < 70; b++) {
                f = e[b];
                a.moveTo(f.x, f.y);
                a.arc(f.x, f.y, f.r, 0, Math.PI * 2, true);
            }
            a.fill();
            h += 0.01;
            for (let b = 0; b < 70; b++) {
                f = e[b];
                f.y += Math.cos(h + f.d) + 1 + f.r / 2;
                f.x += Math.sin(h) * 2;
                if (f.x > d + 5 || f.x < -5 || f.y > c) {
                    e[b] = b % 3 > 0 ? {
                        x: Math.random() * d,
                        y: -10,
                        r: f.r,
                        d: f.d
                    } : Math.sin(h) > 0 ? {
                        x: -5,
                        y: Math.random() * c,
                        r: f.r,
                        d: f.d
                    } : {
                        x: d + 5,
                        y: Math.random() * c,
                        r: f.r,
                        d: f.d
                    }
                }
            }
        }, 70
    )
}

/**
 * 绘制Canvas画布
 */
function drawCanvas() {
    canvasObject.clearRect(0, 0, width, height);
    q = [
        {
            x: 0,
            y: height * 0.7 + f
        },
        {
            x: 0,
            y: height * 0.7 - f
        }
    ];
    while (q[1].x < width + f) {
        d(q[0], q[1]);
    }
}

function d(i, j) {
    canvasObject.beginPath();
    canvasObject.moveTo(i.x, i.y);
    canvasObject.lineTo(j.x, j.y);
    let k = j.x + (Math.random() * 2 - 0.25) * f;
    let n = y(j.y);
    canvasObject.lineTo(k, n);
    canvasObject.closePath();
    let r = u / 50;
    canvasObject.fillStyle = "#" + (Math.cos(r) * 127 + 128 << 16 | Math.cos(r + u / 3) * 127 + 128 << 8 | Math.cos(r + u / 3 * 2) * 127 + 128).toString(16);
    canvasObject.fill();
    q[0] = q[1];
    q[1] = {
        x: k,
        y: n
    };
}

function y(p) {
    let t = p + (Math.random() * 2 - 1.1) * f;
    return (t > height || t < 0) ? y(p) : t;
}

/**
 * 对用户进行检查
 *
 * @param username 用户名
 */
function checkUser(username) {
    // 设置ajax
    $.ajax({
        /* 开始：以下内容用于建立服务器请求 */
        // 服务器地址
        url: "/JSP_Design/check?username=" + username.value,
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

/**
 * 刷新评论
 */
function fetchComments() {
    $.ajax({
        url: "/JSP_Design/sync?username=" + $("title#username")[0].text,
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
 *
 * @param username 用户名
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
        url: "/JSP_Design/sync",
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
        url: "/JSP_Design/repositories",
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
 * 使用jQuery实现Ajax局部刷新
 *
 * @param username 用户名
 */
function uniqueCheck(username) {
    $.ajax({
        url: "/JSP_Design/check?username=" + username.value,
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
 *
 * @param password 密码
 * @param confirm 确认密码
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

/**
 * 更新用户信息
 *
 * @param fields 信息字段
 * @param newProfile 新数据
 */
function updateProfile(fields, newProfile) {
    $.ajax({
        url: "/JSP_Design/settings",
        method: "POST",
        data: {
            "fields": fields,
            "news": newProfile.value,
        },
        dataType: "json",

        success: function (response) {
            if (response["SUCCESS"]) {
                alert("信息更新成功！");
                window.location.reload();
            } else {
                alert("错误：信息更新失败，请重试！");
            }
        },
        error: function () {
            alert("错误：服务器连接异常！");
        }
    })
}
