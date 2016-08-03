<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
<!DOCTYPE html>
<html>
  <head>
    <!-- <link href='http://fonts.googleapis.com/css?family=Droid+Sans+TV' rel='stylesheet' type='text/css'>
    <link type='text/css' href='index.css' rel='stylesheet'> -->
	<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
    <title>Friends of music</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"> 
	<link rel="stylesheet" type="text/css" href="http://fonts.googleapis.com/css?family=Roboto:300">
	<link rel="stylesheet" type="text/css" href="css/common.css"></link>	
    <!--<script type='text/javascript' src='http://ajax.googleapis.com/ajax/libs/jquery/1.5.1/jquery.min.js'></script>-->
	<!-- <script  src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script> --><!-- Google CDN jQuery with fallback to local -->
	<script type='text/javascript' src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
	<script>!window.jQuery && document.write(unescape('%3Cscript src="js/scroll/minified/jquery-1.9.1.min.js"%3E%3C/script%3E'))</script>
	<script src="js/jquery.mCustomScrollbar.concat.min.js"></script>
	<!-- Custom scrollbars CSS -->
	<link href="css/jquery.mCustomScrollbar.css" rel="stylesheet" />
	<script src="jquery-ui-1.9.0.custom.min.js"></script>
    <script type='text/javascript' src='js/index.js'></script>
	
  </head>
  <body>
 	<!-- <div id="bgImage"></div> -->
	<!--Start of header -->
		<header class="header">
			<img id="view_list" src="img/view-list.png"/>
			<img id="view_list_Sel" src="img/selViewList.png"/>
			<div class="logo">
				Friends of music
			</div>
			<div id="proPic" class="rgt"><img src="img/default.jpe"/>
			<div id="signOut">SignOut</div>
			</div>
			<div class="rgt welcomeMsg" ><span id="welcomeText">Hi,</span> <span id="userName">${facebook.name}</span></div>
			
			<div class="clearfix"></div>
			
		</header>
	<!--End of header -->
	<!--Start of content-->
		<div>
			<div class="videoPlayer">
				<div id="player"></div>
				
			
			
			
			
			</div>
			
	
		</div>
	<!--End of Content-->
	<!--Start of drag-able play area-->
		<div class="drgPlay">
			<div id="controls">
			<!--Minimum player control-->
				<div class="lft minControl">
					<img src="img/rewind.png" class="lft cntrlIcon"id="previous"/>
					<img id="play" src="img/pause.png" class="lft cntrlIcon" style="padding-top:5px"/>
					<img src="img/forward.png" id="next" class="lft cntrlIcon"/>
				</div>
			<!--End Minimum player control-->
				<div class="dividerDiv lft">
					<img src="img/playerDivider.png" class="lft divider"/>
				</div>
			<!--Advance player control-->
				<div class="lft advContrl" style="position:relative">
					
					<input type='range' id='volume' title="Volume" min='0' max='100' step='1' value='100'></input>
					<img id="volIcon" src="img/volume.png" class="lft cntrlIcon"/>
					
					<img src="img/repeat.png" class="lft cntrlIcon"/>
					<img src="img/shuffle.png" class="lft cntrlIcon"/>
				</div>
			<!--End of Advance player control-->
			<div class="dividerDiv lft">
				<img src="img/playerDivider.png" class="lft divider"/>
			</div>
			<!-- Album info-->
				<div class="lft playingInfo" >
					<div class="lft" id="currentThumnail">
						<img src="http://i1.ytimg.com/vi/TGtWWb9emYI/default.jpg" height="52px" width="100%"/>
					</div>
					<div class="lft" id="songTitle">
					fifa
					</div>
				</div>
			<!--End of Album Indo-->
			<!--Start of seekbar-->
				<div class="lft" id="seekbar" >
					
						<input class="lft" type='range' id='seek' title="Seek" min='0' max='0' step='1' value='0'></input>
					
			
					
				</div>
				<div class="durationTime lft cntrlIcon">
						<span id='currentTime'>0:00</span> / <span id='duration'>0:00</span>
				</div>
		<!--Start of seekbar-->
		<div class="dividerDiv lft">
				<img src="img/playerDivider.png" class="lft divider"/>
		</div>
			<!--Start of Social media-->
				<div class="lft socialNet">
					<img src="img/like.png" id = "like" class="lft cntrlIcon"/>
					<img id="commentIcon" src="img/comments.png" class="lft cntrlIcon"/>
					<img id="share" src="img/share.png" class="lft cntrlIcon"/>
					<img id="moodIcon" src="img/mood.png" class="lft cntrlIcon"/>
					<div class="dropdownContain" id="menu" style="display:none;">
                        <div class="dropOut"  >
                            <div class="triangle" ></div>
							<div>
								<img src="img/sad_icon.png" data-name="sad" />
								<img data-name="sleepy" src="img/sleepy_icon.png"/>
								<img data-name="happy" src="img/happy_icon.png"/>
								<img data-name="rock" src="img/rock_icon.png"/>                            
							</div>
						</div>
					</div>
					<div class="dropdownContain" id="statusText" style="display:none;">
                        <div class="dropOut"  >
                            <div class="triangle" ></div>
							<div>
								    <textarea id="userStatus" placeholder="Enter status "></textarea>                       
							</div>
						</div>
					</div>
					<!--Comments-->
	<div class="dropdownContain" id="commentText" style="display:none;">
                        <div class="dropOut"  >
                            <div class="triangle" ></div>
							<div>
								    <textarea id="userStatus" placeholder="Enter status "></textarea>                       
							</div>
						</div>
					</div>
	
				<img src="img/openplayList.png" id="openDrp"/>
			</div>
			<!--End of Social media-->
				<div class="clearfix"></div>
				
			</div>
			<div id="drpArea"style="color:#fff">
				<div id="noRecord">Please Drop Here</div>
			
			</div>
		</div>
	<!--End of drag-able play area-->
	<!--Start of side menu-->
		<div id="sideMenu">
		<div>
		<!--Mood Based Songs -->
			<div>
				<div class="grpTitle">News Feeds</div>
				<div class="musicList">
					<ul id="moodSongs">
					</ul>
					
				</div>
			</div>
		<!--End of Mood Based Songs -->
		<!--Top Trending Songs-->
			<div>
				<div class="grpTitle">Trending Songs </div>
				<div class="musicList">
					<ul id="trendSongs">
						
						
					</ul>
				</div>
			</div>
		<!--End of Top Trending Songs-->
		</div>
		</div>
	
	<!--End of side menu-->
	
	<script>
	(function($){
			$(window).load(function(){
				$(".musicList").mCustomScrollbar({
					autoHideScrollbar:true,
					theme:"light-thin"
				});
				//$("#sideMenu").hide();
			});
		})(jQuery);
	
		
	</script>
	</body>
</html>
