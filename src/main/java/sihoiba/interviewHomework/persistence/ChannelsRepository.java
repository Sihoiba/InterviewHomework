package sihoiba.interviewHomework.persistence;

import org.springframework.data.repository.CrudRepository;
import sihoiba.interviewHomework.model.Channel;

import java.util.List;

public interface ChannelsRepository extends CrudRepository<Channel, Long> {

    Channel save( Channel channel );

    List<Channel> findAll();
}
