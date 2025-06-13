package edu.upc.dsa.models;

import java.util.List;

public class VideoListDTO {
    private List<VideoDTO> videos;

    public VideoListDTO() {}

    public VideoListDTO(List<VideoDTO> videos) {
        this.videos = videos;
    }

    public List<VideoDTO> getVideos() {
        return videos;
    }

    public void setVideos(List<VideoDTO> videos) {
        this.videos = videos;
    }
}
