package com.example.kintai.repository;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.kintai.entity.Kintai;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class KintaiRepositoryImpl implements KintaiRepository {

    private final JdbcTemplate jdbcTemplate;

    // RowMapper: SELECT 結果を Kintai オブジェクトにマッピング
    private final RowMapper<Kintai> kintaiRowMapper = new RowMapper<Kintai>() {
        @Override
        public Kintai mapRow(ResultSet rs, int rowNum) throws SQLException {
            Kintai k = new Kintai();
            k.setDate(rs.getDate("date"));
            k.setYear(rs.getInt("year"));
            k.setMonth(rs.getInt("month"));
            k.setDay(rs.getInt("day"));
            k.setInTimeH(rs.getString("in_time_h"));
            k.setInTimeM(rs.getString("in_time_m"));
            k.setOutTimeH(rs.getString("out_time_h"));
            k.setOutTimeM(rs.getString("out_time_m"));
            k.setUserId(rs.getString("user_id"));
            k.setDepartId(rs.getString("depart_id"));
            k.setAttId(rs.getString("att_id"));
            // 休憩時間のマッピングを追加
            k.setBreakTimeH(rs.getInt("break_time_h"));
            k.setBreakTimeM(rs.getInt("break_time_m"));
            return k;
        }
    };

    /**
     * 新規勤怠登録: 休憩時間カラムを追加
     */
    @Override
    public void insert(Kintai k) {
        String sql = "INSERT INTO attendance "
                   + "(date, user_id, depart_id, in_time_h, in_time_m, out_time_h, out_time_m, att_id, break_time_h, break_time_m, year, month, day) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
            k.getDate(),
            k.getUserId(),
            k.getDepartId(),
            k.getInTimeH(),
            k.getInTimeM(),
            k.getOutTimeH(),
            k.getOutTimeM(),
            k.getAttId(),
            k.getBreakTimeH(),
            k.getBreakTimeM(),
            k.getYear(),
            k.getMonth(),
            k.getDay()
        );
    }

    /**
     * 既存勤怠更新: 休憩時間カラムを追加
     */
    @Override
    public void update(Kintai k) {
        String sql = "UPDATE attendance SET "
                   + "depart_id = ?, att_id = ?, in_time_h = ?, in_time_m = ?, out_time_h = ?, out_time_m = ?, break_time_h = ?, break_time_m = ?, year = ?, month = ?, day = ? "
                   + "WHERE date = ? AND user_id = ?";
        jdbcTemplate.update(sql,
            k.getDepartId(),
            k.getAttId(),
            k.getInTimeH(),
            k.getInTimeM(),
            k.getOutTimeH(),
            k.getOutTimeM(),
            k.getBreakTimeH(),
            k.getBreakTimeM(),
            k.getYear(),
            k.getMonth(),
            k.getDay(),
            k.getDate(),
            k.getUserId()
        );
    }

    @Override
    public List<Kintai> selectByUserAndDate(String userId, Date date) {
        String sql = "SELECT * FROM attendance WHERE user_id = ? AND date = ?";
        return jdbcTemplate.query(sql, kintaiRowMapper, userId, date);
    }

    @Override
    public List<Kintai> selectByUserAndPeriod(String userId, Date startDate, Date endDate) {
        String sql = "SELECT * FROM attendance WHERE user_id = ? AND date BETWEEN ? AND ? ORDER BY date";
        return jdbcTemplate.query(sql, kintaiRowMapper, userId, startDate, endDate);
    }

    @Override
    public List<Kintai> selectByDepartmentAndPeriod(String departmentId, Date startDate, Date endDate) {
        String sql = "SELECT * FROM attendance WHERE depart_id = ? AND date BETWEEN ? AND ? ORDER BY date";
        return jdbcTemplate.query(sql, kintaiRowMapper, departmentId, startDate, endDate);
    }
}
