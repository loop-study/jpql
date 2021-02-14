package dialect;

import org.hibernate.dialect.H2Dialect;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.StandardBasicTypes;

// 사용자 정의 함수를 사용하려면
// 해당 디비의 방언에 등록해줘야한다
// persistence.xml 에서 H2Dialect 대신 MyH2Dialect 로 변경해야한다.
// H2Dialect 는 group_concat 없기 때문
public class MyH2Dialect extends H2Dialect {

    public MyH2Dialect() {
        // 등록 방법은 실제 소스코드 내부를 확인해서 참조하자
        registerFunction("group_concat", new StandardSQLFunction("group_concat", StandardBasicTypes.STRING));
    }
}
