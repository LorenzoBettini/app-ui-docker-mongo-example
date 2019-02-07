package com.examples.school.app.swing;

import java.awt.EventQueue;
import java.util.concurrent.Callable;

import com.examples.school.controller.SchoolController;
import com.examples.school.repository.mongo.StudentMongoRepository;
import com.examples.school.view.swing.StudentSwingView;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(mixinStandardHelpOptions = true)
public class SchoolSwingApp implements Callable<Void> {

	@Option(names = { "--mongo-host" }, description = "MongoDB host address")
	private String mongoHost = "localhost";

	@Option(names = { "--mongo-port" }, description = "MongoDB host port")
	private int mongoPort = 27017;

	@Option(names = { "--db-name" }, description = "Database name")
	private String databaseName = "school";

	@Option(names = { "--db-collection" }, description = "Collection name")
	private String collectionName = "student";

	public static void main(String[] args) {
		new CommandLine(new SchoolSwingApp()).execute(args);
	}

	@Override
	public Void call() throws Exception {
		EventQueue.invokeLater(() -> {
			try {
				StudentMongoRepository studentRepository = new StudentMongoRepository(
						new MongoClient(new ServerAddress(mongoHost, mongoPort)), databaseName, collectionName);
				StudentSwingView studentView = new StudentSwingView();
				SchoolController schoolController = new SchoolController(studentView, studentRepository);
				studentView.setSchoolController(schoolController);
				studentView.setVisible(true);
				schoolController.allStudents();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		return null;
	}

}
