package com.examples.school.bdd.steps;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.swing.launcher.ApplicationLauncher.application;

import java.util.List;

import javax.swing.JFrame;

import org.assertj.swing.core.BasicRobot;
import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.finder.WindowFinder;
import org.assertj.swing.fixture.FrameFixture;
import org.bson.Document;

import com.mongodb.MongoClient;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class SchoolSwingAppSteps {

	private static final String DB_NAME = "test-db";
	private static final String COLLECTION_NAME = "test-collection";

	private MongoClient mongoClient;

	private FrameFixture window;

	@Before
	public void setUp() {
		mongoClient = new MongoClient();
		// always start with an empty database
		mongoClient.getDatabase(DB_NAME).drop();
	}

	@After
	public void tearDown() {
		mongoClient.close();
		// the window might be null if the step for showing the view
		// fails or it's not executed
		if (window != null)
			window.cleanUp();
	}

	@Given("The database contains the students with the following values")
	public void the_database_contains_the_students_with_the_following_values(
			List<List<String>> values) {
		values.forEach(
			v -> mongoClient
				.getDatabase(DB_NAME)
				.getCollection(COLLECTION_NAME)
				.insertOne(
					new Document()
						.append("id", v.get(0))
						.append("name", v.get(1)))
		);
	}

	@When("The Student View is shown")
	public void the_Student_View_is_shown() {
		// start the Swing application
		application("com.examples.school.app.swing.SchoolSwingApp")
			.withArgs(
				"--db-name=" + DB_NAME,
				"--db-collection=" + COLLECTION_NAME
			)
			.start();
		// get a reference of its JFrame
		window = WindowFinder.findFrame(new GenericTypeMatcher<JFrame>(JFrame.class) {
			@Override
			protected boolean isMatching(JFrame frame) {
				return "Student View".equals(frame.getTitle()) && frame.isShowing();
			}
		}).using(BasicRobot.robotWithCurrentAwtHierarchy());
	}

	@Then("The list contains an element with id {string} and name {string}")
	public void the_list_contains_an_element_with_id_and_name(String id, String name) {
		assertThat(window.list().contents())
			.anySatisfy(e -> assertThat(e).contains(id, name));
	}

}
