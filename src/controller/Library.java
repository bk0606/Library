package controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import view.ViewConstructor;
import model.DatabaseController;
/**
 * Controller of whole project
 * 
 * @author Albert Bikeev
 */
public class Library {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        DatabaseController dbController = new DatabaseController(
                "jdbc:sqlserver://localhost\\MSSQLSERVER;"+
                "user=sa;password=1;", "Library"
        );

        ViewConstructor vConstructor = new ViewConstructor("Библиотека", dbController);

        ResultSet booksRes = dbController.select("Books");
        String[] booksTitles = new String[]{"№", "Название", "Автор", "Дата публикации"};
        vConstructor.createGridView("Книги", booksTitles, "Books", booksRes);

        ResultSet usersRes = dbController.select("Users");
        String[] usersTitles = new String[]{"№", "ФИО", "Дата рождения", "Номер телефона"};
        vConstructor.createGridView("Пользователи", usersTitles, "Users", usersRes);

        ResultSet userBooksRes = dbController.select("User_books");
        String[] userBooksTitles = new String[]{"№", "№ пользователя", "№ книги", "Дата выдачи"};
        vConstructor.createGridView("Выданные книги", userBooksTitles, "User_books", userBooksRes);

        ResultSet searchRes = dbController.executeQuery(
                "SELECT * FROM Books AS b INNER JOIN User_books AS ub ON b.id = ub.id_book"
            );
        String[] searchTitles = new String[]{"№", "Название", "Автор", "Дата публикации", "№",
                                             "№ пользователя", "№ книги", "Дата выдачи"};
        vConstructor.createSearchView("Поиск", searchTitles, "Books", searchRes);
    }
}