/**
 * 实现404页面动态背景效果
 *
 * @author Dragon1573
 */

// 添加事件监听器
document.addEventListener(
    "touchmove",
    function (e) {
        // 不执行默认方法
        e.preventDefault();
    }
);


let canvas = $("canvas")[0];
let canvasObject = canvas.getContext('2d');
let zoomRatio = window.devicePixelRatio || 1;
let width = window.innerWidth;
let height = window.innerHeight;
let f = 90;
let q;
let u = Math.PI * 2;

canvas.width = width * zoomRatio;
canvas.height = height * zoomRatio;
canvasObject.scale(zoomRatio, zoomRatio);
canvasObject.globalAlpha = 0.6;


function i() {
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


document.onclick = i;
document.ontouchstart = i;
i();


let snow = function () {
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
};

snow();
