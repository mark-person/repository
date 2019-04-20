(function($) {
    jQuery.fn.extend({
        html5_qrcode: function(qrcodeSuccess, qrcodeError, videoError) {
            return this.each(function() {
                var currentElem = $(this);

                var height = currentElem.height();
                var width = currentElem.width();

                if (height == null) {
                    height = 250;
                }

                if (width == null) {
                    width = 300;
                }

                var vidElem = $('<video width="' + width + 'px" height="' + height + 'px"></video>').appendTo(currentElem);
                var canvasElem = $('<canvas id="qr-canvas" width="' + (width - 2) + 'px" height="' + (height - 2) + 'px" style="display:none;"></canvas>').appendTo(currentElem);

                var video = vidElem[0];
                var canvas = canvasElem[0];
                var context = canvas.getContext('2d');
                var localMediaStream;

                var scan = function() {
                    if (localMediaStream) {
                        context.drawImage(video, 0, 0, 307, 250);

                        try {
                            qrcode.decode();
                        } catch (e) {
                            qrcodeError(e, localMediaStream);
                        }

                        $.data(currentElem[0], "timeout", setTimeout(scan, 500));

                    } else {
                        $.data(currentElem[0], "timeout", setTimeout(scan, 500));
                    }
                };//end snapshot function

                window.URL = window.URL || window.webkitURL || window.mozURL || window.msURL;
                navigator.getUserMedia = navigator.mediaDevices.getUserMedia || navigator.getUserMedia || navigator.webkitGetUserMedia || navigator.mozGetUserMedia || navigator.msGetUserMedia;

                var successCallback = function(stream) {
                	
                    video.src = (window.URL && window.URL.createObjectURL(stream)) || stream;
                    localMediaStream = stream;
                    $.data(currentElem[0], "stream", stream);

                    video.play();
                    $.data(currentElem[0], "timeout", setTimeout(scan, 1000));
                };

                // Call the getUserMedia method with our callback functions
                //alert(navigator.mediaDevices.getUserMedia)
                if (navigator.getUserMedia) {
                	
                	 navigator.mediaDevices.getUserMedia({video: true})
                     .then(function(stream) {
                         alert(1)
                     })
                     .catch(function(err) {
                    	 // iphone 需要https
                    	 /*
                    	  NotAllowedError: The request is not 
                    	  allowed by the user agent or the platform in the current context,
                    	  possibly because the user denied permission.
                    	  */
                    	 
                         alert(err);
                     });
                	 
                    
                	 /*navigator.getUserMedia({video: true}, successCallback, function(error) {
                    	alert("error")
                        videoError(error, localMediaStream);
                    });*/
                } else {
                    //console.log('Native web camera streaming (getUserMedia) not supported in this browser.');
                    // Display a friendly "sorry" message to the user
                    alert('Native web camera streaming (getUserMedia) not supported in this browser.')
                }

                qrcode.callback = function (result) {
                    qrcodeSuccess(result, localMediaStream);
                };
            }); // end of html5_qrcode
        },
        html5_qrcode_stop: function() {
            return this.each(function() {
                //stop the stream and cancel timeouts
                $(this).data('stream').getVideoTracks().forEach(function(videoTrack) {
                    videoTrack.stop();
                });

                clearTimeout($(this).data('timeout'));
            });
        }
    });
})(jQuery);
