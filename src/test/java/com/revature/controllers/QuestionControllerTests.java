package com.revature.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.revature.controller.QuestionController;
import com.revature.entities.Answer;
import com.revature.entities.Question;
import com.revature.services.QuestionService;


@RunWith(SpringRunner.class)
@WebMvcTest(QuestionController.class)
public class QuestionControllerTests {

    @Autowired 
    private ObjectMapper mapper;
    
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private QuestionService questionService;
	
	@Test
	public void testGetAllQuestionsHappyPath() throws Exception {
		
		// Create page of data
		List<Question> questions = new ArrayList<>();
		questions.add(new Question(1,1,"title","content", LocalDate.MIN, LocalDate.MIN, true, 1));
		Page<Question> pageResult = new PageImpl<>(questions);
		
		// Stub getAllQuestions to return page of data
		when(questionService.getAllQuestions(Mockito.any(Pageable.class))).thenReturn(pageResult);
		
		// Call API end point and assert result
		mvc.perform(get("/questions")
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content()
					.contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.content[0].title", is("title")));
			
	}
	
	/* @Author ken */
	@Test
	public void testGetAllQuestionsByUserId() throws Exception {
		// Create page of data
		List<Question> questions = new ArrayList<>();
		questions.add(new Question(1,1,"title","content", LocalDate.MIN, LocalDate.MIN, true, 1));
		Page<Question> pageResult = new PageImpl<>(questions);
		
		// Stub getAllQuestions to return page of data
		when(questionService.getAllQuestionsByUserId(Mockito.any(Pageable.class), Mockito.anyInt())).thenReturn(pageResult);
		
		// Call API end point and assert result
		mvc.perform(get("/questions/user/1")
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content()
					.contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.content[0].id", is(1)));
			
	}
	
	/* @Author ken 	*/
	@Test
	public void testGetAllQuestionsByStatus() throws Exception {
		
		// Create page of data
		List<Question> questions = new ArrayList<>();
		questions.add(new Question(1,1,"title", "content", LocalDate.MIN, LocalDate.MIN, false, 1));
		Page<Question> pageResult = new PageImpl<>(questions);
		
		// Stub getAllQuestions to return page of data
		when(questionService.getAllQuestionsByStatus(Mockito.any(Pageable.class), Mockito.anyBoolean())).thenReturn(pageResult);
		
		// Call API end point and assert result
		mvc.perform(get("/questions/status/false")
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content()
					.contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.content[0].id", is(1)));
			
	}
	
	/**@author James */
	@Test
    public void updateStatus() throws Exception {
        Question questions, testQuestions;
        questions = new Question(1,1,"title","content", LocalDate.MIN, LocalDate.MIN, false, 1);
        testQuestions = new Question(1,1,"title","content", LocalDate.MIN, LocalDate.MIN, true, 1);

        when(questionService.updateQuestionStatus(Mockito.any(Question.class), Mockito.anyInt())).thenReturn(testQuestions);

        String toUpdate = mapper.writeValueAsString(questions);
        org.springframework.test.web.servlet.MvcResult result = mvc.perform(MockMvcRequestBuilders.put("/questions/status")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(toUpdate)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                ).andReturn();

        String content = result.getResponse().getContentAsString();
        assertEquals(200, result.getResponse().getStatus());
       // assertTrue("This return object conains the string", content.contains("true"));
       // assertNotEquals(null, content);

    }

	/**@author James */
    @Test
    public void updateQuestionAcceptedAnswerId() throws Exception {
        Question questions, testQuestions;
        questions = new Question(1,1,"title","content", LocalDate.MIN, LocalDate.MIN, false, 1);
        testQuestions = new Question(1,1,"title","content", LocalDate.MIN, LocalDate.MIN, true, 1);

        when(questionService.updateQuestionAcceptedAnswerId(Mockito.any(Question.class))).thenReturn(testQuestions);

        String toUpdate = mapper.writeValueAsString(questions);
        MvcResult result = mvc.perform(MockMvcRequestBuilders.put("/questions")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(toUpdate)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                ).andReturn();

        String content = result.getResponse().getContentAsString();
        assertEquals(200, result.getResponse().getStatus());
        assertTrue("This return object conains the string", content.contains("true"));
        assertNotEquals(null, content);

    }
    
	/* @Author ken 	*/
	@Test
	public void testGetQuestionByQuestionId() throws Exception {
		
		// Create page of data
		Question question = new Question(1,1,"title", "content", LocalDate.MIN, LocalDate.MIN, false, 1);
		//Page<Question> pageResult = new PageImpl<>(question);
		
		// Stub getAllQuestions to return page of data
		when(questionService.findById( Mockito.anyInt())).thenReturn(question);
		
		// Call API end point and assert result
		mvc.perform(get("/questions/id/1")
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content()
					.contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
	}
	
	/** @author ken */
	@Test
	public void testSaveQuestion() throws Exception {
		Question question = new Question(1,1,"title","content", LocalDate.MIN, LocalDate.MIN, true, 1);

		when(questionService.save(Mockito.any(Question.class))).thenReturn(question);
		
        String toUpdate = mapper.writeValueAsString(question);
        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/questions")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(toUpdate)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                ).andReturn();
        String content = result.getResponse().getContentAsString();
        System.out.println("result = " + content);
        assertEquals(200, result.getResponse().getStatus());
        assertTrue("This return object conains the string", content.contains("content"));
        assertNotEquals(null, content);
	}
    
}
