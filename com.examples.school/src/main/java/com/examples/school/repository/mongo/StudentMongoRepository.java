package com.examples.school.repository.mongo;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bson.Document;

import com.examples.school.model.Student;
import com.examples.school.repository.StudentRepository;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

public class StudentMongoRepository implements StudentRepository {

	private MongoCollection<Document> studentCollection;

	public StudentMongoRepository(MongoClient client, String databaseName, String collectionName) {
		studentCollection = client
			.getDatabase(databaseName)
			.getCollection(collectionName);
	}

	@Override
	public List<Student> findAll() {
		return StreamSupport.
				stream(studentCollection.find().spliterator(), false)
				.map(this::fromDocumentToStudent)
				.collect(Collectors.toList());
	}

	private Student fromDocumentToStudent(Document d) {
		return new Student(""+d.get("id"), ""+d.get("name"));
	}

	@Override
	public Student findById(String id) {
		Document d = studentCollection.find(Filters.eq("id", id)).first();
		if (d != null)
			return fromDocumentToStudent(d);
		return null;
	}

	@Override
	public void save(Student student) {
		studentCollection.insertOne(
			new Document()
				.append("id", student.getId())
				.append("name", student.getName()));
	}

	@Override
	public void delete(String id) {
		studentCollection.deleteOne(Filters.eq("id", id));
	}

}
