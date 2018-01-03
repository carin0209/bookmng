package sist.group1;

import java.util.Scanner;
import java.util.regex.Pattern;

public class LibraryService {
	
	private LibraryDAO dao = new LibraryDAO();
	private Utils utils = Utils.getInstance();
	
	public void login(Scanner sc) {
		System.out.println("로그인을 진행합니다.");
		System.out.print("아이디>");
		String userId = sc.next();
		System.out.print("비밀번호>");
		String password = sc.next();
		if(userId.equals(utils.getAdmin())&& password.equals(utils.getAdmin())) {
			this.adminMenu(sc);
		}else {
			try {
				this.isWrongUser(userId, password);
				if(this.dao.getCurrentUser(userId, password)!=null) {
					//현재 사용자 설정
					utils.setCurrentUser(this.dao.getCurrentUser(userId, password));
					this.userMenu(sc);
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}
	
	public void adminMenu(Scanner sc) {
		while(true){
			System.out.println("관리자로 로그인 했습니다.");
			System.out.println("1.도서 관리 2.회원 관리 0.로그 아웃");
			int input = sc.nextInt();
			if(input == 0) break;
			switch(input) {
			case 1:/*도서 관리 메뉴 메소드 호출*/break;
			case 2:/*회원 관리 메뉴 메소드 호출*/break;
			default : System.out.println("알 수 없는 입력입니다. 다시 입력해주세요.");
			}
		}
	}
	public void userMenu(Scanner sc) {
		System.out.println("["+utils.getCurrentUser().getName()+"]"+"님으로 로그인 했습니다.");
		/*관리자에게 온 메세지가 [1]개 있습니다. (메세지 메소드 호출)*/
		/*[1] 연체중인 도서가 있습니다. 반납 해주세요. (연체 메소드 호출)*/
		while(true) {
			System.out.println("1.도서 검색   2.대출 목록   3.반납 목록   4.메시지 확인   0.로그아웃");
			System.out.print("선택>");
			int input = sc.nextInt();
			if(input == 0)break;
			switch(input) {
			case 1: /*도서 검색 sub메뉴 */break;
			case 2: /*대출 목록 sub메뉴*/break;
			case 3: /*반납 목록 sub메뉴*/break;
			case 4: /*메세지 확인 sub메뉴*/break;
			default : System.out.println("알 수 없는 입력입니다. 다시 입력해주세요.");
			}
		}
	}
	public void register(Scanner sc) {
		System.out.println("회원가입을 진행합니다.");

		System.out.print("아이디>");
		String userId = sc.next();
		System.out.print("비밀번호(영문+숫자 조합 8자 이상)>");
		String password = sc.next();
		while (true) {
			try {
				this.isPasswordPattern(password);
				break;
			} catch (Exception e) {
				System.out.println(e.getMessage());
				System.out.print("비밀번호(영문+숫자 조합 8자 이상)>");
				password = sc.next();
			}
		}
		System.out.print("이름>");
		String name = sc.next();
		System.out.print("전화번호(010-1234-5678)>");
		String phone = sc.next();
		while(true) {
			try {
				this.isPhonePattern(phone);
				break;
			}catch(Exception e) {
				System.out.println(e.getMessage());
				System.out.print("전화번호(010-1234-5678)>");
				phone = sc.next();
			}
		}
		if (this.dao.getCurrentUser(userId, password)!=null) {
			System.out.println("이미 존재하는 id 입니다. 다시 입력해주세요.");
		} else {
			this.dao.register(userId, password, name, phone);
			System.out.println("회원 가입이 완료 되었습니다.");
		}
	}
	
	//프로그램 종료시 users, books, checkOuts 데이터 저장
	public void fileSave() {
		System.out.println("프로그램을 종료합니다.");
		this.dao.logout();
	}
	
	public void isPhonePattern(String password) throws PatternException{
		String temp = "(\\d{3}).*(\\d{3}).*(\\d{4})";
		Boolean bool = Pattern.matches(temp, password);
		if(!bool) {
			throw new PatternException("잘못된 전화번호 형식 입니다. 다시 입력해주세요.");
		}
	}
	public void isPasswordPattern(String phone) throws PatternException{
		String temp = "(?=.*\\d)(?=.*[a-z]).{8,15}";
		Boolean bool = Pattern.matches(temp, phone);
		if(!bool) {
			throw new PatternException("잘못된 비밀번호 형식 입니다. 다시 입력해주세요.");
		}
	}
	public void isWrongUser(String userId, String password) throws ExistUserException{
		if(this.dao.getCurrentUser(userId, password)==null && !userId.equals(utils.getAdmin())) {
			throw new ExistUserException("등록되지 않은 사용자 입니다. 다시 입력해주세요.");
		}
		if(this.dao.isWrongPassword(userId, password)) {
			throw new ExistUserException("잘못된 비밀번호 입니다. 다시 입력해주세요.");
		}
	}
	
public void searchForBooks(Scanner sc) throws SearchForBooksException {
		
		try {
		boolean run = true;
		
		while (run) {
			System.out.println("도서검색");
			System.out.println("1.등록번호 검색   2.도서명 검색   3.출판사 검색   4.저자 검색   0.나가기");
			System.out.print("선택>");

			int selectNum = sc.nextInt();
			sc.nextLine();
			System.out.println("검색할 도서를 입력해주세요");
			String key = null;
			
			switch (selectNum) {
			case 1:
				System.out.print("등록번호>");
				key = sc.next();
				this.dao.searchForBooks("등록번호", key);
				break;
			case 2:
				System.out.print("도서명>");
				key = sc.next();
				this.dao.searchForBooks("도서명", key);
				break;
			case 3:
				System.out.print("출판사>");
				key = sc.next();
				this.dao.searchForBooks("출판사", key);
				break;
			case 4:
				System.out.println("저자>");
				key = sc.next();
				this.dao.searchForBooks("저자", key);
				break;
			case 0 : run = false;break;
			
			}
		}
		
		} catch(Exception e) {
			throw new SearchForBooksException("등록되지 않은 책 입니다. 다시 입력해주세요.");
		}
	}

	public void viewCheckedOutBooks(Scanner sc) {

		this.dao.viewCheckedOutBooks();

	}

public void serachForUsers(Scanner sc) throws SearchForUsersException {
		
		try {
			
		boolean run = true;
		while (run) {
			System.out.println("회원검색");
			System.out.println("1.회원번호 검색 2.이름 검색 3.아이디 검색 4.전화번호 검색 0.나가기");
			System.out.print("선택>");

			int selectNum = sc.nextInt();
			sc.nextLine();
			System.out.println("검색 할 회원을 입력해주세요.");
			String key = null;
			
			switch (selectNum) {
			case 0:run = false;break;
			case 1:
				System.out.print("회원번호>");
				key = sc.next();
				System.out.println(this.dao.serachForUsers("회원번호", key));
				break;
			case 2:
				System.out.print("이름>");
				key = sc.next();
				System.out.println(this.dao.serachForUsers("이름", key));
				break;
			case 3:
				System.out.print("아이디>");
				key = sc.next();
				System.out.println(this.dao.serachForUsers("아이디", key));
				break;
			case 4:
				System.out.print("전화번호>");
				key = sc.next();
				System.out.println(this.dao.serachForUsers("전화번호", key));
				break;
			}
		}
		
		} catch(Exception e) {
			throw new SearchForUsersException ("잘못된 검색어 입니다. 다시 입력해 주세요.");
		}
	}

public void viewUserInDetail(Scanner sc) throws ViewUserInDetailSubException {

	boolean run = false;
	while (run) {
		System.out.println("1.회원 상세 보기  0.나가기");
		System.out.println("선택>");
		int selectNo = sc.nextInt();
		sc.nextLine();
		switch (selectNo) {
		// 상세보기 호출
		case 1:this.viewUserInDetailSub(sc);break;
		case 0:run = false;break;
		}
	}
	
	
}

private void viewUserInDetailSub(Scanner sc) throws ViewUserInDetailSubException {
	try {
	System.out.println("상세보기 할 회원 번호를 입력해 주세요");
	System.out.print("회원 번호 입력");
	String userNo = sc.next();
	sc.next();
	System.out.println(this.dao.viewUserInDetail(userNo));
	} catch (Exception e) {
		throw new ViewUserInDetailSubException("잘못된 회원번호 입니다. 다시 입력해 주세요.");
	}
}
}
