var currentMood="happy";
var videos = [];
var videosDetails = [];
var videosTrends = [];
var currentId;
var currentPostId;
var myPlayList=[];
videos.push("9W3sWiZ-iO8");
//videos.push("WTJSt4wP2ME");
//videos.push("j5-yKhDd64s");
//videos.push("8UVNT4wvIGY");
//videos.push("hT_nvWreIhg");
//videos.push("mWRsgZuwf_8");
//videos.push("mk48xRzuNvA");
//videos.push("bxV-OOIamyk");
//videos.push("kn6-c223DUU");
var videoHeight;
var windowHeight;
var headerHeight;
var currentIndex = 0;
var DEFAULT_VIDEO_ID = '9W3sWiZ-iO8';
var seekBarInterval = null;

$(document).ready(function() {
  //disable('loadFeed', 'previous', 'pause', 'play', 'next', 'volume', 'seek');
  windowHeight=$(window).height();
  headerHeight=$("header").outerHeight(true);
  getFeed('sleepy');
  var tag = document.createElement('script');
  tag.src = 'http://www.youtube.com/player_api';
  var firstScriptTag = document.getElementsByTagName('script')[0];
  firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);
  videoHeight=windowHeight-headerHeight-$(".drgPlay").outerHeight(true);
  $("#sideMenu").height(windowHeight-headerHeight);
  var subHeight=($("#sideMenu").height()/2)-($('.grpTitle').outerHeight(true)*2);
  if($(window).width()<=480)
  {
	$("#sideMenu").height( videoHeight);
	//alert(subHeight+(subHeight/7));
	$(".musicList").css('max-height','150px');
  }
  else
  {
	$("#sideMenu").height(windowHeight-headerHeight);
	$(".musicList").css('max-height',subHeight+(subHeight/7));
  }
  //$("#sideMenu").hide();
  //alert($(".drPlay").outerHeight(true));
  getTopTrendFeed();
  $("#volIcon").click(function(event){
  $("#volume").show();
  event.stopPropagation();
  });
  $("body").click(function(){
	$("#volume").hide();
  });
   $("#view_list_Sel").click(function(){
	$("#view_list").show();
	$("#view_list_Sel").hide();
	$(".logo").attr('style','color:#fff;text-shadow: 4px 4px 4px #aaa');
	$(".socialNet").show();
	$("#sideMenu").animate({ left: '-2000px' });
	//$("#sideMenu").animate({ width: 'hide' });
	$(".drgPlay").width("100%");
	$("#sideMenu").css('top','-20000px');
	
	
  });
   $("#view_list").click(function(){
	$("#view_list_Sel").show();
	$("#view_list").hide();
	$(".logo").attr('style','color:#f9b03d;text-shadow: 4px 4px 4px #f9b03d');
	$(".socialNet").hide();
	$(".drgPlay").width("74%");
	//$("#sideMenu").css('left','0px');
	$("#sideMenu").css('top',headerHeight);
	$("#sideMenu").animate({ left: '0px' });
	
  });
  $('#openDrp').click(function(){
	if($("#drpArea").is(":visible"))
	{
		$(this).removeClass('imgRotate');
		$(this).css('-moz-transform','rotate(180deg)');
		$("#drpArea").animate({ height: 'hide' });		
	}
	else{
		$(this).addClass('imgRotate');
		$("#drpArea").animate({ height: 'show' });		
	}
  
  });
 $(".musicList").on('dragstart', 'img', function(e)
{

	var imgSrc=$(this).attr('src');
	var id=$(this).attr('data-vidid');
	var postid=$(this).attr('data-postid');
	var img = document.createElement("img");
	//var img = $("<img/>").attr('src',imgSrc);
  //alert(img);
    img.src = imgSrc;
	//img.height = "30";
	//img.width = "30";
	img.style.width = "30px !important";
	img.style.height = "30px !important";
	//img.className = "imgSet";
    e.originalEvent.dataTransfer.setDragImage(img, 0, 0);
	e.originalEvent.dataTransfer.setData("source", "<img class='drgged plImg' data-vidid='"+id+"' draggable='true' src='"+imgSrc+"'/>"); 

});
$(document).on('dragstart','img.drgged',function(e){
	//alert($(this).addClass("remove"));
	//$( ".drgPlay #drpArea" ).index( this ));

});
$(document).on('dragstart','img.drgged',function(e){
	alert($(this).addClass("remove"));
	//$( ".drgPlay #drpArea" ).index( this ));

});

$('.drgPlay').bind('dragover', function(e)
{
 $('#drpArea').show();

});
 $('.drgPlay').on('drop',function(e){
   var source = e.originalEvent.dataTransfer.getData('Text');
        e.originalEvent.stopPropagation();
        e.originalEvent.preventDefault();
		$("#noRecord").hide();
		
        $("#drpArea").append(e.originalEvent.dataTransfer.getData('source'));
        
    }).on('dragover', function (e) {
      e.preventDefault();
    });


/*$(document).on('drop', '#drpArea', function(e)
{
alert('sss');
});*/
 $(document).on('click','img.plImg',function(){

	 currentId=$(this).attr('data-vidid');
	 currentPostId=$(this).attr("data-postid");

	 player.loadVideoById(currentId);
	 for(var i=0;i<videosDetails.length;i++){
		 if(videosDetails[i]['videoid']==currentId){
			 $("#currentThumnail img").attr("src",videosDetails[i]['thumbnail3']);
			  $("#songTitle").html(videosDetails[i]['title']);
			//alert(currentVideo.videosDetails[i]['title']);
		 }
	 }
	 
 
	 
	 
 });
 $('.musicList').on('click','li',function(){
	 currentId=$(this).find("img").attr('data-vidid');
	 currentPostId=$(this).find("img").attr("data-postid");

	 player.loadVideoById(currentId);
	 for(var i=0;i<videosDetails.length;i++){
		 if(videosDetails[i]['videoid']==currentId){
			 $("#currentThumnail img").attr("src",videosDetails[i]['thumbnail3']);
			  $("#songTitle").html(videosDetails[i]['title']);
			//alert(currentVideo.videosDetails[i]['title']);
		 }
	 }
	 
 });
$('#moodIcon').click(function(){
	var p=$(this).position();
	$( "#menu" ).toggle();
	//$("#menu").css();
	$("#menu").css('top',p.top-"30");
	$("#menu").css('left',p.left);
});
$('#menu img').click(function(){
	//alert($(this).attr('data-name'));
	videosDetails = [];
	//alert(videosDetails.length);
currentMood=$(this).attr('data-name');
$( "#menu" ).hide();
$("#moodSongs").empty();
getFeed(currentMood);
$( "#view_list" ).trigger( "click" );

});
/*Comment*/

$('#commentIcon').click(function(){
	//$( ".commentBox").toggle();
	if($("#commentText").is(":visible"))
	{
		$("#commentText").hide();
	
	}
	else
	{
		var p=$(this).position();
		$("#commentText").show();
		$("#commentText").css('top',p.top-"30");
		$("#commentText").css('left',p.left);
		//$(".commentBox").height(videoHeight);
		//$(".commentBox").css('top',headerHeight);
		
	}
});
$(document).on('keypress', '#userCom', function(e)
		{
		if (e.which == 13)
		{
		var x=$.trim($('#userCom').val());
		//alert(x);
		$(".commentBox").hide();
		$.ajax({
			url: "post",
	        type: "get",
	        data:{'mood':currentMood, 'post_id': currentPostId, 'post_msg': x },
	        
	        success: function() 
	        {
	        	
	        }
		});
		}});


$('#like').click(function(){
	
	$.ajax({
		url: "LikeServlet",
        type: "post",
        data:{'post_id':currentPostId, 'mood':currentMood, 'URL':currentId},
        
        success: function() 
        {
        }});
});
$('#share').click(function(){
	
	if($("#statusText").is(":visible")){
		$("#statusText").hide();
	}
	else{
		var p =$("#share").position();
		$("#userStatus").css('width','99%');
		$("#statusText").css('top',p.top-"30");
		$("#statusText").css('left',p.left);
		
		$("#statusText").show();
	}
	/*$.ajax({
		url:"SharePost",
        type: "post",
        data:{'post_id':currentPostId, 'mood':currentMood, 'URL':currentId},
        
        success: function() 
        {
        }});*/
});
$(document).on('keypress', '#userStatus', function(e)
		{
		if (e.which == 13)
		{
		var x=$.trim($('#userStatus').val());
		$.ajax({
			url:"PostComment",
	        type: "get",
	        data:{'post_id':currentPostId, 'mood':currentMood, 'URL':currentId,'msg':x},
	        
	        success: function() 
	        {
	        }});
		
		}
		$('#userStatus').hide();
		});

$("#proPic").click(function(){
	
	if($("#statusText").is(":visible")){
		$("#signOut").hide();
	}
	else{
		var p =$(this).position();
		$("#signOut").show();
		
		$("#signOut").css('top',p.top+"30");
		$("#signOut").css('left',p.left);
		
		
	}
	
	
});
$("#signOut").click(function(){
	$(this).hide();
	$.ajax({
	    url: 'logout',
	    type: "get",
	    success: function() {
	    }
	    });
	
	//alert("signout");
	
});
});

