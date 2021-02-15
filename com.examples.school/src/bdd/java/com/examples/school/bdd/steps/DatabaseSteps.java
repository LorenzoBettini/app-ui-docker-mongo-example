package com.examples.school.bdd.steps;

import java.util.List;

import org.bson.Document;

import com.examples.school.bdd.SchoolSwingAppBDD;
import com.mongodb.MongoClient;
import com.mongodb.client.model.Filters;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;

public class DatabaseSteps {

	static final String DB_NAME = "test-db";
	static final String COLLECTION_NAME = "test-collection";

	static final String STUDENT_FIXTURE_1_ID = "1";
	static final String STUDENT_FIXTURE_1_NAME = "first student";
	static final String STUDENT_FIXTURE_2_ID = "2";
	static final String STUDENT_FIXTURE_2_NAME = "second student";

	private MongoClient mongoClient;

	@Before
	public void setUp() {
		mongoClient = new MongoClient("localhost", SchoolSwingAppBDD.mongoPort);
		// always start with an empty database
		mongoClient.getDatabase(DB_NAME).drop();
	}

	@After
	public void tearDown() {
		mongoClient.close();
	}

	@Given("The database contains the students with the following values")
	public void the_database_contains_the_students_with_the_following_values(
			List<List<String>> values) {
		values.forEach(
			v -> addTestStudentToDatabase(v.get(0), v.get(1))
		);
	}

	@Given("The database contains a few students")
	public void the_database_contains_a_few_students() {
		addTestStudentToDatabase(STUDENT_FIXTURE_1_ID, STUDENT_FIXTURE_1_NAME);
		addTestStudentToDatabase(STUDENT_FIXTURE_2_ID, STUDENT_FIXTURE_2_NAME);
	}

	@Given("The student is in the meantime removed from the database")
	public void the_student_is_in_the_meantime_removed_from_the_database() {
		mongoClient
			.getDatabase(DB_NAME)
			.getCollection(COLLECTION_NAME)
			.deleteOne(Filters.eq("id", STUDENT_FIXTURE_1_ID));
	}

	private void addTestStudentToDatabase(String id, String name) {
		mongoClient
			.getDatabase(DB_NAME)
			.getCollection(COLLECTION_NAME)
			.insertOne(
				new Document()
					.append("id", id)
					.append("name", name));
	}

}
