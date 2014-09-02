package org.magnum.mobilecloud.video.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface VideoRepository extends CrudRepository<Video, Long> {
	/**
	 * Given an ID, find the video
	 * 
	 * */
	Video findById(long id);

	/**
	 * Given a name, find the video
	 * */
	List<Video> findByName(String name);

	/**
	 * Return a list of objects with a duration less than passed-in duration
	 * value
	 * */
	
	List<Video> findDurationLessThan(long duration);

}