function getTopTrendFeed(){
	$.ajax({
    dataType: 'jsonp',
    url: 'http://gdata.youtube.com/feeds/api/standardfeeds/most_popular_Music?v=2&max-results=10&format=5&alt=json',
    success: function(response) {
	
	if(response.feed && response.feed.entry){
		$.each(response.feed.entry, function(index, entry) {
		//alert(entry['title']['$t']);
		//alert(entry['media$group']['yt$videoid']['$t']);
			videosTrends.push({
				'title': entry['title']['$t'],
				'thumbnail1': entry['media$group']['media$thumbnail'][0]['url'],
				'thumbnail2': entry['media$group']['media$thumbnail'][1]['url'],
				'thumbnail3': entry['media$group']['media$thumbnail'][2]['url'],
				'videoid': entry['media$group']['yt$videoid']['$t'],
			});
			$("#trendSongs").append("<li><img class='plImg' draggable='true' data-vidid="+entry['media$group']['yt$videoid']['$t']+" src='"+entry['media$group']['media$thumbnail'][2]['url']+"'/><div class='title'>"+entry['title']['$t']+"</div><div class='clearfix'></div></li>");
		});
		
	}
      /*if (response.feed && response.feed.entry) {
        $.each(response.feed.entry, function(index, entry) {
		alert(entry['title']['$t']);
		alert(entry['media$group']['yt$videoid']['$t']);
          videos.push({
            'title': entry['title']['$t'],
            'thumbnail': entry['media$group']['media$thumbnail'][0]['url'],
            'videoid': entry['media$group']['yt$videoid']['$t'],
          });
        });*/
        
        
      } 
    
  });

}
function getFeed(mood){
	//alert("Entering Fnx")
	$.ajax({
		url: "post",
        type: "get",
        data:{'mood':mood},
        dataType: "JSON",
        
        success: function(data) 
        {
        	//alert(data);
        	
        	$.each(data, function(idx, obj) 
        	{
        		for(var i=0;i<obj.length;i++)
        		{
        			var vidId=obj[i].map.URL;
        				videos.push(vidId);
        			var post_id=obj[i].map.post_id;
        			var fbLikes = obj[i].map.fblikes;
        			var ytviews = obj[i].map.fblikes;
        			
	    			URL ="http://gdata.youtube.com/feeds/api/videos/"+vidId+"?v=2&alt=json";
	    			
	    			$.get(URL,function(data) {
	    				//alert(data.entry.title.$t);
	    			title=data.entry.title.$t;
	    			imgUrlMq=data.entry.media$group.media$thumbnail[1].url;
	    			imgUrlHq=data.entry.media$group.media$thumbnail[2].url;
	    			imgUrlsmall=data.entry.media$group.media$thumbnail[3].url;
	    			vidiD=data.entry.media$group.yt$videoid.$t;
	    			videosDetails.push({
	                'title': title,
	                'thumbnail1' : imgUrlMq,
	    			'thumbnail2' : imgUrlHq,
	    			'thumbnail3' : imgUrlsmall,
	                'videoid' : vidiD,
	                'fbLikes' : fbLikes,
	                'ytviews' : ytviews,
	                'post_id' : post_id
	                
	              });
	    		  //alert("qwertyu");
	    		  $("#moodSongs").append("<li draggable='true' class='videoList' ><img  class='plImg' data-postId = '"+post_id+"' data-vidid="+vidiD+" src='"+imgUrlsmall+"'/><div class='title'>"+title+"</div><div class='clearfix'></div></li>");
	    		//alert("qwertyuasdfghj");
	    			});
        	}//
        		});
        }
	});
	//$.get("post",function(data) {alert(data)});
}

