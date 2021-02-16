package jpql;

import org.hibernate.dialect.MySQL5Dialect;

import javax.persistence.*;
import java.util.Collection;
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

            Team team2 = new Team();
            team2.setName("teamB");
            em.persist(team2);

            Member member = new Member();
            member.setUsername("member1");
            member.setTeam(team);
            em.persist(member);

            Member member2 = new Member();
            member2.setUsername("member2");
            member2.setTeam(team);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("member3");
            member3.setTeam(team2);
            em.persist(member3);

            // 벌크 연산을 여기서 하면 어떻게 될까? 이 쿼리만 곧바로 디비에 직접 반영됨.
            int resultCount2 = em.createQuery("update Member m set m.age = 20")
                    .executeUpdate();

            System.out.println("resultCount2 = " + resultCount2);

            // 디비상에는 20 으로 되어있음. 데이터에 문제가 생김
            System.out.println("member.getAge = " + member.getAge());  // 0
            System.out.println("member2.getAge = " + member2.getAge());// 0
            System.out.println("member3.getAge = " + member3.getAge());// 0
            
            // 여기서 조회하면? 여기도 0. 영속성에 등록된 캐시값을 가져오기에 괴리감 발생.
            // clear 후 다시 조회해야 일치됨
            Member findMember = em.find(Member.class, member.getId());
            System.out.println("findMember.getAge() = " + findMember.getAge());

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

//            String query = "select size(t.members) " +
//                    "from Team t";

            // 상태필드
//            String query = "select m.username from Member m";드

//            List<Integer> resultList = em.createQuery(query, Integer.class)
//                    .getResultList();

//            for (Integer s : resultList) {
//                System.out.println("s = " + s);
//            }

            // 단일값 연관 경로 - 묵시적 내부조인, 탐색 가능 -> 쿼리 튜닝 어려움.
//            String query = "select m.team from Member m";
            // 콜렉션값 연관 경로 - 묵시적 내부 조인, 탐색 불가능 -> 쿼리 튜닝 어려움.
//            String query = "select t.members from Team t";
            // 명시적 조인을 하면 탐색 가능
//            String query = "select m.username from Team t join t.members m";
//
//            // 묵시적 내부 조인이 안되게 하자. 운영하기 힘들어진다.
//            // 이런 방식으론 사용 안함.
//            Collection result = em.createQuery(query, Collection.class)
//                    .getSingleResult();
//
//            System.out.println("result = " + result);

            // ********** fetch join **********
//            String query = "select m from Member m";
//            String query = "select m from Member m join fetch m.team";
//            String query = "select t from Team t join fetch t.members";

            // distinct 중복 제거, 조회 후 결과값 중복을 줄여줌
//            String query = "select distinct t from Team t join fetch t.members";

            // 패치 조인 대상에 별칭을 못 준다
            // 팀에 컬렉션이 여러개면 둘 이상 컬렉션 패치조인 하면 안된다. 데이터 정합성 문제가 생김.
//            String query = "select t from Team t";
            // -> select m.*, t.* from member m inner join team t on m.team_id = t.id

            // 페치 조인에 페이징까지 쓰면 jpql 에서 경고 메시지 남김
            // 모든 데이터 다 퍼올리고 페이징처리 하기 때문임!!!
            // 다대일로 쿼리 방향을 잡자
            // select t from Team t join fetch t.members
            // -> select m from Member m jon fetch m.team t
//            List<Team> resultList = em.createQuery(query, Team.class)
//                    .setFirstResult(0)
//                    .setMaxResults(2)
//                    .getResultList();
//
//            System.out.println("resultList = " + resultList.size());

//            for (Team tm : resultList) {
////                System.out.println("mm.getUsername() = " + mm.getUsername() + ", " + mm.getTeam().getName());
//                // select m from Member m 로 조회 시.
//                //member1, 팀A(SQL)
//                //member2, 팀A(1차캐시)
//                //member3, 팀B(SQL)
//                //팀 소속 수 만큼 조회됨... 지연 로딩임.
//                //회원이 수백명이라면? 팀도 최대 수백번 조회 쿼리 발생함...
//                //회원 100명 -> n + 1
//
//                //이 해결방법은 fetch join 뿐임
//                // select m from Member m join fetch m.team
//                //fetch join 후 한번만 조회함!!
//                System.out.println("tm.getName() = " + tm.getName() + " | members=" + tm.getMembers().size());
//                for (Member mm : tm.getMembers()) {
//                    System.out.println("mm = " + mm);
//                }
//            }

            // m := member 가 m.id = ? 로 바뀜
//            String query = "select m from Member m where m = :member";
//
//            Member findMember = em.createQuery(query, Member.class)
//                    .setParameter("member", member)
//                    .getSingleResult();

//            String query = "select m from Member m where m.team = :team";
//
//            List<Member> members = em.createQuery(query, Member.class)
//                    .setParameter("team", team)
//                    .getResultList();

//            List<Member> resultList = em.createNamedQuery("Member.findByUsername", Member.class)
//                    .setParameter("username", "member1")
//                    .getResultList();

//            System.out.println("resultList = " + resultList);

            // 벌크 연산, 몇건 영향 받았는지 건수 반환함
            // 벌크 연산은 영속성을 무시하고 디비에 직접 쿼리 날림
            // 벌크 연산을 먼저 실행시키고 영속성 컨텍스트 초기화 시키자
//            int resultCount = em.createQuery("update Member m set m.age = 20")
//                    .executeUpdate();
//
//            System.out.println("resultCount = " + resultCount);

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}
