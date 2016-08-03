<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="tag" tagdir="/WEB-INF/tags" %>

 <html>
<head>
<meta content="text/html; charset=utf-8" http-equiv="Content-Type" />
<title>Sign in with Facebook example</title>
</head>
<style>

body{
   background-color:black;
    font-family: "HelveticaNeue-Light","Helvetica Neue Light","Helvetica Neue",helvetica,"lucida grande",tahoma,verdana,arial,sans-serif;
    font-weight: 300;
    font-size: 60;
}
.contents .line_1 {
    color: #797979;
}
.contents .line_2 {
    color: #A9AAA9;
}
.contents .line_3 {
    color: #D0D0D0;
}

 .line_4 {
    color:#FFFFFF ;
}
a
{
text-decoration: none;
}
a:hover {
    color: #00FF33;
    outline: medium none;
    text-decoration: none;
}

 
</style>
<body>
<tag:notloggedin>
<div class="contents">
<span class="line_1">On FriendsOfMusic,</span>
<br>
<span class="line_2">let's take music</span>
<br>
<span class="line_3">to a whole new level . . .</span>
<br>
<br>
  </div>
  <a href="signin" class="line_4">Ready?</a>

</tag:notloggedin>
<tag:loggedin>
  <h1>Welcome ${facebook.name} (${facebook.id})</h1>
  <form action="./post" method="post">
    <textarea cols="80" rows="2" name="message"></textarea>
    <input type="submit" name="post" value="statuses" />
  </form>
<a href="./logout">logout</a>
</tag:loggedin>
</body>
</html>