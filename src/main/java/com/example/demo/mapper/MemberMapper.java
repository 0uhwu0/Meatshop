package com.example.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.example.demo.domain.Members;

@Mapper
public interface MemberMapper {

	@Insert("""
			   INSERT INTO members (id,
			member_password,
			member_name,
			member_birthday,
			member_email,
			member_address,
			member_phone_number)
			VALUES(
			#{id},
			#{password},
			#{name},
			#{birthday},
			#{email},
			#{address},
			#{phoneNumber}
			)

						""")

	int insert(Members member);

	// 회원과 관리자권한까지 통합해서 검색
	@Select("""
			SELECT * FROM members m LEFT JOIN memberauthority ma ON m.id = ma.member_id
			WHERE id = #{id}
			""")
	@ResultMap("memberMap")
	Members selectId(String id);

	// ------------------------------------------------------------------------------

	@Select("""
			SELECT * FROM members m LEFT JOIN memberauthority ma ON m.id = ma.member_id
			WHERE member_email = #{email}
			""")
	@ResultMap("memberMap")
	Members selectEmail(String email);

	// ------------------------------------------------------------------------------

	@Select("""
			SELECT * FROM members m LEFT JOIN memberauthority ma ON m.id = ma.member_id
			ORDER BY member_created DESC

			""")
	@ResultMap("memberMap")
	List<Members> selectALL();

	@Update("""
			<script>

			UPDATE members
			SET
				<if test="password neq null and password neq ''">
				member_password = #{password},
				</if>


			    member_email = #{email},
			    member_address = #{address},
			    member_phone_number = #{phoneNumber}
			WHERE
				id = #{id}

			</script>
			""")
	@ResultMap("memberMap")
	Integer update(Members member);

	@Delete("""
			DELETE FROM members
			WHERE id= #{id}

			""")

	int deleteById(String id);

	@Select("""
			<script>
			<bind name="pattern" value="'%' + search + '%'" />
			SELECT *  FROM memberview

			<where>
			  <if test="(type eq 'all') or (type eq 'name')">
			       member_name  LIKE #{pattern}
			    </if>
			    <if test="(type eq 'all') or (type eq 'birthday')">
			    OR member_birthday   LIKE #{pattern}
			    </if>
			    <if test="(type eq 'all') or (type eq 'phoneNumber')">
			    OR member_phone_number LIKE #{pattern}
			    </if>
			</where>

			GROUP BY member_id
			ORDER BY member_id DESC

			</script>
			""")
	@ResultMap("memberMap")
	List<Members> selectMember(String search, String type);

	@Select("""
			SELECT member_name
			FROM members
			WHERE
			id = #{customerId}
			""")
	String getCustomerNameById(String customerId);

	// ------------------- memberList 페이지네이션 ------------------------

	@Select("""
			<script>
				<bind name="pattern" value="'%' + search + '%'" />
			SELECT COUNT(*)
			FROM members
					<where>
				<if test="(type eq 'all') or (type eq 'name')">
				   member_name  LIKE #{pattern}
				</if>
				<if test="(type eq 'all') or (type eq 'birthday')">
				OR member_birthday   LIKE #{pattern}
				</if>
				<if test="(type eq 'all') or (type eq 'PhoneNumber')">
				OR member_phone_number LIKE #{pattern}
				</if>
			</where>
			</script>
			""")

	Integer countAll(String search, String type);

	@Select("""
			<script>
			<bind name="pattern" value="'%' + search + '%'" />
			SELECT
			    *,
			    COUNT(m.id) fileCount
			FROM members m LEFT JOIN memberauthority ma ON m.id = ma.member_id

			<where>
			    <if test="(type eq 'all') or (type eq 'name')">
			       member_name  LIKE #{pattern}
			    </if>
			    <if test="(type eq 'all') or (type eq 'birthday')">
			    OR member_birthday   LIKE #{pattern}
			    </if>
			    <if test="(type eq 'all') or (type eq 'PhoneNumber')">
			    OR member_phone_number LIKE #{pattern}
			    </if>
			</where>

			GROUP BY m.id
			ORDER BY m.id DESC
			LIMIT #{startIndex}, #{rowPerPage}
			</script>
			""")
	@ResultMap("memberMap")
	List<Members> selectAllPaging(Integer startIndex, Integer rowPerPage, String search, String type);

}
