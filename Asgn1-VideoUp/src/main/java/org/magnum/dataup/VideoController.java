/*
 * 
 * Copyright 2014 Jules White
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package org.magnum.dataup;

import org.springframework.stereotype.Controller;

import org.magnum.dataup.model.Video;
import org.magnum.dataup.model.VideoStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Controller
public class VideoController {

	/**
	 * You will need to create one or more Spring controllers to fulfill the
	 * requirements of the assignment. If you use this file, please rename it
	 * to something other than "AnEmptyController"
	 * 
	 * 
		 ________  ________  ________  ________          ___       ___  ___  ________  ___  __       
		|\   ____\|\   __  \|\   __  \|\   ___ \        |\  \     |\  \|\  \|\   ____\|\  \|\  \     
		\ \  \___|\ \  \|\  \ \  \|\  \ \  \_|\ \       \ \  \    \ \  \\\  \ \  \___|\ \  \/  /|_   
		 \ \  \  __\ \  \\\  \ \  \\\  \ \  \ \\ \       \ \  \    \ \  \\\  \ \  \    \ \   ___  \  
		  \ \  \|\  \ \  \\\  \ \  \\\  \ \  \_\\ \       \ \  \____\ \  \\\  \ \  \____\ \  \\ \  \ 
		   \ \_______\ \_______\ \_______\ \_______\       \ \_______\ \_______\ \_______\ \__\\ \__\
		    \|_______|\|_______|\|_______|\|_______|        \|_______|\|_______|\|_______|\|__| \|__|
                                                                                                                                                                                                                                                                        
	 * 
	 */
	
	@Autowired
	private VideoFileManager fileManager;
	
	private Map<Long,Video> videos = new HashMap<Long, Video>();
	private static final AtomicLong currentId = new AtomicLong(0L);
	
	/*
	 * Get request
	 */
	@RequestMapping(value = "/video", method = RequestMethod.GET)
	public @ResponseBody Collection<Video> getVideos() {
		Collection<Video> videoList = new ArrayList<Video>();
		for (long id: videos.keySet())
			videoList.add(videos.get(id));
		
		return videoList;
	}
	
    @RequestMapping(value = "/video/{id}/data", method = RequestMethod.GET)
    public void getVideo(@PathVariable("id") long id,
                         HttpServletResponse response) throws IOException {

        Video video = videos.get(id);

        if (video != null) {
            fileManager.copyVideoData(video, response.getOutputStream());
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
	
	/*
	 * Post request
	 */
	@RequestMapping(value = "/video", method = RequestMethod.POST)
	public @ResponseBody Video addVideoMetadata(@RequestBody Video video) {
		return save(video);
	}
	
	
    @RequestMapping(value = "/video/{id}/data", method = RequestMethod.POST)
    public @ResponseBody VideoStatus addSomeVideo(@PathVariable("id") long id,
                                                  @RequestParam("data") MultipartFile videoData,
                                                  HttpServletResponse response) throws IOException {
        if (!videos.containsKey(id)) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }

        fileManager.saveVideoData(videos.get(id), videoData.getInputStream());

        return new VideoStatus(VideoStatus.VideoState.READY);
    }
	
    
    /*
     * Helper functions: return the saved video
     */
	private Video save(Video vid) {
		if (vid.getId() == 0)
			vid.setId(currentId.incrementAndGet());
		
		vid.setDataUrl(getDataUrl(vid.getId()));		
		videos.put(vid.getId(), vid);
		
		return vid;
	}	
	
	
    private String getDataUrl(long videoId) {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        String urlLocalServer = "http://" + request.getServerName()
                + ((request.getServerPort() != 80) ? ":" + request.getServerPort() : "");

        return urlLocalServer + "/video/" + videoId + "/data";
    }
    
    
}
