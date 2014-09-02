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

package org.magnum.mobilecloud.video;

import java.util.Collection;

import org.magnum.mobilecloud.video.repository.Video;
import org.magnum.mobilecloud.video.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;

@Controller
public class VideoServiceController {
	public static final String VIDEO_PATH = "/video";
	public static final String SEARCH_PATH = "/search";

	@Autowired
	private VideoRepository videoRepository;

	/**
	 * Returns the list of videos that have been added to the server as JSON
	 * */
	@RequestMapping(value = VIDEO_PATH, method = RequestMethod.GET)
	public @ResponseBody
	Collection<Video> getVideos() {
		return Lists.newArrayList(videoRepository.findAll());
	}

	/**
	 * Save the video metadata provided by the client and returns the saved
	 * video represented as JSON
	 * */
	@RequestMapping(value = VIDEO_PATH, method = RequestMethod.POST)
	public @ResponseBody Video addVideoMetadata(@RequestBody Video video) {
		return videoRepository.save(video);
	}

	@RequestMapping(value = "/go", method = RequestMethod.GET)
	public @ResponseBody
	String goodLuck() {
		return "Good Luck!";
	}

}
