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
		ViewConstructor vConstructor = new ViewConstructor("����������", dbController);
		
		String[] booksTitles = {"�", "��������", "�����", "���� ����������"};
		vConstructor.createView("�����", "Books", booksTitles);
		
		String[] usersTitles = new String[]{"�", "���", "���� ��������", "����� ��������"};
		vConstructor.createView("������������", "Users", usersTitles);
		
		String[] userBooksTitles = new String[]{"�", "� ������������", "� �����"};
		vConstructor.createView("�������� �����", "UserBooks", userBooksTitles);
	}
}
