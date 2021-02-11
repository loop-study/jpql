package jpql;

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

            String query = "select m from Member m left join m.team t on t.name = 'teamA'";
            List<Member> memberList = em.createQuery(query, Member.class)
                    .getResultList();

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}
