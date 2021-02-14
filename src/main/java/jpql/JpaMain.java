package jpql;

import org.hibernate.dialect.MySQL5Dialect;

import javax.persistence.*;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

//            for (int i = 0; i < 100; i++) {

            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            member.setTeam(team);
            member.setType(MemberType.ADMIN);
            em.persist(member);

//            }
//
            em.flush();
            em.clear();

            // 파라미터는 이름 기준, 위치 기준인데 이름 기준을 권장한다.
            // 이름 기준
//            Member singleResult = em.createQuery("select m from Member m where m.username = :username", Member.class)
//                    .setParameter("username", "member1")
//                    .getSingleResult();

            // 위치 기준, 여러개 파라미터인데 중간에 하나 밀어넣으면...  뒤로 다 하나씩 밀어줘야하는데 잘못하다간 에러 발생하기 쉽다
//            Member singleResult = em.createQuery("select m from Member m where m.username = ?1", Member.class)
//                    .setParameter(1, "member1")
//                    .getSingleResult();


//            TypedQuery<String> query2 = em.createQuery("select m.username from Member m", String.class);
//            Query query = em.createQuery("select m.username, m.age from Member m"); // 타입 정보를 못 받아오는 경우.

            // 1건 반환, 결과 없으면 에러 발생. 2개 이상면 에러 발생.
            // spring date JPA -> null 반환해줌. 익셉션 안터드림
//            Member result = query1.getSingleResult();
//            System.out.println("singleResult = " + singleResult.getUsername());

//
//            // 영속성 관리가 될까?
//            List<Member> members = em.createQuery("select m from Member as m", Member.class)
//                    .getResultList();
//            // 이렇게 team 가능함. 비권장 방식.
////            List<Team> teams = em.createQuery("select m.team from Member as m", Team.class)
////                    .getResultList();
//            // 권장 방식
//            List<Team> teams = em.createQuery("select t from Member m join m.team t", Team.class)
//                    .getResultList();
//
//            // update 발생. 영속성 관리가 된다.
//            Member member1 = members.get(0);
//            member1.setAge(20);

//            List<Object[]> resultList = em.createQuery("select m.username, m.age from Member m")
//                    .getResultList();
////            Object o = resultList.get(0);
//            Object[] result = resultList.get(0); //(Object[]) o;
//            System.out.println("username = " + result[0]);
//            System.out.println("age = " + result[1]);


            // 엔티티가 아닌 다른 경우, 생성자를 통해서 선언, 단점 패키지부터 다 적어야해서 불편. 나중에 쿼리DSL 로 처리함
//            List<MemberDTO> resultList = em.createQuery("select new jpql.MemberDTO(m.username, m.age) from Member m", MemberDTO.class)
//                    .getResultList();
//
//            MemberDTO memberDTO = resultList.get(0);
//            System.out.println("memberDTO.getUsername() = " + memberDTO.getUsername());
//            System.out.println("memberDTO.getAge() = " + memberDTO.getAge());

//            List<Member> resultList = em.createQuery("select m from Member m order by m.age desc", Member.class)
//                    .setFirstResult(1)  // 0번째부터
//                    .setMaxResults(10)  // 10개만
//                    .getResultList();
//
//            System.out.println("resultList.size() = " + resultList.size());
//            for (Member member1 : resultList) {
//                System.out.println("member = " + member1.toString());
//            }
//
//            String query = "select m from Member m left join m.team t on t.name = 'teamA'";
//            List<Member> memberList = em.createQuery(query, Member.class)
//                    .getResultList();

//            String query = "select m.username, 'HELLO', true From Member m " +
////                           "where m.type = jpql.MemberType.ADMIN";
//                           "where m.type = :userType";
//
//            List<Object[]> result = em.createQuery(query)
//                    .setParameter("userType", MemberType.ADMIN)
//                    .getResultList();
//
//            for (Object[] objects : result) {
//                System.out.println("objects = " + objects[0]); //teamA
//                System.out.println("objects = " + objects[1]); //HELLO
//                System.out.println("objects = " + objects[2]); //true
//            }

            //queryDsl 이용하면 단순하게 처리됨
//            String query = "select " +
//                                "case when m.age <= 10 then '학생요금' " +
//                                "     when m.age >= 60 then '경로요금' " +
//                                "     else '일반요금' end " +
//                                "from Member m";

            //coalesce
//            String query = "select coalesce(m.username, '이름 없는 회원') as username " +
//                            "from Member m ";

            //nullif
//            String query = "select nullif(m.username, '관리자') as username " +
//                            "from Member m";

            //***사용자 정의 함수 호출 - 디비 방언에 추가되어 있어야한다
            //MySQL5Dialect registerFunction(...) 으로 등록되어있는걸 볼 수 있다
//            String query = "select function('group_concat', m.type) as username " +
//                    "from Member m";

            // MyH2Dialect 방언에 등록 후 사용이 가능해짐
//            String query = "select group_concat(m.username) from Member m";

            // concat 도 있고. substring, trim, LOWER, UPPER, LENGTH 등등 다 가능
//            String query = "select concat('a', 'b') as strsum " +
//                    "from Member m";

//            @OrderColumn // 사용 비권장
//            String query = "select index(t.members) " +
//                    "from Team t";

            String query = "select size(t.members) " +
                    "from Team t";

            List<Integer> resultList = em.createQuery(query, Integer.class)
                    .getResultList();

            for (Integer s : resultList) {
                System.out.println("s = " + s);
            }

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}
