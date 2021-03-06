package com.c09.cinpockema.movie.entities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;


import com.c09.cinpockema.cinema.entities.Cinema;
import com.c09.cinpockema.product.entities.Screening;
import com.c09.cinpockema.user.entities.User;

import org.hibernate.validator.constraints.Range;
import org.neo4j.cypher.internal.compiler.v2_2.commands.indexQuery;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
public class Movie {

	@Id
	@NotNull
//	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;

	@NotNull
	@Column(nullable=false)
	private String title;

	private String originalTitle;

	@NotNull
	@Column(nullable=false)
	private double rating;

	private String genres;

	private String imageUrl;
	
	
	private boolean onShow;
	
	public boolean isOnShow() {
		return onShow;
	}

	public void setOnShow(boolean onShow) {
		this.onShow = onShow;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getOriginalTitle() {
		return originalTitle;
	}

	public void setOriginalTitle(String originalTitle) {
		this.originalTitle = originalTitle;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public String getGenres() {
		return genres;
	}

	public void setGenres(String genres) {
		this.genres = genres;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}


	@OneToMany(cascade = CascadeType.ALL, mappedBy = "movie", fetch = FetchType.LAZY)    
	List<MovieComment> movieComments = new ArrayList<MovieComment>();

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@JsonBackReference
	public List<MovieComment> getMovieComments() {
		return movieComments;
	}

	public void setMovieComments(List<MovieComment> movieComments) {
		this.movieComments = movieComments;
	}

	public void addMovieComment(MovieComment movieComment) {
		movieComment.setMovie(this);
		movieComments.add(movieComment);
	}
	
	@ManyToMany(mappedBy="movies", fetch=FetchType.LAZY)
    private List<Cinema> cinemas = new ArrayList<Cinema>();
	
	@ManyToMany(cascade = { CascadeType.MERGE,CascadeType.REFRESH })
    @JoinTable(name="user_movie",
    	joinColumns={@JoinColumn(name="movie_id")},
    	inverseJoinColumns={@JoinColumn(name="user_id")})
    private List<User> users = new ArrayList<User>();
	
	@JsonBackReference
	public List<User> getUsers() {
		return users;
	}

	@JsonIgnore
	public void setUsers(List<User> users) {
		this.users = users;
	}

	public void addUser(User user) {
		for (User _user: users) {
			if (_user.getId() == user.getId()) {
				return;
			}
		}
		this.users.add(user);
	}

	public void removeUser(User user) {
		for (int i = 0 ; i < users.size() ; i++) {
			if (users.get(i).getId() == user.getId()) {
				users.remove(i);
				return;
			}
		}
	}
	
	@JsonBackReference
	public List<Cinema> getCinemas() {
		return cinemas;
	}

	@JsonIgnore
	public void setCinemas(List<Cinema> cinemas) {
		this.cinemas = cinemas;
	}
	
	public void addCinema(Cinema cinema) {
		for (Cinema _cCinema: cinemas) {
			if (_cCinema.getId() == cinema.getId()) {
				return;
			}
		}
		cinemas.add(cinema);
	}
	
	/*
	 * 新增Screening List，和Screening形成一对多关系
	 */
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "movie", fetch = FetchType.LAZY)    
	List<Screening> screenings = new ArrayList<Screening>();
	
	@JsonBackReference
	public List<Screening> getScreenings() {
		return screenings;
	}

	@JsonIgnore
	public void setScreenings(List<Screening> screenings) {
		this.screenings = screenings;
	}
	
	public void addScreening(Screening screening) {
		screenings.add(screening);
	}
}
