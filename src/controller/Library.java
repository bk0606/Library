package controller;


import java.sql.SQLException;
import view.ViewConstructor;
import model.DatabaseController;

public class Library {
	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		DatabaseController dbController = new DatabaseController(
				"jdbc:sqlserver://localhost\\MSSQLSERVER;"+
				"user=sa;password=1;", "Library"
		);
		
		ViewConstructor vConstructor = new ViewConstructor("Библиотека", dbController);
		
		String[] booksTitles = new String[]{"№", "Название", "Автор", "Дата публикации"};
		vConstructor.createGridView("Книги", "Books", booksTitles);
		
		String[] usersTitles = new String[]{"№", "ФИО", "Дата рождения", "Номер телефона"};
		vConstructor.createGridView("Пользователи", "Users", usersTitles);
		
		String[] userBooksTitles = new String[]{"№", "№ пользователя", "№ книги"};
		vConstructor.createGridView("Выданные книги", "UserBooks", userBooksTitles);
	}
}
