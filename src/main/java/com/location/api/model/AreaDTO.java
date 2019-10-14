package com.location.api.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

@Component
public class AreaDTO {
	private String searchObjectType;
	private String location;
	private String locationLat;
	private String locationLng;
	private Set<SearchObjectDetailDTO> searchObjectDetail = new HashSet();
	private List<String> suggestion = new ArrayList();
	public String getSearchObjectType() {
		return searchObjectType;
	}
	public void setSearchObjectType(String searchObjectType) {
		this.searchObjectType = searchObjectType;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getLocationLat() {
		return locationLat;
	}
	public void setLocationLat(String locationLat) {
		this.locationLat = locationLat;
	}
	public String getLocationLng() {
		return locationLng;
	}
	public void setLocationLng(String locationLng) {
		this.locationLng = locationLng;
	}
		public Set<SearchObjectDetailDTO> getSearchObjectDetail() {
		return searchObjectDetail;
	}
	public void setSearchObjectDetail(Set<SearchObjectDetailDTO> searchObjectDetail) {
		this.searchObjectDetail = searchObjectDetail;
	}
	public List<String> getSuggestion() {
		return suggestion;
	}
	public void setSuggestion(List<String> suggestion) {
		this.suggestion = suggestion;
	}
	
	
	
}
