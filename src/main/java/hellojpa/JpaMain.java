package hellojpa;

import jakarta.persistence.*;
import org.hibernate.Hibernate;

import java.time.LocalDateTime;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Team team2 = new Team();
            team2.setName("teamB");
            em.persist(team2);

            Member member = new Member();
            member.setUsername("hello1");
            member.setTeam(team);

            em.persist(member);

            Member member2 = new Member();
            member2.setUsername("hello2");
            member2.setTeam(team2);

            em.persist(member2);

            em.flush();
            em.clear();

//            Member m = em.find(Member.class, member.getId());
//            System.out.println("m.getTeam().getClass() = " + m.getTeam().getClass());

            List<Member> members = em.createQuery("select m from Member m join fetch m.team", Member.class)
                    .getResultList();
            //sql : select * from member; -> 하고보니 LAZY로딩 상태라 Team 도 가져와야함 -> select * from Team Where TEAM_ID = ?

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
        emf.close();
    }
}
