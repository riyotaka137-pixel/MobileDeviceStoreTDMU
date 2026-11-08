package com.tdmu.mobiledevicestoretdmu.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.tdmu.mobiledevicestoretdmu.R;
import com.tdmu.mobiledevicestoretdmu.database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdminDashboardFragment extends Fragment {

    private TextView tvTongDoanhThu, tvSoDonHangChoDuyet, tvTongSoKhachHang, tvTitleDoanhThu;
    private MaterialButtonToggleGroup toggleGroupDoanhThu;
    private LineChart lineChartDoanhThu;
    private PieChart pieChartDanhMuc;
    private DatabaseHelper databaseHelper;
    private int currentChartMode = 0; // 0: Tuần, 1: Tháng, 2: Năm

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_dashboard, container, false);

        tvTongDoanhThu = view.findViewById(R.id.tvTongDoanhThu);
        tvSoDonHangChoDuyet = view.findViewById(R.id.tvSoDonHangChoDuyet);
        tvTongSoKhachHang = view.findViewById(R.id.tvTongSoKhachHang);
        tvTitleDoanhThu = view.findViewById(R.id.tvTitleDoanhThu);

        toggleGroupDoanhThu = view.findViewById(R.id.toggleGroupDoanhThu);
        lineChartDoanhThu = view.findViewById(R.id.lineChartDoanhThu);
        pieChartDanhMuc = view.findViewById(R.id.pieChartDanhMuc);

        databaseHelper = new DatabaseHelper(requireContext());

        if (toggleGroupDoanhThu != null) {
            toggleGroupDoanhThu.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
                if (isChecked) {
                    if (checkedId == R.id.btnDoanhThuTuan) {
                        currentChartMode = 0;
                    } else if (checkedId == R.id.btnDoanhThuThang) {
                        currentChartMode = 1;
                    } else if (checkedId == R.id.btnDoanhThuNam) {
                        currentChartMode = 2;
                    }
                    setupLineChart(currentChartMode);
                }
            });
        }

        return view;
    }

    private void capNhatThongKe() {
        if (databaseHelper == null || getContext() == null) return;

        try {
            long tongDoanhThu = databaseHelper.getTongDoanhThu();
            int donChoDuyet = databaseHelper.getSoDonHangChoDuyet();
            int tongKhachHang = databaseHelper.getTongSoKhachHang();

            String giaFormat = String.format("%,d", tongDoanhThu).replace(',', '.');
            tvTongDoanhThu.setText(giaFormat + " đ");
            tvSoDonHangChoDuyet.setText(donChoDuyet + " đơn");
            tvTongSoKhachHang.setText(tongKhachHang + " TK");

            setupLineChart(currentChartMode);
            setupPieChart();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupLineChart(int mode) {
        if (lineChartDoanhThu == null) return;

        Map<String, Long> mapDoanhThu;
        if (mode == 1) {
            mapDoanhThu = databaseHelper.getDoanhThu30NgayQua();
            if (tvTitleDoanhThu != null) tvTitleDoanhThu.setText("Doanh thu (30 ngày)");
        } else if (mode == 2) {
            mapDoanhThu = databaseHelper.getDoanhThu12ThangQua();
            if (tvTitleDoanhThu != null) tvTitleDoanhThu.setText("Doanh thu (12 tháng)");
        } else {
            mapDoanhThu = databaseHelper.getDoanhThu7NgayQua();
            if (tvTitleDoanhThu != null) tvTitleDoanhThu.setText("Doanh thu (7 ngày)");
        }

        List<Entry> entries = new ArrayList<>();
        List<String> xLabels = new ArrayList<>();

        int index = 0;
        for (Map.Entry<String, Long> entry : mapDoanhThu.entrySet()) {
            xLabels.add(entry.getKey());
            entries.add(new Entry(index, entry.getValue().floatValue()));
            index++;
        }

        LineDataSet dataSet = new LineDataSet(entries, "Doanh thu (VNĐ)");
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setColor(Color.parseColor("#333333"));
        dataSet.setLineWidth(2.5f);
        dataSet.setCircleColor(Color.parseColor("#333333"));
        dataSet.setCircleRadius(4f);
        dataSet.setDrawCircleHole(true);
        dataSet.setCircleHoleRadius(2f);
        dataSet.setValueTextSize(9f);
        dataSet.setValueTextColor(Color.parseColor("#333333"));
        dataSet.setDrawFilled(true);
        dataSet.setFillColor(Color.parseColor("#FDB623"));

        LineData lineData = new LineData(dataSet);
        lineChartDoanhThu.setData(lineData);

        // Styling Chart UI
        lineChartDoanhThu.getDescription().setEnabled(false);
        lineChartDoanhThu.getLegend().setEnabled(false);
        lineChartDoanhThu.setTouchEnabled(true);
        lineChartDoanhThu.setPinchZoom(true);

        XAxis xAxis = lineChartDoanhThu.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xLabels));

        YAxis yAxisRight = lineChartDoanhThu.getAxisRight();
        yAxisRight.setEnabled(false);

        YAxis yAxisLeft = lineChartDoanhThu.getAxisLeft();
        yAxisLeft.setDrawGridLines(true);
        yAxisLeft.setGridColor(Color.parseColor("#E5E5EA"));

        lineChartDoanhThu.animateX(800);
        lineChartDoanhThu.invalidate();
    }

    private void setupPieChart() {
        if (pieChartDanhMuc == null) return;

        Map<String, Float> mapTyTrong = databaseHelper.getTyTrongDanhMuc();
        List<PieEntry> pieEntries = new ArrayList<>();

        for (Map.Entry<String, Float> entry : mapTyTrong.entrySet()) {
            pieEntries.add(new PieEntry(entry.getValue(), entry.getKey()));
        }

        if (pieEntries.isEmpty()) {
            pieEntries.add(new PieEntry(1f, "Chưa có dữ liệu"));
        }

        PieDataSet dataSet = new PieDataSet(pieEntries, "");

        // Dynamic Colors Palette
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#FDB623"));
        colors.add(Color.parseColor("#333333"));
        colors.add(Color.parseColor("#1A73E8"));
        colors.add(Color.parseColor("#34A853"));
        colors.add(Color.parseColor("#EA4335"));
        for (int c : ColorTemplate.MATERIAL_COLORS) colors.add(c);
        dataSet.setColors(colors);

        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setValueTextSize(12f);
        dataSet.setValueTextColor(Color.WHITE);

        PieData pieData = new PieData(dataSet);
        pieData.setValueFormatter(new PercentFormatter(pieChartDanhMuc));

        pieChartDanhMuc.setData(pieData);
        pieChartDanhMuc.setUsePercentValues(false);
        pieChartDanhMuc.getDescription().setEnabled(false);
        pieChartDanhMuc.setDrawHoleEnabled(true);
        pieChartDanhMuc.setHoleColor(Color.WHITE);
        pieChartDanhMuc.setHoleRadius(50f);
        pieChartDanhMuc.setTransparentCircleRadius(55f);
        pieChartDanhMuc.setEntryLabelColor(Color.parseColor("#333333"));
        pieChartDanhMuc.setEntryLabelTextSize(11f);

        pieChartDanhMuc.animateY(1200, Easing.EaseInOutQuad);
        pieChartDanhMuc.invalidate();
    }

    @Override
    public void onResume() {
        super.onResume();
        capNhatThongKe();
    }
}
