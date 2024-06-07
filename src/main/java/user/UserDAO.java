package user;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {
	//UserDAO는 데이터베이스의 연결 및 사용자 관련 작업을 담당
	private Connection conn; // 데이터베이스 연결을 위한 Connection 객체
	private PreparedStatement pstmt; // SQL 쿼리를 실행하기 위한 PreparedStatement 객체	
	private ResultSet rs; // SQL 쿼리 결과를 저장하기 위한 ResultSet 객체

	public UserDAO() {
		try {
			String dbURL = "jdbc:mysql://localhost:3306/BoardSystem?useSSL=false&serverTimezone=UTC";
			String dbID = "root";
			String dbPassword = "1234";
			// MySQL 드라이버 로드
			Class.forName("com.mysql.cj.jdbc.Driver");
			// 데이터베이스 연결
			conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	userID 사용자의 ID
    userPassword 사용자의 비밀번호
    return 1: 로그인 성공, 0: 비밀번호 불일치, -1: ID가 없음, -2: 데이터베이스 오류
    */
	public int login(String userID, String userPassword) {
		String SQL = "SELECT userPassword FROM USER WHERE userID = ?";
		try {
			pstmt = conn.prepareStatement(SQL); // SQL 쿼리 준비
			pstmt.setString(1, userID); // 사용자 ID 설정
			rs = pstmt.executeQuery(); // 쿼리 실행
			if (rs.next()) {
				if (rs.getString(1).equals(userPassword)) {
					return 1; // 로그인 성공
				} else {
					return 0; // 비밀번호 불일치
				}
			}
			return -1; // ID가 없음

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) rs.close(); // ResultSet 객체 닫기
				if (pstmt != null) pstmt.close(); // PreparedStatement 객체 닫기
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return -2; // 데이터베이스 오류
	}
	
	//새로운 사용자를 데이터베이스에 등록. ex) 회원가입
	public int join(User user) {
		String SQL = "INSERT INTO USER VALUES (?, ?, ?, ?, ?)";
		try {
			pstmt = conn.prepareStatement(SQL); //쿼리준비
			pstmt.setString(1, user.getUserID());
			pstmt.setString(2, user.getUserPassword());
			pstmt.setString(3, user.getUserName());
			pstmt.setString(4, user.getUserGender());
			pstmt.setString(5, user.getUserEmail());
			return pstmt.executeUpdate(); //쿼리 실행 및 결과 반환
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1; // 데이터베이스 오류
	}
}
