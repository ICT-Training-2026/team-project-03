package com.example.kintai.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody; // CSV出力用

import com.example.kintai.entity.AttendanceType;
import com.example.kintai.entity.Department;
import com.example.kintai.entity.Kintai;
import com.example.kintai.entity.User;
import com.example.kintai.service.AttendanceTypeService;
import com.example.kintai.service.DepartmentService;
import com.example.kintai.service.KintaiService;
import com.example.kintai.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class DepartmentPageController {

    private final DepartmentService departmentService;
    private final UserService userService;
    private final KintaiService kintaiService;
    private final AttendanceTypeService attendanceTypeService;

    /**
     * 各部署勤怠情報画面を表示する。
     * GETリクエストを "/department" で受け取ります。
     * @param departmentId 検索対象の部署ID - オプション
     * @param year 検索対象の年 - オプション
     * @param month 検索対象の月 - オプション
     * @param model Thymeleafに渡すデータを保持するModelオブジェクト
     * @param session セッション情報
     * @return "department" (department.htmlのテンプレート名)
     */
    @GetMapping("/department")
    public String showDepartmentPage(
            @RequestParam(required = false) String departmentId,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month,
            Model model,
            HttpSession session) {

        // 管理者権限チェック
        Integer isAdminInt = (Integer) session.getAttribute("loginUserAdmin");
        if (isAdminInt == null || isAdminInt != 1) {
            return "redirect:/main"; // 管理者でない場合はメインページへリダイレクト
        }

        // 全部署リストを取得してモデルに追加
        List<Department> departments = departmentService.getAllDepartments();
        model.addAttribute("departments", departments);

        // デフォルトの年月と部署IDを設定
        LocalDate today = LocalDate.now();
        int currentYear = (year != null) ? year : today.getYear();
        int currentMonth = (month != null) ? month : today.getMonthValue();
        String currentDepartmentId = (departmentId != null && !departmentId.isEmpty()) ? departmentId :
                                     (departments.isEmpty() ? null : departments.get(0).getDepartmentId());

        model.addAttribute("selectedYear", currentYear);
        model.addAttribute("selectedMonth", currentMonth);
        model.addAttribute("selectedDepartmentId", currentDepartmentId);

        String selectedDepartmentName = "";
        if (currentDepartmentId != null) {
            selectedDepartmentName = departments.stream()
                    .filter(d -> d.getDepartmentId().equals(currentDepartmentId))
                    .map(Department::getDepartmentName)
                    .findFirst()
                    .orElse("不明");
        }
        model.addAttribute("selectedDepartmentName", selectedDepartmentName);

        // 勤怠区分マップを準備
        Map<String, String> attendanceTypeMap = attendanceTypeService.getAllAttendanceTypes().stream()
                .collect(Collectors.toMap(AttendanceType::getAttendanceTypeId, AttendanceType::getAttendanceTypeName));
        model.addAttribute("attendanceTypeMap", attendanceTypeMap);

        // 勤怠データを取得してモデルに追加
        if (currentDepartmentId != null) {
            LocalDate startDate = LocalDate.of(currentYear, currentMonth, 1);
            LocalDate endDate = startDate.with(TemporalAdjusters.lastDayOfMonth());

            List<Kintai> kintaiList = kintaiService.getDepartmentKintaiByPeriod(currentDepartmentId, startDate, endDate);

            // ユーザーごとに勤怠データをまとめる (userId -> Map<day, Kintai>)
            Map<String, Map<Integer, Kintai>> kintaiByUsers = kintaiList.stream()
                    .collect(Collectors.groupingBy(
                            Kintai::getUserId,
                            Collectors.toMap(Kintai::getDay, k -> k)
                    ));
            model.addAttribute("kintaiByUsers", kintaiByUsers);

            // その部署の全ユーザー（ソート済み）
            List<User> usersInDepartment = userService.getAllUsers().stream()
                    .filter(user -> user.getDepartmentId().equals(currentDepartmentId))
                    .sorted(Comparator.comparing(User::getUserId)) // ユーザーIDでソート
                    .collect(Collectors.toList());
            model.addAttribute("usersInDepartment", usersInDepartment);

            // ユーザーIDとユーザー名のマップ
            Map<String, String> userMap = usersInDepartment.stream()
                    .collect(Collectors.toMap(User::getUserId, User::getUserName));
            model.addAttribute("userMap", userMap);

            model.addAttribute("lastDayOfMonth", endDate.getDayOfMonth());
        } else {
            model.addAttribute("kintaiByUsers", new HashMap<>());
            model.addAttribute("usersInDepartment", new ArrayList<>());
            model.addAttribute("userMap", new HashMap<>());
            model.addAttribute("lastDayOfMonth", today.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth());
        }

        return "department";
    }

    /**
     * 部署勤怠情報をCSVで出力する。
     * GETリクエストを "/department/exportCsv" で受け取ります。
     * @param departmentId 検索対象の部署ID
     * @param year 検索対象の年
     * @param month 検索対象の月
     * @param userIds CSV出力対象のユーザーIDリスト (カンマ区切り文字列)
     * @return 生成されたCSV文字列
     * @throws IOException CSV生成エラー
     */
    @GetMapping("/department/exportCsv")
    @ResponseBody // 直接レスポンスボディを返す
    public String exportDepartmentKintaiCsv(
            @RequestParam String departmentId,
            @RequestParam Integer year,
            @RequestParam Integer month,
            @RequestParam(required = false) List<String> userIds) throws IOException {

        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.with(TemporalAdjusters.lastDayOfMonth());

        List<Kintai> kintaiList = kintaiService.getDepartmentKintaiByPeriod(departmentId, startDate, endDate);

        // userIdsが指定されている場合はフィルタリング
        List<Kintai> filteredKintaiList = (userIds != null && !userIds.isEmpty()) ?
                kintaiList.stream()
                        .filter(k -> userIds.contains(k.getUserId()))
                        .collect(Collectors.toList()) :
                kintaiList;

        // CSVヘッダー
        StringBuilder csvBuilder = new StringBuilder();
        csvBuilder.append("社員コード,年月,始業時刻(時),始業時刻(分),就業時刻(時),終業時刻(時),労働時間,休憩時間,超過時間\n");

        DateTimeFormatter ymFormatter = DateTimeFormatter.ofPattern("yyyy-MM");

        for (Kintai rec : filteredKintaiList) {
            String emp = rec.getUserId();
            String ym = LocalDate.of(rec.getYear(), rec.getMonth(), rec.getDay()).format(ymFormatter);
            String inH = rec.getInTimeH() != null ? rec.getInTimeH() : "0";
            String inM = rec.getInTimeM() != null ? rec.getInTimeM() : "0";
            String outH = rec.getOutTimeH() != null ? rec.getOutTimeH() : "0";
            String outM = rec.getOutTimeM() != null ? rec.getOutTimeM() : "0";

            int startMin = Integer.parseInt(inH) * 60 + Integer.parseInt(inM);
            int endMin = Integer.parseInt(outH) * 60 + Integer.parseInt(outM);
            int totalMin = Math.max(endMin - startMin, 0);
            int breakMin = totalMin > 0 ? 60 : 0; // 暫定的に1時間休憩
            int workMin = Math.max(totalMin - breakMin, 0);
            int overtime = Math.max(workMin / 60 - 8, 0); // 8時間超を超過時間

            int workH = workMin / 60;
            int breakH = breakMin / 60;

            csvBuilder.append(String.join(",",
                    emp,
                    ym,
                    inH,
                    inM,
                    outH,
                    outM,
                    String.valueOf(workH),
                    String.valueOf(breakH),
                    String.valueOf(overtime)
            )).append("\n");
        }

        // BOMを付与 (UTF-8 BOM)
        return "\uFEFF" + csvBuilder.toString();
    }
}