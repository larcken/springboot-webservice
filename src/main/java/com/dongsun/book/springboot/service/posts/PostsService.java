package com.dongsun.book.springboot.service.posts;

import com.dongsun.book.springboot.web.domain.posts.Posts;
import com.dongsun.book.springboot.web.domain.posts.PostsRepository;
import com.dongsun.book.springboot.web.dto.PostsResponseDto;
import com.dongsun.book.springboot.web.dto.PostsSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class PostsService {
    private final PostsRepository postsRepository;

    // INFO
    // Autowired 는 Bean을 주입받을때 사용, 생성자로 주입받는것을 더 권장
    // 생성자는 lombok 에서 지원하는 @RequiredArgsConstructor 을 통해 생성자 코드를 자동으로 생성한다.
    // 그렇게 되면 코드가 변경될때마다 자동으로 생성해주기 때문에 편리

    @Transactional
    public Long save(PostsSaveRequestDto requestDto) {
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, PostsSaveRequestDto requestDto) {
        Posts posts = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(("해당 사용자가 없습니다. id=" + id)));
        posts.update(requestDto.getTitle(), requestDto.getContent());
        return id;
    }

    public PostsResponseDto findById(Long id) {
        Posts entity = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(("해당 사용자가 없습니다. id=" + id)));
        return new PostsResponseDto(entity);
    }
}