function onYouTubePlayerAPIReady() {
//alert(videosDetails[0].thumbnail2);
	//$("html").css("background-image","url("+videosDetails[0].thumbnail2+")");
	//$("#currentThumnail img").attr("src",videosDetails[0].thumbnail3);
	//$("#songTitle").html(videosDetails[0].title);
  player = new YT.Player('player', {
    height: videoHeight,
    width: '100%',
    videoId: '9W3sWiZ-iO8',
    playerVars: {
	'controls': 0,
      'enablejsapi': 1,
      'html5': 1,
      'origin': window.location.host
    },
    events: {
      'onReady': onReady,
      'onStateChange': onStateChange
      /* 'onPlaybackQualityChange': onPlaybackQualityChange,
      'onError': onError */
    }
  });
    $('#pause').click(function() {
    
      player.pauseVideo();
    
  });
   $('#volume').change(function() {
    player.setVolume($(this).val());
  });
  $('#play').click(function() {
  
	if($(this).attr('src')=='img/Play.png')
	{
      player.playVideo();
	  $(this).attr('src','img/pause.png');
	}
	else{
		player.pauseVideo();
		$(this).attr('src','img/Play.png');
	}
    
  });
  $('#seek').change(function() {
    if (seekBarInterval != null) {
      clearInterval(seekBarInterval);
    }
    
    $('#currentTime').html(secondsToMmSs($(this).val()));
  });
   $('#previous').click(function() {
    playPreviousVideo(player);
  });
  
  $('#next').click(function() {
    playNextVideo(player);
  });
   $('#seek').mouseup(function() {
    player.seekTo($(this).val(), true);
    
    setSeekBarInterval();
  });
 }
 function onReady(evt) {
        evt.target.playVideo();
    }
