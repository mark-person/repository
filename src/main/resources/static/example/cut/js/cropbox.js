/**
 * Created by ezgoing on 14/9/2014.
 */

"use strict";
(function (factory) {
    if (typeof define === 'function' && define.amd) {
        define(['jquery'], factory);
    } else {
        factory(jQuery);
    }
}(function ($) {
    var cropbox = function(options, el){
        var el = el || $(options.imageBox),
            obj =
            {
                state : {},
                ratio : 1,
                options : options,
                imageBox : el,
                thumbBox : el.find(options.thumbBox),
                spinner : el.find(options.spinner),
                image : new Image(),
                getDataURL: function ()
                {
                    var width = this.thumbBox.width(),
                        height = this.thumbBox.height(),
                        canvas = document.createElement("canvas"),
                        dim = el.css('background-position').split(' '),
                        size = el.css('background-size').split(' '),
                        dx = parseInt(dim[0]) - el.width()/2 + width/2,
                        dy = parseInt(dim[1]) - el.height()/2 + height/2,
                        dw = parseInt(size[0]),
                        dh = parseInt(size[1]),
                        sh = parseInt(this.image.height),
                        sw = parseInt(this.image.width);

                    canvas.width = width;
                    canvas.height = height;
                    var context = canvas.getContext("2d");
                    
                    /**
                     * img  所要绘制的图像元素
                     * x    该元素绘制的起点x坐标 （即img左上角x坐标）
                     * y    该元素绘制的起点y坐标 （即img左上角y坐标）
                     * width  所要绘制该元素的最长宽度（即画布上允许图像出现的最长宽度）
                     * height 所要绘制该元素的最大高度（即画布上允许图像出现的最大高度）
                     * 
                     * s 相关参数都以原始图片的宽高来参照的：如原始规格长：365、高：549
                     * sx   指定该元素从什么位置开始裁剪的x坐标
                     * sy   指定该元素从什么位置开始裁剪的y坐标
                     * swidth  	规定所裁剪图像的长度（即裁剪从sx点开始，向右延伸swidth的长度裁剪）
                     * sheight	规定所裁剪图像的高度（即裁剪从sy点开始，向下延伸sheight的长度裁剪）
                     */
                    context.drawImage(this.image, 0, 0, sw, sh, dx, dy, dw, dh);
                    
                    
                    
                    /*
                    context.beginPath();
                    // context.arc(x,y,radius,starAngle,endAngle,anticlockwise);
                    // 设置虚线4-2-4-2排列
                    context.setLineDash([4,2]);
                    context.arc(100, 100, 20, 0, Math.PI*2, true);
                    context.stroke();
                    context.closePath();
                    //context.fillStyle = "rgba(255,0,0,0.25)";
                    */
                    
                    
                    
                    
                    var imageData = canvas.toDataURL('image/png');
                    return imageData;
                },
                getBlob: function()
                {
                    var imageData = this.getDataURL();
                    var b64 = imageData.replace('data:image/png;base64,','');
                    var binary = atob(b64);
                    var array = [];
                    for (var i = 0; i < binary.length; i++) {
                        array.push(binary.charCodeAt(i));
                    }
                    return  new Blob([new Uint8Array(array)], {type: 'image/png'});
                },
                zoomIn: function ()
                {
                    this.ratio*=1.1;
                    setBackground();
                    
                    var bg = cropper.imageBox.css('background-position').split(' '); 
                    currentBgX = parseInt(bg[0]);
                    currentBgY = parseInt(bg[1]);
                    
                },
                zoomOut: function ()
                {
                    this.ratio*=0.9;
                    setBackground();
                    
                    var bg = cropper.imageBox.css('background-position').split(' '); 
                    currentBgX = parseInt(bg[0]);
                    currentBgY = parseInt(bg[1]);
                }
            },
            setBackground = function()
            {
                var w =  parseInt(obj.image.width)*obj.ratio;
                var h =  parseInt(obj.image.height)*obj.ratio;

                var pw = (el.width() - w) / 2;
                var ph = (el.height() - h) / 2;

                el.css({
                    'background-image': 'url(' + obj.image.src + ')',
                    'background-size': w +'px ' + h + 'px',
                    'background-position': pw + 'px ' + ph + 'px',
                    'background-repeat': 'no-repeat'});
            },
            imgMouseDown = function(e)
            {
                e.stopImmediatePropagation();

                obj.state.dragable = true;
                obj.state.mouseX = e.clientX;
                obj.state.mouseY = e.clientY;
            },
            imgMouseMove = function(e)
            {
                e.stopImmediatePropagation();

                if (obj.state.dragable)
                {
                    var x = e.clientX - obj.state.mouseX;
                    var y = e.clientY - obj.state.mouseY;

                    var bg = el.css('background-position').split(' ');

                    var bgX = x + parseInt(bg[0]);
                    var bgY = y + parseInt(bg[1]);

                    el.css('background-position', bgX +'px ' + bgY + 'px');

                    obj.state.mouseX = e.clientX;
                    obj.state.mouseY = e.clientY;
                    
                    
                }
            },
            imgMouseUp = function(e)
            {
                e.stopImmediatePropagation();
                obj.state.dragable = false;
            },
            zoomImage = function(e)
            {
                e.originalEvent.wheelDelta > 0 || e.originalEvent.detail < 0 ? obj.ratio*=1.1 : obj.ratio*=0.9;
                setBackground();
                
                var bg = cropper.imageBox.css('background-position').split(' '); 
                currentBgX = parseInt(bg[0]);
                currentBgY = parseInt(bg[1]);
            }

        obj.spinner.show();
        obj.image.onload = function() {
        	
            
        	
            obj.spinner.hide();
            setBackground();
            
            if (isNewImage) {
        		var bg = cropper.imageBox.css('background-position').split(' ');
        		currentBgX = parseInt(bg[0]);
        		currentBgY = parseInt(bg[1]);
        		isNewImage = false;
        	}
        	cropper.imageBox.css('background-position', currentBgX +'px ' + currentBgY + 'px');
        	
            

            el.bind('mousedown', imgMouseDown);
            el.bind('mousemove', imgMouseMove);
            $(window).bind('mouseup', imgMouseUp);
            el.bind('mousewheel DOMMouseScroll', zoomImage);
        };
        
        obj.image.src = options.imgSrc;
        
        
        
        
		
        el.on('remove', function(){$(window).unbind('mouseup', imgMouseUp)});

        return obj;
    };

    jQuery.fn.cropbox = function(options){
        return new cropbox(options, this);
    };
}));
