package controller;


import java.sql.SQLException;

import view.ViewConstructor;
import model.DatabaseController;

public class Library {
	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		DatabaseController dbController = new DatabaseController(
				"jdbc:sqlserver://localhost\\MSSQLSERVER;"+
				"databaseName=Library;user=sa;password=1;"
		);
		ViewConstructor vConstructor = new ViewConstructor("Библиотека", dbController);
		
		String[] booksTitles = {"№", "Название", "Автор", "Дата публикации"};
		vConstructor.createView("Книги", "Books", booksTitles);
		
		String[] usersTitles = new String[]{"№", "ФИО", "Дата рождения", "Номер телефона"};
		vConstructor.createView("Пользователи", "Users", usersTitles);
		
		String[] userBooksTitles = new String[]{"№", "№ пользователя", "№ книги"};
		vConstructor.createView("Выданные книги", "UserBooks", userBooksTitles);
	}
}
