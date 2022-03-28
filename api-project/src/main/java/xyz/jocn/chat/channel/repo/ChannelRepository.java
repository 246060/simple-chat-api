package xyz.jocn.chat.channel.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import xyz.jocn.chat.channel.ChannelEntity;

@Repository
public interface ChannelRepository extends JpaRepository<ChannelEntity, Long>, ChannelRepositoryExt {

}
