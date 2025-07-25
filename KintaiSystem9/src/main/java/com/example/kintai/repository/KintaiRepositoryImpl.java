// 勤怠データをデータベースとやり取りするリポジトリの実装クラスです。
// @Repository アノテーションにより、Springによって管理されるコンポーネントになります。
// @RequiredArgsConstructor は Lombok のアノテーションで、finalなフィールドを自動でDI（依存性注入）します。
package com.example.kintai.repository;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

// データベースのテーブルに対応するJavaオブジェクト
import com.example.kintai.entity.Kintai;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class KintaiRepositoryImpl implements KintaiRepository {

    // JdbcTemplate はSpringが提供する、JDBC（Java DataBase Connectivity）を簡単に扱うためのクラスです。
    // これを使ってSQLクエリを実行します。
    private final JdbcTemplate jdbcTemplate;

    // RowMapper は、データベースから取得した1行のデータをJavaのKintaiオブジェクトに変換するためのものです。
    private RowMapper<Kintai> kintaiRowMapper = new RowMapper<Kintai>() {
        @Override
        public Kintai mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            Kintai kintaiRecord = new Kintai();
            // ResultSetから各列の値を取得し、Kintaiオブジェクトに設定します。
            kintaiRecord.setDate(resultSet.getDate("date"));
            kintaiRecord.setYear(resultSet.getInt("year"));
            kintaiRecord.setMonth(resultSet.getInt("month"));
            kintaiRecord.setDay(resultSet.getInt("day"));
            kintaiRecord.setInTimeH(resultSet.getString("in_time_h"));
            kintaiRecord.setInTimeM(resultSet.getString("in_time_m"));
            kintaiRecord.setOutTimeH(resultSet.getString("out_time_h"));
            kintaiRecord.setOutTimeM(resultSet.getString("out_time_m"));
            kintaiRecord.setUserId(resultSet.getString("user_id"));
            kintaiRecord.setDepartId(resultSet.getString("depart_id"));
            kintaiRecord.setAttId(resultSet.getString("att_id"));
            return kintaiRecord;
        }
    };

    /**
     * 新しい勤怠情報をデータベースに挿入（登録）します。
     * @param kintaiRecord 登録する勤怠情報（Kintaiオブジェクト）
     */
    @Override
    public void insert(Kintai kintaiRecord) {
        // データベースに新しい勤怠レコードを挿入するためのSQL文
        String sql = "INSERT INTO attendance "
                   + "(date,user_id,depart_id,att_id,in_time_h,in_time_m,out_time_h,out_time_m,year,month,day) "
                   + "VALUES (?,?,?,?,?,?,?,?,?,?,?)";

        // JdbcTemplate の update メソッドを使ってSQLを実行します。
        // ? にはKintaiオブジェクトから取得した値が順番にバインドされます。
        jdbcTemplate.update(sql,
                kintaiRecord.getDate(),
                kintaiRecord.getUserId(),
                kintaiRecord.getDepartId(),
                kintaiRecord.getAttId(),
                kintaiRecord.getInTimeH(),
                kintaiRecord.getInTimeM(),
                kintaiRecord.getOutTimeH(),
                kintaiRecord.getOutTimeM(),
                kintaiRecord.getYear(),
                kintaiRecord.getMonth(),
                kintaiRecord.getDay());
    }

    /**
     * 既存の勤怠情報をデータベースで更新します。
     * @param kintaiRecord 更新する勤怠情報（Kintaiオブジェクト）
     */
    @Override
    public void update(Kintai kintaiRecord) {
        // データベースの勤怠レコードを更新するためのSQL文
        String sql = "UPDATE attendance SET "
                   + "depart_id = ?, att_id = ?, in_time_h = ?, in_time_m = ?, "
                   + "out_time_h = ?, out_time_m = ?, year = ?, month = ?, day = ? "
                   + "WHERE date = ? AND user_id = ?"; // 特定のユーザーと日付のレコードを対象
        
        // JdbcTemplate の update メソッドを使ってSQLを実行します。
        jdbcTemplate.update(sql,
                kintaiRecord.getDepartId(),
                kintaiRecord.getAttId(),
                kintaiRecord.getInTimeH(),
                kintaiRecord.getInTimeM(),
                kintaiRecord.getOutTimeH(),
                kintaiRecord.getOutTimeM(),
                kintaiRecord.getYear(),
                kintaiRecord.getMonth(),
                kintaiRecord.getDay(),
                kintaiRecord.getDate(), // WHERE句の条件
                kintaiRecord.getUserId()); // WHERE句の条件
    }

    /**
     * 特定のユーザーの特定の日付の勤怠情報をデータベースから選択（取得）します。
     * @param userId 検索するユーザーのID
     * @param date 検索する日付（java.sql.Dateオブジェクト）
     * @return 該当する勤怠情報（Kintaiオブジェクト）のリスト。見つからない場合は空のリスト。
     */
    @Override
    public List<Kintai> selectByUserAndDate(String userId, Date date) {
        // 特定のユーザーと日付の勤怠情報を取得するためのSQL文
        String sql = "SELECT * FROM attendance WHERE user_id = ? AND date = ?";
        // JdbcTemplate の query メソッドを使ってSQLを実行し、結果をKintaiオブジェクトのリストにマッピングします。
        return jdbcTemplate.query(sql, kintaiRowMapper, userId, date);
    }

    /**
     * 特定のユーザーの指定された期間の勤怠情報をデータベースから選択（取得）します。
     * @param userId 検索するユーザーのID
     * @param startDate 検索期間の開始日（java.sql.Dateオブジェクト）
     * @param endDate 検索期間の終了日（java.sql.Dateオブジェクト）
     * @return 該当する勤怠情報（Kintaiオブジェクト）のリスト。
     */
    @Override
    public List<Kintai> selectByUserAndPeriod(String userId, Date startDate, Date endDate) {
        // 特定のユーザーと期間の勤怠情報を取得するためのSQL文
        // ORDER BY date で日付順に並べ替えます。
        String sql = "SELECT * FROM attendance WHERE user_id = ? AND date BETWEEN ? AND ? ORDER BY date";
        return jdbcTemplate.query(sql, kintaiRowMapper, userId, startDate, endDate);
    }

    /**
     * 特定の部署の指定された期間の勤怠情報をデータベースから選択（取得）します。
     * @param departmentId 検索する部署のID
     * @param startDate 検索期間の開始日（java.sql.Dateオブジェクト）
     * @param endDate 検索期間の終了日（java.sql.Dateオブジェクト）
     * @return 該当する勤怠情報（Kintaiオブジェクト）のリスト。
     */
    @Override
    public List<Kintai> selectByDepartmentAndPeriod(String departmentId, Date startDate, Date endDate) {
        // 特定の部署と期間の勤怠情報を取得するためのSQL文
        // ORDER BY date で日付順に並べ替えます。
        String sql = "SELECT * FROM attendance WHERE depart_id = ? AND date BETWEEN ? AND ? ORDER BY date";
        return jdbcTemplate.query(sql, kintaiRowMapper, departmentId, startDate, endDate);
    }
}