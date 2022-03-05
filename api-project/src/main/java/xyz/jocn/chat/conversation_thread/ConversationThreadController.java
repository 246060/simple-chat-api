package xyz.jocn.chat.conversation_thread;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/rooms/{roomId}/threads")
@RestController
public class ConversationThreadController {
}
