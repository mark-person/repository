
// >>>>>>>>>>>>>>>>>>>>>>img
var img = {};
var IMG_SCALE_HEIGHT = 60;
img.fileChange = function(obj) {
	this.loadImg(obj.files, obj.files.length, 0.1);
	// 重新生成一个，防change失效
	$(obj).prop("outerHTML", $(obj).prop("outerHTML"));
}

function changeOrient(tmpImg, canvas, context, orientation) {
	 var degree = 0;
	 var drawWidth = tmpImg.width;
     var drawHeight = tmpImg.height;
     switch(orientation){
    	//iphone横屏拍摄，此时home键在左侧
     	 case 3:
          degree=180;
          drawWidth=-tmpImg.width;
          drawHeight=-tmpImg.height;
          break;
      //iphone竖屏拍摄，此时home键在下方(正常拿手机的方向)
      case 6:
          canvas.width=tmpImg.height;
          canvas.height=tmpImg.width; 
          degree=90;
          drawWidth=tmpImg.width;
          drawHeight=-tmpImg.height;
          break;
      //iphone竖屏拍摄，此时home键在上方
      case 8:
          canvas.width=tmpImg.height;
          canvas.height=tmpImg.width; 
          degree=270;
          drawWidth=-tmpImg.width;
          drawHeight=tmpImg.height;
          break;
  	}
    context.rotate(degree*Math.PI/180);
    return {drawWidth:drawWidth, drawHeight:drawHeight}
}

img.loadImg = function(f, n, encoder) {
	n--;
	var reader = new FileReader();
	reader.onload = function() {
		var tmpImg = new Image();
		tmpImg.onload = function() {
			
			var canvas = document.createElement("canvas");
			canvas.width = this.width;
		    canvas.height = this.height;
		    var context = canvas.getContext("2d");
		    
		    //EXIF js 可以读取图片的元信息  https://github.com/exif-js/exif-js
		    EXIF.getData(this, function() {
		        var orient = EXIF.getTag(this, 'Orientation');
		        
		        var drawWidth = tmpImg.width;
		        var drawHeight = tmpImg.height;
		        // iphone不同方向拍摄
		        if (orient == 3 || orient == 6 || orient == 8) {
		        	var o = changeOrient(tmpImg, canvas, context, orient)
		        	drawWidth = o.drawWidth;
		        	drawHeight = o.drawHeight;
		        	context.drawImage(this, 0, 0, drawWidth, drawHeight);
		        }
		        else {
		        	canvas.width = tmpImg.width;
		            canvas.height = tmpImg.height;
		            context.drawImage(this, 0, 0, drawWidth, drawHeight, 0, 0, drawWidth, drawHeight);
		        }
		       	
			    var newSrc = canvas.toDataURL('image/jpeg', encoder);
			    
			    var jObj = $($("#imgTemplate").html());
				var imgObj = jObj.find(".upload-img");
			    imgObj.attr("src", newSrc);
				imgObj.data("mFile", dataURLtoFile(newSrc));
				$("#imgEndLi").before(jObj);
				if (n > 0) {
					img.loadImg(f, n, encoder);
				}
				else {
					img.refreshTop();
				}
		    });
		}
		tmpImg.src = this.result;
	}
	reader.readAsDataURL(f[n]);
}
function dataURLtoFile(dataurl) {
	var arr = dataurl.split(','), mime = arr[0].match(/:(.*?);/)[1],
	bstr = atob(arr[1]), n = bstr.length, u8arr = new Uint8Array(n);
	while(n--){
		u8arr[n] = bstr.charCodeAt(n);
	}
	return new Blob([u8arr], {type:mime});
}
img.resize = function(img) {
	var initWidth = img.width;
	var initHeight = img.height;
	$(img).data("data-init-width", initWidth);
	$(img).data("data-init-height", initHeight);
	$(img).data("data-init-boolean", true);
	
	if (initWidth < initHeight) {
		
		$(img).css({width:IMG_SCALE_HEIGHT * initWidth / initHeight, height:IMG_SCALE_HEIGHT});
	}
	else {
		var marginTop = (IMG_SCALE_HEIGHT - IMG_SCALE_HEIGHT * initHeight / initWidth) / 2;
		$(img).css({width:IMG_SCALE_HEIGHT, height:IMG_SCALE_HEIGHT * initHeight / initWidth, marginTop:marginTop});
	}
	$(".upload-img").show();
}

img.click = function(obj) {
	var initWidth = new Number($(obj).data("data-init-width"));
	var initHeight = new Number($(obj).data("data-init-height"));
	
	if (initWidth/initHeight > $(document.body).width()/$(document.body).height()) {
		$("#viewImg").css("margin-top", ($(window).height() - $(document.body).width()*initHeight/initWidth) / 2);
		$("#viewImg").css("width", $(document.body).width());
		$("#viewImg").css("height", $(document.body).width()*initHeight/initWidth);
	}
	else {
		$("#viewImg").css("margin-top", 0);
		$("#viewImg").css("width",  $(document.body).height()*initWidth/initHeight);
		$("#viewImg").css("height", $(document.body).height());
	}
	$("#viewImg").attr("src", obj.src);
	$("#imgBackground").css({width:$(window).width(), height:$(window).height()});
	$("#imgBackground").show();
		
}
img.refreshTop = function() {
	$(".to-top").show();
	if ($(".to-top:eq(0)").parent().prev().attr("id") == "imgLi") {
		$(".to-top:eq(0)").hide();
	}
}
img.remove = function(obj) {
	alert(1)
	$(obj).parent().remove();
	this.refreshTop();
}
img.top = function(obj) {
	$("#imgLi").after($(obj).parent());
	this.refreshTop();
}

function textToImg() {
	var kContent = $("#kContent").val();
	if (kContent) {
		var isTitleCenter = $("#titleCenter").prop("checked");
		
		var line = kContent.split("\n");
		var newKContent = [];
		for (var i = 0; i < line.length; i++) {
			var str = $.trim(line[i]);
			if (str.indexOf("#") == 0) {
				// marked的一个bug，第二#后开始少了个li
				if (i != 0 ) {
					str = '\n' + str;
				}
				
				if (isTitleCenter) {
					var last = str.lastIndexOf("#");
					newKContent.push(str.substring(0, last + 1) + " <center>" + str.substring(last + 2, str.length) + "</center>");
				}
				else {
					newKContent.push(str);
				}
			}
			else {
				newKContent.push(str);
			}
		}
		kContent = newKContent.join("\n");
		
		var html = marked(kContent);
		var iframeBody = $('#kContentIframe').contents().find('body');
		iframeBody.html(html);
		iframeBody.css({padding:"0.5rem",margin:"0rem"});
		
		var color = $("[name=background]:checked").val();
		iframeBody.css("background", "url(" + $("[name=background]:checked").parent().find("img").attr("src") + ")");
		iframeBody.css({color:color, fontSize:"0.825rem", height:iframeBody[0].scrollHeight});
		html2canvas(iframeBody[0]).then(function(canvas) {
			var url = canvas.toDataURL();
			var f = []; 
			f.push(dataURLtoFile(url));
			img.loadImg(f, 1, 0.8); 
	    });
	}
}

