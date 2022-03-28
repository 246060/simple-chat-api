package xyz.jocn.chat.participant.repo;

import java.util.List;

import xyz.jocn.chat.participant.dto.ParticipantDto;

public interface ParticipantRepositoryExt {

	List<ParticipantDto> findCurrentParticipantsInChannel(long channelId);

}
