package com.nhale.repository;

import java.util.Date;
import java.util.List;

public interface StatsRepository {

    /**
     * @return list các mảng Object[],
     * 1 mảng gồm các phần tử id của danh mục, tên danh mục và phần tử chứa số lượng sản phẩm có trong danh mục đó
     */
    List<Object[]> cateStats();

    /**
     * thống kê doanh thu từng sản phẩm
     * lấy chọn lọc theo tên sản phẩm có chứa từ tìm kiếm truyền vào
     * và khoảng thời gian đặt hàng các sản phẩm đó
     *
     * @param kw       từ tìm kiếm
     * @param fromDate từ ngày
     * @param toDate   đến ngày
     * @return list các mảng Object, mỗi mảng chứa sản phẩm và doanh thu của nó
     */
    List<Object[]> productStats(String kw, Date fromDate, Date toDate);

    /**
     * thống kê doanh thu các sản phẩm theo từng mốc thời gian theo tháng, năm
     * lấy chọn lọc theo tên sản phẩm phải giống từ tìm kiếm truyền vào
     * và khoảng thời gian đặt hàng các sản phẩm đó
     *
     * @param kw       từ tìm kiếm
     * @param fromDate từ ngày
     * @param toDate   đến ngày
     * @return list các các mảng Object, mỗi mảng chứa tháng, năm và doanh thu của tháng năm đó
     */
    List<Object[]> productMonthStats(String kw, Date fromDate, Date toDate);

    /**
     * thống kê doanh thu các sản phẩm theo từng mốc thời gian theo ngày, tháng, năm
     * lấy chọn lọc theo tên sản phẩm phải giống từ tìm kiếm truyền vào
     * và khoảng thời gian đặt hàng các sản phẩm đó
     *
     * @param kw       từ tìm kiếm
     * @param fromDate từ ngày
     * @param toDate   đến ngày
     * @return list các các mảng Object, mỗi mảng chứa ngày, tháng, năm và doanh thu của tháng năm đó
     */
    List<Object[]> productDayStats(String kw, Date fromDate, Date toDate);

}
