package xyz.jocn.chat.chat_space.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import xyz.jocn.chat.thread.ThreadController;
import xyz.jocn.chat.thread.ThreadService;

@WebMvcTest(ThreadController.class)
class ThreadControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	ThreadService threadService;
}