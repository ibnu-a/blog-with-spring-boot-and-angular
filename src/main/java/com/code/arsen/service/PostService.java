package com.code.arsen.service;

import com.code.arsen.dto.PostDto;
import com.code.arsen.entity.Post;
import com.code.arsen.exception.PostNotFoundException;
import com.code.arsen.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    private AuthService authService;
    @Autowired
    private PostRepository postRepository;

    public void createPost(PostDto postDto) {
        Post post = mapFromDtoToPost(postDto);
        postRepository.save(post);
    }

    public List<Post> showAllPost() {
        return postRepository.findAll();
    }

    public Post readSinglePost(Long id) {
        return postRepository.findById(id).get();
    }

    public List<PostDto> showAllData() {
        List<Post> posts = postRepository.findAll();
        return posts.stream().map(this::mapFromPostToDto).collect(Collectors.toList());
    }

    private PostDto mapFromPostToDto(Post post) {
        PostDto postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setTitle(post.getTitle());
        postDto.setContent(post.getContent());
        postDto.setUsername(post.getUsername());
        return postDto;
    }

    private Post mapFromDtoToPost(PostDto postDto) {
        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        User username = authService.getCurrentUser().orElseThrow(() -> new IllegalArgumentException("No User logged in"));
        post.setUsername(username.getUsername());
        post.setCreatedOn(Instant.now());
        return post;
    }

    public PostDto readSingleData(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException("Not found Id " + id));
        return mapFromPostToDto(post);
    }
}
