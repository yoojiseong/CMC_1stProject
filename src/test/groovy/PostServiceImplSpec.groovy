package cmc_demoproject.posts.Posts

import cmc_demoproject.posts.post.entity.Categories
import cmc_demoproject.posts.post.entity.Posts
import cmc_demoproject.posts.post.repository.PostRepository
import cmc_demoproject.posts.post.service.PostServiceImpl
import cmc_demoproject.posts.user.entity.Users
import spock.lang.Specification
import spock.lang.Subject

class PostServiceImplSpec extends Specification {

    def postRepository = Mock(PostRepository)

    @Subject
    def postService = new PostServiceImpl(postRepository)

    def "카테고리 이름이 주어지면 해당 카테고리의 게시글 목록을 반환한다"() {
        given: "테스트용 엔티티 설정"
        def categoryName = "정치"
        def user = Users.builder().user_id(1L).userName("홍길동").build()
        def category = Categories.builder().categoryName(categoryName).build()
        def post = Posts.builder()
                .post_id(10L)
                .title("테스트 제목")
                .content("내용")
                .users(user)
                .categories(category)
                .build()

        and: "Repository 반환값 설정"
        postRepository.findByCategoryName(categoryName) >> [post]

        when: "서비스 메서드 호출"
        def result = postService.getPostList(categoryName)

        then: "DTO 변환 결과 확인"
        result.size() == 1
        result[0].post_id == 10L
        result[0].title == "테스트 제목"
        result[0].category == "정치"
        result[0].writer.userName == "홍길동"
    }

    def "카테고리 이름이 null이거나 공백이면 전체 게시글을 조회한다"() {
        when: "카테고리 없이 호출"
        postService.getPostList(input)

        then: "findAll 메서드가 호출됨"
        1 * postRepository.findAll() >> []

        where:
        input << [null, "", "  "]
    }
}