/*Start of seekbar function*/
function secondsToMmSs(seconds) {
  var minutesValue = Math.floor(seconds / 60);
  var secondsValue = Math.floor(seconds % 60);
  if (secondsValue < 10) {
    secondsValue = '0' + secondsValue;
  }
  
  return minutesValue + ':' + secondsValue;
}

function setSeekBarInterval() {

  seekBarInterval = setInterval(function() {
    var currentTime = Math.round(player.getCurrentTime());
    $('#currentTime').html(secondsToMmSs(currentTime));
    $('#seek').val(currentTime);
  }, 1000);
}
/*End of seekbar function*/
function  onStateChange(event) {
	var player = event.target;
	/*if(evt.data==YT.PlayerState.ENDED)
	{
		playNextVideo(player);
		
	}*/
    switch (event.data) {
    case YT.PlayerState.ENDED:
      if (seekBarInterval != null) {
        clearInterval(seekBarInterval);
        seekBarInterval = null;
      }
      
      var duration = Math.round(player.getDuration());
      $('#currentTime').html(secondsToMmSs(duration));
      $('#seek').val(duration);

      //enable('play');
      //disable('pause', 'volume', 'seek');
      
      playNextVideo(player);
    break;
      
    case YT.PlayerState.PLAYING:
      if (seekBarInterval != null) {
        clearInterval(seekBarInterval);
      }
      
      setSeekBarInterval();
      
      //enable('pause', 'volume', 'seek');
      //disable('play');
      
      $('#volume').val(player.getVolume());
      
      var duration = Math.round(player.getDuration());
      $('#duration').html(secondsToMmSs(duration));
      $('#seek').attr('max', duration);
    break;
      
    case YT.PlayerState.PAUSED:
      //enable('play', 'volume', 'seek');
      //disable('pause');
      
      if (seekBarInterval != null) {
        clearInterval(seekBarInterval);
        seekBarInterval = null;
      }
    break;
      
    case YT.PlayerState.BUFFERING:
      //enable('pause', 'volume', 'seek');
      //disable('play');
      
      if (seekBarInterval != null) {
        clearInterval(seekBarInterval);
        seekBarInterval = null;
      }
    break;
      
    case YT.PlayerState.CUED:
      //enable('play');
      //disable('pause', 'volume', 'seek');
    break;
  }
}
function playPreviousVideo(player) {
  currentIndex--;
  playCurrentVideo(player);
}

function playNextVideo(player) {
  currentIndex++;
  playCurrentVideo(player);
}
/*playing the next video in queue*/
function playNextVideo(player) {

  currentIndex++;

  playCurrentVideo(player);
}
function playCurrentVideo(player) {
  if (seekBarInterval != null) {
    clearInterval(seekBarInterval);
  }
  
  $('#currentTime').html(secondsToMmSs(0));
  $('#duration').html(secondsToMmSs(0));
  
  if (videos.length > 0) {
    $('#playerDiv').show();
  
    var previousVideo = videosDetails[currentIndex - 1];
    var currentVideo = videosDetails[currentIndex];
    var nextVideo = videosDetails[currentIndex + 1];
  
    /*if (previousVideo == null) {
      $('#previousThumbnail').hide();
    } else {
      $('#previousThumbnail').show();
      $('#previousThumbnail').attr('src', previousVideo.thumbnail);
      $('#previousThumbnail').attr('title', previousVideo.title)
    }
  
    if (nextVideo == null) {
      $('#nextThumbnail').hide();
    } else {
      $('#nextThumbnail').show();
      $('#nextThumbnail').attr('src', nextVideo.thumbnail);
      $('#nextThumbnail').attr('title', nextVideo.title)
    }*/
  
    if (currentVideo != null) {
      //$('#title').html(currentVideo.title);
	  $("html").css("background-image","url("+currentVideo.thumbnail2+")");
	  $("#currentThumnail img").attr("src",currentVideo.thumbnail3);
	  $("#songTitle").html(currentVideo.title);
      player.loadVideoById(currentVideo.videoid);
      
      currentPostId = currentVideo.post_id;
      currentId = currentVideo.videoid;
      $.ajax({
  		url: "AddListeningActivity",
          type: "post",
          data:{'mood':currentMood, 'post_id' : currentPostId, "URL":currentId},
          success: function() 
          {
        	  
          }
      });
      
      /*enable('play');
      disable('pause');*/
    }
  } else {
  
    //$('#playerDiv').hide();
  }
}
/*End of playing next video in queue*/

    function stopVideo() {
        player.stopVideo();
    }
