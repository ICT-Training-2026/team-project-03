package com.example.demo.repository;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Attendance;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AttInfoRepository implements ReviewRepository {

	private final JdbcTemplate jdbcTemplate;
	
	@Override
	public void add(Attendance review) {

		String sql =
			" INSERT INTO t_review " +
			" (restaurant_id, user_id, visit_date, rating, comment) " +
			" VALUES (?, ?, ?, ?, ?) ";	

		jdbcTemplate.update(sql, review.getRestaurantId(),
								 review.getUserId(),
								 review.getVisitDate(),
								 review.getRating(),
								 review.getComment()		);
		
	}

	@Override
	public List<Attendance> selectByRestaurantId(int restaurantId) {

		String sql = 
				"  SELECT                 " + 
				"    review_id,           " + 
				"    restaurant_id,       " + 
				"    user_id,             " + 
				"    visit_date,          " + 
				"    rating,              " + 
				"    comment              " + 
				"  FROM                   " + 
				"    t_review             " + 
				"  WHERE                  " + 
				"    restaurant_id = ?    " + 
				"  ORDER BY               " + 
				"    visit_date DESC,     " + 
				"    review_id ASC        ";
		
		// SQLで検索（プレースホルダ：引数で受け取ったrestaurantId）
		List<Map<String, Object>> list 
						= jdbcTemplate.queryForList(sql, restaurantId);
		
		// 値の取得⇒結果の格納
		List<Attendance> result = new ArrayList<Attendance>(); // 結果の初期化
		for (Map<String, Object> one : list) {
			Attendance review = new Attendance();
			review.setReviewId((int)one.get("review_id"));
			review.setRestaurantId((int)one.get("restaurant_id"));
			review.setUserId((String)one.get("user_id"));
			review.setVisitDate((Date)one.get("visit_date"));
			review.setRating((int)one.get("rating"));
			review.setComment((String)one.get("comment"));
			result.add(review);
		}

		return result;
	}

	@Override
	public void update(Attendance review) {

		String sql =
				" UPDATE             " + 
				"   t_review         " + 
				" SET                " + 
				"   user_id = ? ,    " + 
				"   visit_date = ? , " + 
				"   rating = ?,      " + 
				"   comment = ?      " + 
				" WHERE              " + 
				"   review_id = ?    ";
		
		jdbcTemplate.update(sql, 
				review.getUserId(),
				review.getVisitDate(),
				review.getRating(),
				review.getComment(),
				review.getReviewId()  );
		
	}

	@Override
	public void delete(Attendance review) {
		
		String sql =
				" DELETE              " + 
				" FROM                " + 
				"   t_review          " + 
				" WHERE               " + 
				"   review_id = ?     "; 
		
		jdbcTemplate.update(sql, review.getReviewId());
		
	}

}
