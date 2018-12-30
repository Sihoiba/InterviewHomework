package sihoiba.interviewHomework.persistence;

import org.springframework.data.repository.CrudRepository;
import sihoiba.interviewHomework.model.Video;

import java.util.List;
import java.util.Optional;

/**
 * JPA Repository for the Videos table. Uses SpringBoot JPA to auto generate the behaviour at runtime.
 */
public interface VideosRepository extends CrudRepository<Video, Long> {

    Video save( Video video );

    Optional<Video> findById( Long id );

    List<Video> findAll();

    List<Video> findByTitleContainingIgnoreCase( String title );

    void deleteById( Long id );

}
