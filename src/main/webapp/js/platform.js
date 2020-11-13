/**
 * @author Dragon1573
 * @date 2019/7/22
 */

/* @formatter:off */
function acceptWarning(){alert("您已确认修改用户名，我们不对修改用户名产生的任何问题负责！"),$("p#warning").hide(),$("form#change_username").show(),$("button#accept_warning").hide()}function snow(){$("body").append('<canvas id="christmasCanvas" />');let e,t=$("#christmasCanvas")[0],n=t.getContext("2d"),a=window.innerWidth,r=window.innerHeight;for(t.width=a,t.height=r,e=[],t=0;t<70;t++)e.push({x:Math.random()*a,y:Math.random()*r,r:4*Math.random()+1,d:70*Math.random()});let s=0;window.intervral4Christmas=setInterval(function(){let t;n.clearRect(0,0,a,r),n.fillStyle="rgba(255, 255, 255, 0.6)",n.shadowBlur=5,n.shadowColor="rgba(255, 255, 255, 0.9)",n.beginPath();for(let a=0;a<70;a++)t=e[a],n.moveTo(t.x,t.y),n.arc(t.x,t.y,t.r,0,2*Math.PI,!0);n.fill(),s+=.01;for(let n=0;n<70;n++)(t=e[n]).y+=Math.cos(s+t.d)+1+t.r/2,t.x+=2*Math.sin(s),(t.x>a+5||t.x<-5||t.y>r)&&(e[n]=n%3>0?{x:Math.random()*a,y:-10,r:t.r,d:t.d}:Math.sin(s)>0?{x:-5,y:Math.random()*r,r:t.r,d:t.d}:{x:a+5,y:Math.random()*r,r:t.r,d:t.d})},70)}function drawCanvas(){for(canvasObject.clearRect(0,0,width,height),q=[{x:0,y:.7*height+f},{x:0,y:.7*height-f}];q[1].x<width+f;)d(q[0],q[1])}function d(e,t){canvasObject.beginPath(),canvasObject.moveTo(e.x,e.y),canvasObject.lineTo(t.x,t.y);let n=t.x+(2*Math.random()-.25)*f,a=y(t.y);canvasObject.lineTo(n,a),canvasObject.closePath();let r=u/50;canvasObject.fillStyle="#"+(127*Math.cos(r)+128<<16|127*Math.cos(r+u/3)+128<<8|127*Math.cos(r+u/3*2)+128).toString(16),canvasObject.fill(),q[0]=q[1],q[1]={x:n,y:a}}function y(e){let t=e+(2*Math.random()-1.1)*f;return t>height||t<0?y(e):t}function checkUser(e){$.ajax({url:"/JSP_Design/check?username="+e.value,type:"GET",dataType:"json",success:function(t){""===e.value?($("#errorMessage").hide(),$("#question").hide(),$("#answer").hide()):"DUPLICATE"!==t["FLAG"]?("UNIQUE"===t["FLAG"]?$("#errorMessage").html("错误：用户不存在").show():$("#errorMessage").html("错误：未设置密保，请联系管理员！").show(),$("#question").hide(),$("#answer").hide()):($("#question").html("密保问题："+t["QUESTION"]).show(),$("#answer").show(),$("#errorMessage").hide())},error:function(){alert("错误：请求页面异常！")}})}function fetchComments(e){$.ajax({url:"/JSP_Design/sync",type:"GET",dataType:"json",data:{username:e},success:function(e){let t="";$.each(e,function(n){t+="<tr><td class='left'><a href='details.jsp?timestamp="+e[n]["DATETIME"]+"'>",t+=e[n]["SENDER"]+"：",e[n]["TITLE"].length>30?t+=e[n]["TITLE"].substr(0,30)+"……":t+=e[n]["TITLE"],t+="</a></td>",t+="<td style='float: right;'>"+e[n]["DATETIME"]+"</td></tr>"}),""===t&&(t="<tr><td colspan='3' style='font-size: large'>"+"<span class='warning'>【系统消息】未获取到任何评论！</span>"+"</td></tr>"),$("tbody#comments").html(t)},error:function(){alert("错误：服务器连接失败！")}})}function sendComments(e,t,n){""!==n&&"Anonymous"!==e?$.ajax({url:"/JSP_Design/sync",type:"POST",dataType:"json",data:{user:e,title:t,details:n},success:function(e){alert(!0===e["FLAG"]?"发送成功！":"发送失败！"),fetchComments("username"),$("button#reset").click()},error:function(){alert("发送失败，请重试……"),fetchComments("username")}}):alert("评论内容不能为空！")}function deleteRepositories(e,t){confirm("【警告】\n"+"删除仓库是一个具有危险性的操作，一旦执行将不可撤销。\n"+"您确认要删除仓库？")&&$.ajax({url:"/JSP_Design/repositories",type:"POST",dataType:"json",data:{method:"delete",username:e,new_name:null,old_name:t},success:function(t){t["SUCCESS"]?alert("删除成功！"):alert("错误：删除失败！"),fetchRepositories(!0,e)},error:function(){alert("错误：服务器连接异常！"),fetchRepositories(!0,e)}})}function renameRepositories(e,t){$("tbody#repositories").children().each(function(){let n=$(this).children().first(),a=n.text(),r=a.substr(a.indexOf("/")+1);n.html("<input type='text' />"),n.children().val(r),n.next().children().text("确定").unbind("click").click("click",function(){$.ajax({url:"/JSP_Design/repositories",type:"POST",dataType:"json",data:{method:"rename",username:e,new_name:n.children().val(),old_name:t},success:function(t){t["SUCCESS"]?alert("重命名成功！"):alert("错误：重命名失败！"),fetchRepositories(!0,e)},error:function(){alert("错误：服务器连接异常！"),fetchRepositories(!0,e)}})}),n.next().next().children().text("取消").unbind("click").click(function(){fetchRepositories(!0,e)})})}function fetchRepositories(e,t){$.ajax({url:"/JSP_Design/repositories",type:"GET",dataType:"json",data:{username:t},success:function(n){let a=$("tbody#repositories");a.empty(),$.each(n,function(r){let s=$("<tr></tr>");s.append("<td class='column1'></td>"),s.children("td.column1").append("<a href='/JSP_Design/files.jsp?user="+n[r]["USERNAME"]+"&repo="+n[r]["REPOSITORY"]+"&path='></a>"),s.children("td.column1").children("a").html(n[r]["USERNAME"]+"/"+n[r]["REPOSITORY"]),e&&(s.append("<td class='column2'></td>","<td class='column3'></td>"),s.children("td.column2").append("<a class='rename' href='javascript:void(0)'>重命名</a>"),s.children("td.column3").append("<a class='delete' href='javascript:void(0)'>删除</a>"),s.find("a.rename").on("click",function(){renameRepositories(t,n[r]["REPOSITORY"])}),s.find("a.delete").on("click",function(){deleteRepositories(t,n[r]["REPOSITORY"])})),a.append(s)}),""===a.html()&&a.html("<tr>"+"<td colspan='3' style='text-align:center;font-size:larger'>"+"<span class='warning'>不存在代码仓库！</span>"+"</td>"+"</tr>")},error:function(){alert("错误：服务器连接失败！")}})}function uniqueCheck(e){$.ajax({url:"/JSP_Design/check?username="+e.value,type:"GET",dataType:"json",success:function(t){"UNIQUE"===t["FLAG"]||""===e.value?$("p#unique").hide():$("p#unique").show()},error:function(){alert("错误：请求页面异常！")}})}function confirmCheck(e,t){""===e.value||""===t.value||e.value===t.value?$("p#difference").hide():$("p#difference").show()}function updateProfile(e,t,n){$.ajax({url:"/JSP_Design/settings",method:"POST",data:{fields:e,news:t.value,old:n.value},dataType:"json",success:function(e){e["SUCCESS"]?(alert("信息更新成功！"),window.location.reload()):alert("错误：信息更新失败，请重试！")},error:function(){alert("错误：服务器连接异常！")}})}
/* @formatter:on */
